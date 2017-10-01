package com.micro.services.search.api;

import com.micro.services.search.api.request.SearchServiceRequest;
import com.micro.services.search.api.response.SearchServiceResponse;

import java.io.Serializable;

public class SearchModelWrapper  implements Serializable {
    private SearchServiceRequest searchServiceRequest;
    private SearchServiceResponse searchServiceResponse;

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
        return "SearchModelWrapper{" +
                "searchServiceRequest=" + searchServiceRequest +
                ", searchServiceResponse=" + searchServiceResponse +
                '}';
    }
}
