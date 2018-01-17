package com.ftd.services.search.bl.clients.availibility;

import com.ftd.services.search.api.request.SearchServiceRequest;
import com.ftd.services.search.api.response.SearchServiceResponse;

import java.util.Map;

public interface AvailabilityClient {
    AvailabilityServiceResponse
    callAvailabilityService(SearchServiceRequest searchServiceRequest,
                            SearchServiceResponse searchServiceResponse) throws Exception;

    Map<String, Object> buildMap(AvailabilityServiceResponse products);
}
