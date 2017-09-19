package com.micro.services.search.bl.processor;

import com.micro.services.search.api.request.ServiceRequest;
import com.micro.services.search.api.response.Debug;
import com.micro.services.search.api.response.ServiceResponse;
import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.response.QueryResponse;

import javax.inject.Named;
import java.util.ArrayList;
import java.util.List;

@Named("debugDelegate")
public class DebugDelegate  extends BaseDelegate {
    private static Logger logger = Logger.getLogger(DebugDelegate.class.getName());

    private SolrQuery solrQuery;
    @Override
    public SolrQuery preProcessQuery(SolrQuery solrQuery, ServiceRequest serviceRequest) {
        logger.info("Debug enabled");
        this.solrQuery = solrQuery;
        return solrQuery;
    }

    @Override
    public ServiceResponse postProcessResult(ServiceRequest serviceRequest, QueryResponse queryResponse, ServiceResponse serviceResponse) {
        Debug debug = serviceResponse.getDebug();
        if(debug == null) {
            debug = new Debug();
        }
        List<String> queries = debug.getQueries();
        if(queries == null) {
            queries = new ArrayList<>();
            debug.setQueries(queries);
        }
        queries.add(solrQuery.toString());
        debug.setRound(serviceRequest.getRound());
        debug.setServiceRequest(serviceRequest);
        serviceResponse.setDebug(debug);
        return serviceResponse;
    }
}
