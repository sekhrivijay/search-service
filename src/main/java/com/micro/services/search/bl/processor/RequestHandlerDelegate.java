package com.micro.services.search.bl.processor;


import com.micro.services.search.api.request.ServiceRequest;
import com.micro.services.search.config.GlobalConstants;
import com.micro.services.search.api.response.ServiceResponse;
import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.response.QueryResponse;

import javax.inject.Named;

@Named("requestHandlerDelegate")
public class RequestHandlerDelegate  extends BaseDelegate {
    private static Logger logger = Logger.getLogger(QueryTermDelegate.class.getName());

    @Override
    public SolrQuery preProcessQuery(SolrQuery solrQuery, ServiceRequest serviceRequest) {
        String requestHandler = (serviceRequest.getQt()!=null)?  serviceRequest.getQt(): GlobalConstants.SELECT_HANDLER;
        solrQuery.setRequestHandler(GlobalConstants.FORWARD_SLASH + requestHandler);
        return solrQuery;
    }

    @Override
    public ServiceResponse postProcessResult(ServiceRequest serviceRequest, QueryResponse queryResponse, ServiceResponse serviceResponse) {
        return serviceResponse;
    }
}
