package com.micro.services.search.bl.processor;

import com.micro.services.search.api.request.ServiceRequest;
import com.micro.services.search.api.response.ServiceResponse;
import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.response.QueryResponse;

import javax.inject.Named;

@Named("parameterDelegate")
public class ParameterDelegate extends BaseDelegate {
    private static Logger logger = Logger.getLogger(ParameterDelegate.class.getName());

    @Override
    public SolrQuery preProcessQuery(SolrQuery solrQuery, ServiceRequest serviceRequest) {
        if (serviceRequest.getParameters() != null) {
            for (String key : serviceRequest.getParameters().keySet()) {
                if (key != null && serviceRequest.getParameters().get(key) != null) {
                    for(String value : serviceRequest.getParameters().get(key)) {
                        solrQuery.add(key, value);
                    }
                }
            }
        }
        return solrQuery;
    }

    @Override
    public ServiceResponse postProcessResult(ServiceRequest serviceRequest, QueryResponse queryResponse, ServiceResponse serviceResponse) {
        return serviceResponse;
    }
}