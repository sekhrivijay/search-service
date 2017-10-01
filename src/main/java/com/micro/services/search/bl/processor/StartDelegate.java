package com.micro.services.search.bl.processor;


import com.micro.services.search.api.request.SearchServiceRequest;
import com.micro.services.search.api.response.SearchServiceResponse;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.response.QueryResponse;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;

import javax.inject.Named;

@Named("startDelegate")
public class StartDelegate  extends BaseDelegate {
//    private static final Logger LOGGER = LoggerFactory.getLogger(StartDelegate.class);
    @Override
    public SolrQuery preProcessQuery(SolrQuery solrQuery, SearchServiceRequest searchServiceRequest) {
        if (searchServiceRequest.getStart() != 0) {
            solrQuery.setStart(searchServiceRequest.getStart());
        }
        return solrQuery;
    }

    @Override
    public SearchServiceResponse postProcessResult(SearchServiceRequest searchServiceRequest,
                                                   QueryResponse queryResponse,
                                                   SearchServiceResponse searchServiceResponse) {

        return searchServiceResponse;
    }
}
