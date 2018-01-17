package com.ftd.services.search.bl.clients.rules;

import java.io.Serializable;

import com.ftd.services.search.api.request.SearchServiceRequest;
import com.ftd.services.search.api.response.SearchServiceResponse;

public class RuleServiceResponse implements Serializable {
    private SearchServiceRequest searchServiceRequest;
    private SearchServiceResponse searchServiceResponse;

    public RuleServiceResponse() {
    }

    public RuleServiceResponse(SearchServiceRequest searchServiceRequest, SearchServiceResponse searchServiceResponse) {
        this.searchServiceRequest = searchServiceRequest;
        this.searchServiceResponse = searchServiceResponse;
    }

    public SearchServiceRequest getSearchServiceRequest() {
        return searchServiceRequest;
    }

    public void setSearchServiceRequest(SearchServiceRequest searchServiceRequest) {
        this.searchServiceRequest = searchServiceRequest;
    }

    public SearchServiceResponse getSearchServiceResponse() {
        return searchServiceResponse;
    }

    public void setSearchServiceResponse(SearchServiceResponse searchServiceResponse) {
        this.searchServiceResponse = searchServiceResponse;
    }



    @Override
    public String toString() {
        return "RuleServiceResponse{" +
                "searchServiceRequest=" + searchServiceRequest +
                ", searchServiceResponse=" + searchServiceResponse +
                '}';
    }
}
