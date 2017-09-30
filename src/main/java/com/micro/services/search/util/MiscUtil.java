package com.micro.services.search.util;

import com.micro.services.search.api.response.SearchServiceResponse;

public class MiscUtil {
    public static boolean isValidResponse(SearchServiceResponse searchServiceResponse) {
        return searchServiceResponse != null
                && searchServiceResponse.getDocuments() != null
                && searchServiceResponse.getDocuments().size() > 0;
    }
}
