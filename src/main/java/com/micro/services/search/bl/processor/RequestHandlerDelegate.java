package com.micro.services.search.bl.processor;


import com.micro.services.search.api.request.SearchServiceRequest;
import com.micro.services.search.api.response.SearchServiceResponse;
import com.micro.services.search.config.GlobalConstants;
import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.response.QueryResponse;

import javax.inject.Named;

@Named("requestHandlerDelegate")
public class RequestHandlerDelegate  extends BaseDelegate {
    private static Logger logger = Logger.getLogger(QueryTermDelegate.class.getName());

    @Override
    public SolrQuery preProcessQuery(SolrQuery solrQuery, SearchServiceRequest searchServiceRequest) {
        String requestHandler = (searchServiceRequest.getQt()!=null)?  searchServiceRequest.getQt(): GlobalConstants.SELECT_HANDLER;
        solrQuery.setRequestHandler(GlobalConstants.FORWARD_SLASH + requestHandler);
        return solrQuery;
    }

    @Override
    public SearchServiceResponse postProcessResult(SearchServiceRequest searchServiceRequest, QueryResponse queryResponse, SearchServiceResponse searchServiceResponse) {
        return searchServiceResponse;
    }
}
