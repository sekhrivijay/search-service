package com.micro.services.search.bl.processor;

import com.micro.services.search.api.request.SearchServiceRequest;
import com.micro.services.search.api.response.Page;
import com.micro.services.search.api.response.Pagination;
import com.micro.services.search.api.response.SearchServiceResponse;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.springframework.beans.factory.annotation.Value;

import javax.inject.Named;
import java.util.ArrayList;
import java.util.List;

//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;

@Named("rowsDelegate")
public class RowsDelegate extends BaseDelegate {
//    private static final Logger LOGGER = LoggerFactory.getLogger(RowsDelegate.class);

    @Value("${service.defaultRows}")
    private int rows;

    @Override
    public SolrQuery preProcessQuery(SolrQuery solrQuery, SearchServiceRequest searchServiceRequest) {
        solrQuery.setRows(getRows(searchServiceRequest.getRows(), rows));
        return solrQuery;
    }

    @Override
    public SearchServiceResponse postProcessResult(SearchServiceRequest searchServiceRequest,
                                                   QueryResponse queryResponse,
                                                   SearchServiceResponse searchServiceResponse) {

        searchServiceResponse.setRows(getRows(searchServiceRequest.getRows(), rows));
        return searchServiceResponse;
    }

}