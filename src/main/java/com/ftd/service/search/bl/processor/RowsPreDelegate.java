package com.ftd.service.search.bl.processor;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.response.QueryResponse;

import com.ftd.service.search.api.request.SearchServiceRequest;
import com.ftd.service.search.api.response.SearchServiceResponse;

import javax.inject.Named;

//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;

@Named("rowsPreDelegate")
public class RowsPreDelegate extends BaseDelegate {


    @Override
    public SolrQuery preProcessQuery(SolrQuery solrQuery, SearchServiceRequest searchServiceRequest) {
        solrQuery.setRows(getRows(searchServiceRequest.getRows(), getRows(searchServiceRequest)));
        return solrQuery;
    }

    @Override
    public SearchServiceResponse postProcessResult(SearchServiceRequest searchServiceRequest,
                                                   QueryResponse queryResponse,
                                                   SearchServiceResponse searchServiceResponse) {
        return searchServiceResponse;
    }


}