package com.micro.services.search.bl.impl;


import com.micro.services.search.api.request.RequestType;
import com.micro.services.search.api.request.SearchServiceRequest;
import com.micro.services.search.api.response.SearchServiceResponse;
import com.micro.services.search.bl.DelegateInitializer;
import com.micro.services.search.bl.processor.Delegate;
import com.micro.services.search.bl.task.QueryCommand;
import com.micro.services.search.bl.QueryService;
import com.micro.services.search.config.GlobalConstants;
import com.micro.services.search.util.SolrUtil;
import org.apache.commons.lang3.SerializationUtils;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
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
    private static final Logger LOGGER = LoggerFactory.getLogger(QueryServiceImpl.class);


    private DelegateInitializer delegateInitializer;

    @Inject
    public void setDelegateInitializer(DelegateInitializer delegateInitializer) {
        this.delegateInitializer = delegateInitializer;
    }

    @Value("${service.solrQueryTimeout}")
    private long solrQueryTimeout;

    @Value("${service.maxQueryRounds:5}")
    private long maxQueryRounds;

    @Value("${service.spellCheckNumfoundThreshhold:0}")
    private long spellCheckNumfoundThreshhold;

    @Value("${service.mustMatchNumfoundThreshhold:0}")
    private long mustMatchNumfoundThreshhold;

    @Value("${spring.application.name}")
    public void setApplicationName(String name) {
        GlobalConstants.APPLICATION_NAME = name;
    }

    @Value("${spring.profiles.active}")
    public void setEnvironment(String environment) {
        GlobalConstants.ENVIRONMENT = environment;
    }


    @Inject
    private QueryCommand queryCommand;


    @Cacheable(cacheNames = "default",
            key = "T(com.micro.services.search.util.MiscUtil).getCacheKey(#searchServiceRequest)",
            condition = "#searchServiceRequest.from != T(com.micro.services.search.api.request.From).INDEX",
            unless = "T(com.micro.services.search.util.MiscUtil).isValidResponse(#result) == false")
    public SearchServiceResponse query(SearchServiceRequest searchServiceRequest) throws Exception {
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
            SearchServiceResponse preProcessResponse = preProcessResponse(searchServiceRequest, key, queryResponse);
            if (preProcessResponse != null) {
                return preProcessResponse;
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

    public SearchServiceResponse preProcessResponse(
            SearchServiceRequest serviceRequest,
            String key,
            QueryResponse queryResponse) throws Exception {

        long numberOfResults = queryResponse.getResults().getNumFound();
        int round = serviceRequest.getRound();
        if (round >= maxQueryRounds
                || serviceRequest.getRequestType() == RequestType.AUTOFILL
                || serviceRequest.getRequestType() == RequestType.SPELL) {
            return null;
        }

        if (numberOfResults <= spellCheckNumfoundThreshhold && round == GlobalConstants.SPELL_CORRECT_SOLR_ROUND) {
            if (!serviceRequest.isSpellCheck()) {
                SearchServiceRequest spellCorrectServiceRequest = cloneRequest(serviceRequest);
                spellCorrectServiceRequest.setFuzzyCompare(true);
                spellCorrectServiceRequest.setSpellCheck(true);
                return query(spellCorrectServiceRequest);
            } else {
                serviceRequest.setRound(++round);
            }
        }

        int numberOfTermTokens = serviceRequest.getQ().split(GlobalConstants.SPACE).length;
        if (numberOfResults <= mustMatchNumfoundThreshhold
                && round == GlobalConstants.MUST_MATCH_ROUND_1) {
            if (!serviceRequest.isMustMatchSeventyFivePercent() && numberOfTermTokens > 1) {
                SearchServiceRequest mustMatchServiceRequest = cloneRequest(serviceRequest);
                mustMatchServiceRequest.setMustMatchSeventyFivePercent(true);
                return query(mustMatchServiceRequest);
            } else {
                serviceRequest.setRound(++round);
            }
        }
        if (numberOfResults <= mustMatchNumfoundThreshhold
                && round == GlobalConstants.MUST_MATCH_ROUND_2) {
            if (!serviceRequest.isMustMatchFiftyPercent() && numberOfTermTokens > 1) {
                SearchServiceRequest mustMatchServiceRequest = cloneRequest(serviceRequest);
                mustMatchServiceRequest.setMustMatchFiftyPercent(true);
                return query(mustMatchServiceRequest);
            } else {
                serviceRequest.setRound(++round);
            }
        }
//        if (numberOfResults < spellCheckNumfoundThreshhold
// && round == GlobalConstants.SPELL_CORRECT_LANGUAGE_TOOL_ROUND) {
//            SearchServiceRequest spellCorrectServiceRequest =
// spellCorrectService.buildCorrectSpellingsServiceRequest(queryResponse.getSpellCheckResponse(), serviceRequest);
//            if (spellCorrectServiceRequest.isSpellCheck()) {
//                return query(spellCorrectServiceRequest);
//            }
//        }
        return null;
    }

    @NotNull
    private SearchServiceRequest cloneRequest(SearchServiceRequest serviceRequest) {
        SearchServiceRequest spellCorrectServiceRequest = SerializationUtils.clone(serviceRequest);
        spellCorrectServiceRequest.setParent(serviceRequest);
        return spellCorrectServiceRequest;
    }

}
