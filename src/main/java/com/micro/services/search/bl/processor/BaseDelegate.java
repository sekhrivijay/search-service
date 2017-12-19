package com.micro.services.search.bl.processor;

import com.micro.services.search.api.request.RequestType;
import com.micro.services.search.api.request.SearchServiceRequest;
import com.micro.services.search.api.request.Site;
import com.micro.services.search.api.response.SearchServiceResponse;
import com.micro.services.search.config.GlobalConstants;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;

public abstract class BaseDelegate implements Delegate {

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

    protected String buildOriginalQuery(SearchServiceRequest searchServiceRequest) {
        StringBuilder stringBuilder = new StringBuilder();
        if (searchServiceRequest.getParametersOriginal() == null) {
            return StringUtils.EMPTY;
        }
        for (String paramName : searchServiceRequest.getParametersOriginal().keySet()) {
            for (String value : searchServiceRequest.getParametersOriginal().get(paramName)) {
                stringBuilder
                        .append(GlobalConstants.AMPERSAND)
                        .append(paramName)
                        .append(GlobalConstants.EQUAL)
                        .append(value);
            }
        }
        return stringBuilder.toString();
    }

    protected String getQuery(SearchServiceResponse searchServiceResponse, String query, String endpoint) {
        if (queryContains(searchServiceResponse, query)) {
            return endpoint + searchServiceResponse.getOriginalQuery();
        }
        return endpoint + searchServiceResponse.getOriginalQuery() + query;
    }

    protected boolean queryContains(SearchServiceResponse searchServiceResponse, String query) {
        return searchServiceResponse.getOriginalQuery().contains(query);
    }

    protected int getRows(int rowsInput, int rows) {
        if (rowsInput != 0) {
            return rowsInput;
        }
        return rows;
    }

    public int getRows(SearchServiceRequest searchServiceRequest) {
        RequestType requestType = searchServiceRequest.getRequestType();
        Site site = searchServiceRequest.getSite();
        if (site == Site.DESKTOP && requestType == RequestType.SEARCH) {
            return searchDesktopRows;
        }
        if (site == Site.MOBILE && requestType == RequestType.SEARCH) {
            return searchMobileRows;
        }

        if (site == Site.DESKTOP && requestType == RequestType.AUTOFILL) {
            return autofillDesktopRows;
        }
        if (site == Site.MOBILE && requestType == RequestType.AUTOFILL) {
            return autofillMobileRows;
        }

        if (site == Site.DESKTOP && requestType == RequestType.BROWSE) {
            return browseDesktopRows;
        }
        if (site == Site.MOBILE && requestType == RequestType.BROWSE) {
            return browseMobileRows;
        }

        return searchDesktopRows;
    }
}
