package com.micro.services.search.bl.processor;


import com.micro.services.search.api.request.ServiceRequest;
import com.micro.services.search.api.response.ServiceResponse;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.response.QueryResponse;

import javax.inject.Named;
import java.util.*;
@Named("productsDelegate")
public class ProductsDelegate extends BaseDelegate {
    @Override
    public SolrQuery preProcessQuery(SolrQuery solrQuery, ServiceRequest serviceRequest) {
        return solrQuery;
    }

    @Override
    public ServiceResponse postProcessResult(ServiceRequest serviceRequest, QueryResponse queryResponse, ServiceResponse serviceResponse) {
        if(queryResponse.getResults() == null) {
            return serviceResponse;
        }
        List<Map<String, String>> documents = buildProducts(queryResponse.getResults());
        if (documents.size() > 0) {
            serviceResponse.setDocuments(documents);
        }
        return serviceResponse;
    }


}
