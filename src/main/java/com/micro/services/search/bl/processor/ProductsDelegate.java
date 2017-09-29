package com.micro.services.search.bl.processor;


import com.micro.services.search.api.request.SearchServiceRequest;
import com.micro.services.search.api.response.SearchServiceResponse;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.response.QueryResponse;

import javax.inject.Named;
import java.util.*;
@Named("productsDelegate")
public class ProductsDelegate extends BaseDelegate {
    @Override
    public SolrQuery preProcessQuery(SolrQuery solrQuery, SearchServiceRequest searchServiceRequest) {
        return solrQuery;
    }

    @Override
    public SearchServiceResponse postProcessResult(SearchServiceRequest searchServiceRequest, QueryResponse queryResponse, SearchServiceResponse searchServiceResponse) {
        if(queryResponse.getResults() == null) {
            return searchServiceResponse;
        }
        List<Map<String, String>> documents = buildProducts(queryResponse.getResults());
        if (documents.size() > 0) {
            searchServiceResponse.setDocuments(documents);
        }
        return searchServiceResponse;
    }


}
