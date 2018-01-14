package com.ftd.service.search.bl;

import com.ftd.service.search.api.request.SearchServiceRequest;
import com.ftd.service.search.api.response.SearchServiceResponse;

public interface DetailsService {
    void postQueryDetails(SearchServiceRequest searchServiceRequest, SearchServiceResponse searchServiceResponse);
}
