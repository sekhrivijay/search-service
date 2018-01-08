package com.micro.services.search.bl;

import com.micro.services.search.api.request.SearchServiceRequest;
import com.micro.services.search.api.response.SearchServiceResponse;

public interface DetailsService {
    void postQueryDetails(SearchServiceRequest searchServiceRequest, SearchServiceResponse searchServiceResponse);
}
