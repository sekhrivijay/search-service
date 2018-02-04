package com.ftd.services.search.bl.impl;


import org.apache.commons.lang3.SerializationUtils;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.ftd.services.search.api.request.RequestType;
import com.ftd.services.search.api.request.SearchServiceRequest;
import com.ftd.services.search.api.response.SearchServiceResponse;
import com.ftd.services.search.bl.DelegateInitializer;
import com.ftd.services.search.bl.QueryService;
import com.ftd.services.search.bl.processor.Delegate;
import com.ftd.services.search.bl.processor.RulesDelegate;
import com.ftd.services.search.bl.clients.solr.EnhancedSolrClient;
import com.ftd.services.search.bl.clients.solr.EnhancedSolrClientImpl;
import com.ftd.services.search.config.GlobalConstants;
import com.ftd.services.search.bl.clients.solr.util.SolrUtil;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

@Service("queryService")
public class QueryServiceImpl implements QueryService {
    private static final Logger LOGGER = LoggerFactory.getLogger(QueryServiceImpl.class);


    private DelegateInitializer delegateInitializer;
    private SolrUtil solrUtil;

    @Inject
    public void setSolrUtil(SolrUtil solrUtil) {
        this.solrUtil = solrUtil;
    }

    @Inject
    public void setDelegateInitializer(DelegateInitializer delegateInitializer) {
        this.delegateInitializer = delegateInitializer;
    }

    @Value("${service.solrService.queryTimeout}")
    private long solrQueryTimeout;

    @Value("${service.maxQueryRounds:5}")
    private long maxQueryRounds;

    @Value("${service.spellCheckNumfoundThreshhold:0}")
    private long spellCheckNumfoundThreshhold;

    @Value("${service.mustMatchNumfoundThreshhold:0}")
    private long mustMatchNumfoundThreshhold;

    @Value("${spring.application.name}")
    public void setApplicationName(String name) {
        GlobalConstants.setApplicationName(name);
    }

    @Value("${spring.profiles.active}")
    public void setEnvironment(String environment) {
        GlobalConstants.setEnvironment(environment);
    }

    private EnhancedSolrClient enhancedSolrClient;

    @Inject
    public void setEnhancedSolrClient(EnhancedSolrClient enhancedSolrClient) {
        this.enhancedSolrClient = enhancedSolrClient;
    }


    @Cacheable(cacheNames = "autofill",
            key = "T(com.ftd.services.search.bl.clients.MiscUtil).getCacheKey(#searchServiceRequest)",
            condition = "#searchServiceRequest.from != T(com.ftd.services.search.api.request.From).INDEX",
            unless = "T(com.ftd.services.search.bl.clients.MiscUtil).isValidResponse(#result) == false")
    public SearchServiceResponse queryAutofill(SearchServiceRequest searchServiceRequest) throws Exception {
        return query(searchServiceRequest);
    }

    @Cacheable(cacheNames = "search",
            key = "T(com.ftd.services.search.bl.clients.MiscUtil).getCacheKey(#searchServiceRequest)",
            condition = "#searchServiceRequest.from != T(com.ftd.services.search.api.request.From).INDEX",
            unless = "T(com.ftd.services.search.bl.clients.MiscUtil).isValidResponse(#result) == false")
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

        SearchServiceResponse searchServiceResponse = new SearchServiceResponse();

        SearchServiceResponse preProcessResponse = preProcessRequest(searchServiceRequest, searchServiceResponse);
        if (preProcessResponse != null) {
            return preProcessResponse;
        }

        Map<String, Future<QueryResponse>> futureMap = submitQueries(solrQueryMap);


