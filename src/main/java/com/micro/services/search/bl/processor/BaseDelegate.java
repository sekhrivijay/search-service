package com.micro.services.search.bl.processor;

import com.micro.services.search.api.request.RequestType;
import com.micro.services.search.api.request.SearchServiceRequest;
import com.micro.services.search.api.request.Site;
import com.micro.services.search.api.response.SearchServiceResponse;
import com.micro.services.search.config.GlobalConstants;
import com.micro.services.search.util.SolrDocumentUtil;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.springframework.beans.factory.annotation.Value;

import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public abstract class BaseDelegate implements Delegate {

    @Value("${service.document.search.desktop.size}")
    protected int searchDesktopRows;

    @Value("${service.document.search.mobile.size}")
    protected int searchMobileRows;

    @Value("${service.document.browse.desktop.size}")
    protected int browseDesktopRows;

    @Value("${service.document.browse.mobile.size}")
    protected int browseMobileRows;

    @Value("${service.document.autofill.desktop.size}")
    protected int autofillDesktopRows;

    @Value("${service.document.autofill.mobile.size}")
    protected int autofillMobileRows;

    protected String buildOriginalQuery(SearchServiceRequest searchServiceRequest) {
        StringBuilder stringBuilder = new StringBuilder();
        for (String paramName : searchServiceRequest.getParametersOriginal().keySet()) {
            for(String value : searchServiceRequest.getParametersOriginal().get(paramName) ) {
                stringBuilder
                        .append(GlobalConstants.AMPERSAND)
                        .append(paramName)
                        .append(GlobalConstants.EQUAL)
                        .append(value);
            }
        }
        return stringBuilder.toString();
    }

    protected String getQuery(SearchServiceResponse searchServiceResponse, String query) {
        if(queryContains(searchServiceResponse, query)) {
            return searchServiceResponse.getOriginalQuery();
        }
        return searchServiceResponse.getOriginalQuery() + query;
    }

    protected boolean queryContains(SearchServiceResponse searchServiceResponse, String query) {
        return searchServiceResponse.getOriginalQuery().contains(query);
    }

    protected  int getRows(int rowsInput, int rows) {
        if (rowsInput != 0) {
            return rowsInput;
        }
        return rows;
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
