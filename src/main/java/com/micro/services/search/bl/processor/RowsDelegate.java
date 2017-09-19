package com.micro.services.search.bl.processor;

import com.micro.services.search.api.request.ServiceRequest;
import com.micro.services.search.api.response.ServiceResponse;
import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.response.QueryResponse;

import javax.inject.Named;

@Named("rowsDelegate")
public class RowsDelegate  extends BaseDelegate {
    private static Logger logger = Logger.getLogger(RowsDelegate.class.getName());

    @Override
    public SolrQuery preProcessQuery(SolrQuery solrQuery, ServiceRequest serviceRequest) {
        if (serviceRequest.getRows() != 0) {
            solrQuery.setRows(serviceRequest.getRows());
        }

        return solrQuery;
    }

    @Override
    public ServiceResponse postProcessResult(ServiceRequest serviceRequest, QueryResponse queryResponse, ServiceResponse serviceResponse) {
        return serviceResponse;
    }
}