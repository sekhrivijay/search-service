package com.micro.services.search.bl.impl;


import com.micro.services.search.api.request.ServiceRequest;
import com.micro.services.search.bl.DelegateInitializer;
import com.micro.services.search.bl.processor.Delegate;
import com.micro.services.search.bl.task.QueryCommand;
import com.micro.services.search.api.response.ServiceResponse;
import com.micro.services.search.bl.QueryService;
import com.micro.services.search.util.LogUtil;
import com.micro.services.search.util.SolrUtil;
import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.response.QueryResponse;
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
    private static Logger logger = Logger.getLogger(QueryServiceImpl.class.getName());

    @Inject
    DelegateInitializer delegateInitializer;

    @Value("${service.solrQueryTimeout}")
    private long solrQueryTimeout;

    @Inject
    private QueryCommand queryCommand;


    @Cacheable(cacheNames = "default", key = "#serviceRequest.cacheKey", condition = "#serviceRequest.from != T(com.sears.search.service.api.response.From).INDEX", unless = "T(com.micro.services.search.util.MiscUtil).isValidResponse(#result) == false")
    public ServiceResponse query(ServiceRequest serviceRequest) throws Exception {
        long startTime = System.currentTimeMillis();
        logger.info(serviceRequest);
        serviceRequest.setRound(serviceRequest.getRound() + 1);

        Map<String, List<Delegate>> delegateMapList = delegateInitializer.buildDelegateMapList(serviceRequest);
        Map<String, SolrQuery> solrQueryMap = new HashMap<>();
        for (String key : delegateMapList.keySet()) {
            SolrQuery solrQuery = new SolrQuery();
            for (Delegate delegate : delegateMapList.get(key)) {
                delegate.preProcessQuery(solrQuery, serviceRequest);
            }
            solrQueryMap.put(key, solrQuery);
        }
        Map<String, Future<QueryResponse>> futureMap = submitQueries(solrQueryMap);

        ServiceResponse serviceResponse = new ServiceResponse();
        for (String key : delegateMapList.keySet()) {
            QueryResponse queryResponse = SolrUtil.getQueryResponse(futureMap, key, solrQueryTimeout);
            if (queryResponse == null) {
                continue;
            }
            for (Delegate delegate : delegateMapList.get(key)) {
                delegate.postProcessResult(serviceRequest, queryResponse, serviceResponse);
            }
        }
        LogUtil.logTotalTimeTaken(logger, "QueryService", startTime);
        return serviceResponse;

    }


    public Map<String, Future<QueryResponse>> submitQueries(Map<String, SolrQuery> solrQueryMap) throws Exception {
        Map<String, Future<QueryResponse>> toReturn = new HashMap<>();
        for (String key : solrQueryMap.keySet()) {
            toReturn.put(key, queryCommand.run(solrQueryMap.get(key)));
        }
        return toReturn;
    }


}
