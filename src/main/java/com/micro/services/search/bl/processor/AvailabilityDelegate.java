package com.micro.services.search.bl.processor;

import com.micro.services.search.api.request.SearchServiceRequest;
import com.micro.services.search.api.response.SearchServiceResponse;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.response.QueryResponse;

public class AvailabilityDelegate extends BaseDelegate {

    @Override
    public SolrQuery preProcessQuery(SolrQuery solrQuery, SearchServiceRequest searchServiceRequest) {
        return solrQuery;
    }

    @Override
    public SearchServiceResponse postProcessResult(SearchServiceRequest searchServiceRequest,
                                                   QueryResponse queryResponse,
                                                   SearchServiceResponse searchServiceResponse) {
        return searchServiceResponse;
    }
}
