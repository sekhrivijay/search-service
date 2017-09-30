package com.micro.services.search.bl.processor;

import com.micro.services.search.api.request.SearchServiceRequest;
import com.micro.services.search.api.response.SearchServiceResponse;
import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.response.QueryResponse;

import javax.inject.Named;

@Named("parameterDelegate")
public class ParameterDelegate extends BaseDelegate {
//    private static Logger logger = Logger.getLogger(ParameterDelegate.class.getName());

    @Override
    public SolrQuery preProcessQuery(SolrQuery solrQuery, SearchServiceRequest searchServiceRequest) {
        if (searchServiceRequest.getParameters() != null) {
            for (String key : searchServiceRequest.getParameters().keySet()) {
                if (key != null && searchServiceRequest.getParameters().get(key) != null) {
                    for (String value : searchServiceRequest.getParameters().get(key)) {
                        solrQuery.add(key, value);
                    }
                }
            }
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