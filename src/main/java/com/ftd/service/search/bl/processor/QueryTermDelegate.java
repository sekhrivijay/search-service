package com.ftd.service.search.bl.processor;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.response.QueryResponse;

import com.ftd.service.search.api.request.SearchServiceRequest;
import com.ftd.service.search.api.response.SearchServiceResponse;

import javax.inject.Named;

//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;

@Named("queryTermDelegate")
public class QueryTermDelegate extends BaseDelegate {
//    private static final Logger LOGGER = LoggerFactory.getLogger(QueryTermDelegate.class);

    @Override
    public SolrQuery preProcessQuery(SolrQuery solrQuery, SearchServiceRequest searchServiceRequest) {
        solrQuery.setQuery(searchServiceRequest.getQ());
        return solrQuery;
    }

    @Override
    public SearchServiceResponse postProcessResult(SearchServiceRequest searchServiceRequest,
                                                   QueryResponse queryResponse,
                                                   SearchServiceResponse searchServiceResponse) {
        return searchServiceResponse;
    }
}

