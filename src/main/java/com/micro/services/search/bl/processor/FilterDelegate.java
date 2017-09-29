package com.micro.services.search.bl.processor;


import com.micro.services.search.api.request.SearchServiceRequest;
import com.micro.services.search.api.response.SearchServiceResponse;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.response.QueryResponse;

import javax.inject.Named;

@Named("filterDelegate")
public class FilterDelegate  extends BaseDelegate {
    private static Logger logger = Logger.getLogger(FilterDelegate.class.getName());

    @Override
    public SolrQuery preProcessQuery(SolrQuery solrQuery, SearchServiceRequest searchServiceRequest) {
        if(StringUtils.isNotEmpty(searchServiceRequest.getFq())) {
            solrQuery.setFilterQueries(searchServiceRequest.getFq());
        }
        return solrQuery;
    }

    @Override
    public SearchServiceResponse postProcessResult(SearchServiceRequest searchServiceRequest, QueryResponse queryResponse, SearchServiceResponse searchServiceResponse) {
        return searchServiceResponse;
    }
}

