package com.ftd.services.search.bl;


import com.ftd.services.search.api.request.SearchServiceRequest;
import com.ftd.services.search.api.response.SearchServiceResponse;

public interface QueryService {
    SearchServiceResponse query(SearchServiceRequest searchServiceRequest) throws Exception;
    SearchServiceResponse queryAutofill(SearchServiceRequest searchServiceRequest) throws Exception;
}
