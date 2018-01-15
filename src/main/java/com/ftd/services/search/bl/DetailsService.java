package com.ftd.services.search.bl;

import com.ftd.services.search.api.request.SearchServiceRequest;
import com.ftd.services.search.api.response.SearchServiceResponse;

public interface DetailsService {
    void postQueryDetails(SearchServiceRequest searchServiceRequest, SearchServiceResponse searchServiceResponse);
}
