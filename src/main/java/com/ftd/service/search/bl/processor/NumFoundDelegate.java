package com.ftd.service.search.bl.processor;


import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.response.QueryResponse;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;

import com.ftd.service.search.api.request.SearchServiceRequest;
import com.ftd.service.search.api.response.SearchServiceResponse;

import javax.inject.Named;

@Named("numFoundDelegate")
public class NumFoundDelegate  extends BaseDelegate {
//    private static final Logger LOGGER = LoggerFactory.getLogger(NumFoundDelegate.class);
    @Override
    public SolrQuery preProcessQuery(SolrQuery solrQuery, SearchServiceRequest searchServiceRequest) {
        return solrQuery;
    }

    @Override
    public SearchServiceResponse postProcessResult(SearchServiceRequest searchServiceRequest,
                                                   QueryResponse queryResponse,
                                                   SearchServiceResponse searchServiceResponse) {
        if (queryResponse != null && queryResponse.getResults() != null) {
            searchServiceResponse.setNumFound(queryResponse.getResults().getNumFound());
        }
        return searchServiceResponse;
    }
}