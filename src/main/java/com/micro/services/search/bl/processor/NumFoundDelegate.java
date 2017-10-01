package com.micro.services.search.bl.processor;


import com.micro.services.search.api.request.SearchServiceRequest;
import com.micro.services.search.api.response.SearchServiceResponse;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.response.QueryResponse;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;

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