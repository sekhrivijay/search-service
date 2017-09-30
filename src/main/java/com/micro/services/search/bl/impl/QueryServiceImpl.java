package com.micro.services.search.bl.impl;


import com.micro.services.search.api.request.SearchServiceRequest;
import com.micro.services.search.api.response.SearchServiceResponse;
import com.micro.services.search.bl.DelegateInitializer;
import com.micro.services.search.bl.processor.Delegate;
import com.micro.services.search.bl.task.QueryCommand;
import com.micro.services.search.bl.QueryService;
import com.micro.services.search.util.SolrUtil;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

@Service("queryService")
public class QueryServiceImpl implements QueryService {
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(QueryServiceImpl.class);


    private DelegateInitializer delegateInitializer;

    @Inject
    public void setDelegateInitializer(DelegateInitializer delegateInitializer) {
        this.delegateInitializer = delegateInitializer;
    }

    @Value("${service.solrQueryTimeout}")
    private long solrQueryTimeout;

    @Inject
    private QueryCommand queryCommand;


    @Cacheable(cacheNames = "default",
            key = "#searchServiceRequest.cacheKey",
            condition = "#searchServiceRequest.from != T(com.sears.search.service.api.response.From).INDEX",
            unless = "T(com.micro.services.search.util.MiscUtil).isValidResponse(#result) == false")
    public SearchServiceResponse query(SearchServiceRequest searchServiceRequest) throws Exception {
//        long startTime = System.currentTimeMillis();
        LOGGER.info(searchServiceRequest.toString());
        searchServiceRequest.setRound(searchServiceRequest.getRound() + 1);

        Map<String, List<Delegate>> delegateMapList = delegateInitializer.buildDelegateMapList(searchServiceRequest);
        Map<String, SolrQuery> solrQueryMap = new HashMap<>();
        for (String key : delegateMapList.keySet()) {
            SolrQuery solrQuery = new SolrQuery();
            for (Delegate delegate : delegateMapList.get(key)) {
                delegate.preProcessQuery(solrQuery, searchServiceRequest);
            }
            solrQueryMap.put(key, solrQuery);
        }
        Map<String, Future<QueryResponse>> futureMap = submitQueries(solrQueryMap);

        SearchServiceResponse searchServiceResponse = new SearchServiceResponse();
        for (String key : delegateMapList.keySet()) {
            QueryResponse queryResponse = SolrUtil.getQueryResponse(futureMap, key, solrQueryTimeout);
            if (queryResponse == null) {
                continue;
            }
            for (Delegate delegate : delegateMapList.get(key)) {
                delegate.postProcessResult(searchServiceRequest, queryResponse, searchServiceResponse);
            }
        }
        return searchServiceResponse;

    }


    public Map<String, Future<QueryResponse>> submitQueries(Map<String, SolrQuery> solrQueryMap) throws Exception {
        Map<String, Future<QueryResponse>> toReturn = new HashMap<>();
        for (String key : solrQueryMap.keySet()) {
            toReturn.put(key, queryCommand.run(solrQueryMap.get(key)));
        }
        return toReturn;
    }


}
