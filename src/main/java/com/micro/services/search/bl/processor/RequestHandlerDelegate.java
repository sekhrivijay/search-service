package com.micro.services.search.bl.processor;


import com.micro.services.search.api.request.SearchServiceRequest;
import com.micro.services.search.api.response.SearchServiceResponse;
import com.micro.services.search.config.GlobalConstants;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.response.QueryResponse;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;

import javax.inject.Named;

@Named("requestHandlerDelegate")
public class RequestHandlerDelegate  extends BaseDelegate {
//    private static final Logger LOGGER = LoggerFactory.getLogger(RequestHandlerDelegate.class);

    @Override
    public SolrQuery preProcessQuery(SolrQuery solrQuery, SearchServiceRequest searchServiceRequest) {
        String requestHandler = GlobalConstants.SELECT_HANDLER;
        if (searchServiceRequest.getQt() != null) {
            requestHandler = searchServiceRequest.getQt();
        }

        solrQuery.setRequestHandler(GlobalConstants.FORWARD_SLASH + requestHandler);
        return solrQuery;
    }

    @Override
    public SearchServiceResponse postProcessResult(SearchServiceRequest searchServiceRequest,
                                                   QueryResponse queryResponse,
                                                   SearchServiceResponse searchServiceResponse) {
        return searchServiceResponse;
    }
}
