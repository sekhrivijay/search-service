package com.ftd.service.search.bl;


import com.ftd.service.search.api.request.SearchServiceRequest;
import com.ftd.service.search.api.response.SearchServiceResponse;

public interface QueryService {
    SearchServiceResponse query(SearchServiceRequest searchServiceRequest) throws Exception;
    SearchServiceResponse queryAutofill(SearchServiceRequest searchServiceRequest) throws Exception;
}
