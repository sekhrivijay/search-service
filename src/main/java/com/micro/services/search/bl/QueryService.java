package com.micro.services.search.bl;


import com.micro.services.search.api.request.SearchServiceRequest;
import com.micro.services.search.api.response.SearchServiceResponse;

public interface QueryService {
    SearchServiceResponse query(SearchServiceRequest searchServiceRequest) throws Exception;
    SearchServiceResponse queryAutofill(SearchServiceRequest searchServiceRequest) throws Exception;
}
