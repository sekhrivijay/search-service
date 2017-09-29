package com.micro.services.search.bl.processor;


import com.micro.services.search.api.request.SearchServiceRequest;
import com.micro.services.search.api.response.SearchServiceResponse;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.response.QueryResponse;

public interface Delegate {
    SolrQuery preProcessQuery(SolrQuery solrQuery, SearchServiceRequest searchServiceRequest);
    SearchServiceResponse postProcessResult(SearchServiceRequest searchServiceRequest, QueryResponse queryResponse, SearchServiceResponse searchServiceResponse);
}
