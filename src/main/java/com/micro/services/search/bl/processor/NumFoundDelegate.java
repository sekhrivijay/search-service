package com.micro.services.search.bl.processor;


import com.micro.services.search.api.request.ServiceRequest;
import com.micro.services.search.api.response.ServiceResponse;
import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.response.QueryResponse;

import javax.inject.Named;

@Named("numFoundDelegate")
public class NumFoundDelegate  extends BaseDelegate {
    private static Logger logger = Logger.getLogger(NumFoundDelegate.class.getName());

    @Override
    public SolrQuery preProcessQuery(SolrQuery solrQuery, ServiceRequest serviceRequest) {
        return solrQuery;
    }

    @Override
    public ServiceResponse postProcessResult(ServiceRequest serviceRequest, QueryResponse queryResponse, ServiceResponse serviceResponse) {
        if(queryResponse != null && queryResponse.getResults() != null) {
            serviceResponse.setNumFound(queryResponse.getResults().getNumFound());
        }
        return serviceResponse;
    }
}