package com.micro.services.search.util;

import com.micro.services.search.api.response.ServiceResponse;

public class MiscUtil {
    public static boolean isValidResponse(ServiceResponse serviceResponse) {
        return serviceResponse != null && serviceResponse.getDocuments() != null && serviceResponse.getDocuments().size() > 0;
    }
}
