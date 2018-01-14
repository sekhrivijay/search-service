package com.ftd.service.search.bl.processor;


import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.response.QueryResponse;

import com.ftd.service.search.api.request.SearchServiceRequest;
import com.ftd.service.search.api.response.SearchServiceResponse;

public interface Delegate {
    SolrQuery preProcessQuery(SolrQuery solrQuery, SearchServiceRequest searchServiceRequest);
    SearchServiceResponse postProcessResult(SearchServiceRequest searchServiceRequest,
                                            QueryResponse queryResponse,
                                            SearchServiceResponse searchServiceResponse);
}
