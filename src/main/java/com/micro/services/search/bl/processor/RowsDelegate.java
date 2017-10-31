package com.micro.services.search.bl.processor;

import com.micro.services.search.api.request.RequestType;
import com.micro.services.search.api.request.SearchServiceRequest;
import com.micro.services.search.api.request.Site;
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

    @Value("${service.document.search.desktop.size}")
    private int searchDesktopRows;

    @Value("${service.document.search.mobile.size}")
    private int searchMobileRows;

    @Value("${service.document.browse.desktop.size}")
    private int browseDesktopRows;

    @Value("${service.document.browse.mobile.size}")
    private int browseMobileRows;

    @Value("${service.document.autofill.desktop.size}")
    private int autofillDesktopRows;

    @Value("${service.document.autofill.mobile.size}")
    private int autofillMobileRows;


    @Override
    public SolrQuery preProcessQuery(SolrQuery solrQuery, SearchServiceRequest searchServiceRequest) {
        solrQuery.setRows(getRows(searchServiceRequest.getRows(), getRows(searchServiceRequest)));
        return solrQuery;
    }

    @Override
    public SearchServiceResponse postProcessResult(SearchServiceRequest searchServiceRequest,
                                                   QueryResponse queryResponse,
                                                   SearchServiceResponse searchServiceResponse) {


        searchServiceResponse.setRows(getRows(searchServiceRequest.getRows(), getRows(searchServiceRequest.getRows(), getRows(searchServiceRequest))));
        return searchServiceResponse;
    }


    public int getRows(SearchServiceRequest searchServiceRequest) {
        RequestType requestType = searchServiceRequest.getRequestType();
        Site site = searchServiceRequest.getSite();
        if(site == Site.DESKTOP && requestType == RequestType.SEARCH) {
            return searchDesktopRows;
        }
        if(site == Site.MOBILE && requestType == RequestType.SEARCH) {
            return searchMobileRows;
        }

        if(site == Site.DESKTOP && requestType == RequestType.AUTOFILL) {
            return autofillDesktopRows;
        }
        if(site == Site.MOBILE && requestType == RequestType.AUTOFILL) {
            return autofillMobileRows;
        }

        if(site == Site.DESKTOP && requestType == RequestType.BROWSE) {
            return browseDesktopRows;
        }
        if(site == Site.MOBILE && requestType == RequestType.BROWSE) {
            return browseMobileRows;
        }

        return searchDesktopRows;
    }

}