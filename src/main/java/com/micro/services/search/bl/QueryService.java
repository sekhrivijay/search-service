package com.micro.services.search.bl;


import com.micro.services.search.api.request.ServiceRequest;
import com.micro.services.search.api.response.ServiceResponse;

public interface QueryService {
    ServiceResponse query(ServiceRequest serviceRequest) throws Exception;
}
