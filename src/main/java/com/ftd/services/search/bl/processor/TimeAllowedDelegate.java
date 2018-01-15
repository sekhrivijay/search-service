package com.ftd.services.search.bl.processor;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.response.QueryResponse;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import com.ftd.services.search.api.request.SearchServiceRequest;
import com.ftd.services.search.api.response.SearchServiceResponse;

import javax.inject.Named;

@Named("timeAllowedDelegate")
public class TimeAllowedDelegate  extends BaseDelegate {
//    private static final Logger LOGGER = LoggerFactory.getLogger(TimeAllowedDelegate.class);

    @Value("${service.queryTimeAllowed}")
    private int queryTimeAllowed;

    @Override
    public SolrQuery preProcessQuery(SolrQuery solrQuery, SearchServiceRequest searchServiceRequest) {
        solrQuery.setTimeAllowed(queryTimeAllowed);
        return solrQuery;
    }

    @Override
    public SearchServiceResponse postProcessResult(SearchServiceRequest searchServiceRequest,
                                                   QueryResponse queryResponse,
                                                   SearchServiceResponse searchServiceResponse) {
        return searchServiceResponse;
    }
}