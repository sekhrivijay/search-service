package com.micro.services.search.bl.processor;


import com.micro.services.search.api.request.ServiceRequest;
import com.micro.services.search.api.response.ServiceResponse;
import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.response.QueryResponse;

import javax.inject.Named;

@Named("startDelegate")
public class StartDelegate  extends BaseDelegate {
    private static Logger logger = Logger.getLogger(StartDelegate.class.getName());

    @Override
    public SolrQuery preProcessQuery(SolrQuery solrQuery, ServiceRequest serviceRequest) {
        if (serviceRequest.getStart() != 0) {
            solrQuery.setStart(serviceRequest.getStart());
        }
        return solrQuery;
    }

    @Override
    public ServiceResponse postProcessResult(ServiceRequest serviceRequest, QueryResponse queryResponse, ServiceResponse serviceResponse) {
        return serviceResponse;
    }
}
