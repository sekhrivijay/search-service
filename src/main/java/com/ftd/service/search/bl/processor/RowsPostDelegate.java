package com.ftd.service.search.bl.processor;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.response.QueryResponse;

import com.ftd.service.search.api.request.SearchServiceRequest;
import com.ftd.service.search.api.response.SearchServiceResponse;

import javax.inject.Named;

@Named("rowsPostDelegate")
public class RowsPostDelegate extends BaseDelegate {

    @Override
    public SolrQuery preProcessQuery(SolrQuery solrQuery, SearchServiceRequest searchServiceRequest) {
        return solrQuery;
    }

    @Override
    public SearchServiceResponse postProcessResult(SearchServiceRequest searchServiceRequest,
                                                   QueryResponse queryResponse,
                                                   SearchServiceResponse searchServiceResponse) {

        searchServiceResponse.setRows(
                getRows(
                        searchServiceRequest.getRows(),
                        getRows(searchServiceRequest.getRows(), getRows(searchServiceRequest))));
        return searchServiceResponse;
    }


}