package com.micro.services.search.bl.processor;

import com.micro.services.search.api.request.RequestType;
import com.micro.services.search.api.request.SearchServiceRequest;
import com.micro.services.search.api.response.SearchServiceResponse;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.response.QueryResponse;

import javax.inject.Named;

//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;

@Named("rowsDelegate")
public class RowsDelegate extends BaseDelegate {


    @Override
    public SolrQuery preProcessQuery(SolrQuery solrQuery, SearchServiceRequest searchServiceRequest) {
        solrQuery.setRows(getRows(searchServiceRequest.getRows(), getRows(searchServiceRequest)));
        return solrQuery;
    }

    @Override
    public SearchServiceResponse postProcessResult(SearchServiceRequest searchServiceRequest,
                                                   QueryResponse queryResponse,
                                                   SearchServiceResponse searchServiceResponse) {

        if(searchServiceRequest.getRequestType() == RequestType.AUTOFILL) {
            return searchServiceResponse;
        }
        searchServiceResponse.setRows(getRows(searchServiceRequest.getRows(), getRows(searchServiceRequest.getRows(), getRows(searchServiceRequest))));
        return searchServiceResponse;
    }


}