        for (String key : delegateMapList.keySet()) {
            QueryResponse queryResponse = solrUtil.getQueryResponse(futureMap, key, solrQueryTimeout);
            if (queryResponse == null || queryResponse == EnhancedSolrClientImpl.FALLBACK_QUERY_RESPONSE) {
                searchServiceResponse.setCacheable(false);
                continue;
            }
            preProcessResponse = preProcessResponse(searchServiceRequest, key, queryResponse);
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
            toReturn.put(key, enhancedSolrClient.runInThread(solrQueryMap.get(key)));
        }
        return toReturn;
    }


    public SearchServiceResponse preProcessRequest(
            SearchServiceRequest searchServiceRequest,
            SearchServiceResponse searchServiceResponse) throws Exception {
        if (searchServiceRequest != null
                && searchServiceRequest.getHolder() != null) {
            if (searchServiceRequest.getHolder() == RulesDelegate.FALLBACK_RULE_RESPONSE) {
                searchServiceResponse.setCacheable(false);
            }
            if (searchServiceRequest.getHolder().getRedirect() != null
                    && searchServiceRequest.getHolder().getRedirect().getRedirectUrl() != null) {
                SearchServiceResponse searchServiceResponseOut = new SearchServiceResponse();
                searchServiceResponseOut.setRedirect(searchServiceRequest.getHolder().getRedirect());
                return searchServiceResponseOut;
            }
        }
        return null;
    }


    public SearchServiceResponse preProcessResponse(
            SearchServiceRequest serviceRequest,
            String key,
            QueryResponse queryResponse) throws Exception {

        if (queryResponse == null || queryResponse.getResults() == null) {
            return null;
        }
        long numberOfResults = queryResponse.getResults().getNumFound();
        int round = serviceRequest.getRound();
//        serviceRequest.setRound(++round);
        LOGGER.info("Round " + round);
        if (round >= maxQueryRounds
                || serviceRequest.getRequestType() == RequestType.AUTOFILL
                || serviceRequest.getRequestType() == RequestType.PDP
                || serviceRequest.getRequestType() == RequestType.SPELL) {
            return null;
        }

        if (numberOfResults <= spellCheckNumfoundThreshhold && round == GlobalConstants.SPELL_CORRECT_SOLR_ROUND) {
            if (!serviceRequest.isSpellCheck()) {
                SearchServiceRequest spellCorrectServiceRequest = cloneRequest(serviceRequest);
                spellCorrectServiceRequest.setFuzzyCompare(true);
                spellCorrectServiceRequest.setSpellCheck(true);
                return query(spellCorrectServiceRequest);
            }
        }

        int numberOfTermTokens = getLength(serviceRequest);
        if (numberOfResults <= mustMatchNumfoundThreshhold
                && round == GlobalConstants.MUST_MATCH_ROUND_1) {
            if (!serviceRequest.isMustMatchSeventyFivePercent() && numberOfTermTokens > 1) {
                SearchServiceRequest mustMatchServiceRequest = cloneRequest(serviceRequest);
                mustMatchServiceRequest.setMustMatchSeventyFivePercent(true);
                return query(mustMatchServiceRequest);
            }

        }
        if (numberOfResults <= mustMatchNumfoundThreshhold
                && round == GlobalConstants.MUST_MATCH_ROUND_2) {
            if (!serviceRequest.isMustMatchFiftyPercent() && numberOfTermTokens > 1) {
                SearchServiceRequest mustMatchServiceRequest = cloneRequest(serviceRequest);
                mustMatchServiceRequest.setMustMatchFiftyPercent(true);
                return query(mustMatchServiceRequest);
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

    private int getLength(SearchServiceRequest serviceRequest) {
        if (serviceRequest == null || serviceRequest.getQ() == null) {
            return 0;
        }
        return serviceRequest.getQ().split(GlobalConstants.SPACE).length;
    }

    @NotNull
    private SearchServiceRequest cloneRequest(SearchServiceRequest serviceRequest) {
        SearchServiceRequest spellCorrectServiceRequest = SerializationUtils.clone(serviceRequest);
        spellCorrectServiceRequest.setParent(serviceRequest);
        return spellCorrectServiceRequest;
    }

}
