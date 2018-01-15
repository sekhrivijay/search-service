package com.ftd.services.search.bl.processor;


import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.response.QueryResponse;

import com.ftd.services.search.api.request.SearchServiceRequest;
import com.ftd.services.search.api.response.SearchServiceResponse;

import javax.inject.Named;

//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;

@Named("filterDelegate")
public class FilterDelegate extends BaseDelegate {
//    private static final Logger LOGGER = LoggerFactory.getLogger(FilterDelegate.class);

    @Override
    public SolrQuery preProcessQuery(SolrQuery solrQuery, SearchServiceRequest searchServiceRequest) {
        if (searchServiceRequest.getFq() != null) {
            solrQuery.setFilterQueries(searchServiceRequest.getFq());
        }
        return solrQuery;
    }

    @Override
    public SearchServiceResponse postProcessResult(SearchServiceRequest searchServiceRequest,
                                                   QueryResponse queryResponse,
                                                   SearchServiceResponse searchServiceResponse) {

        return searchServiceResponse;
    }
}

