package com.micro.services.search.util;

import com.micro.services.search.api.request.SearchServiceRequest;
import com.micro.services.search.api.response.SearchServiceResponse;
import com.micro.services.search.config.GlobalConstants;
import org.apache.commons.lang3.StringUtils;


public class MiscUtil {

    public static boolean isValidResponse(SearchServiceResponse searchServiceResponse) {
        return searchServiceResponse != null
                && searchServiceResponse.getDocumentList() != null
                && searchServiceResponse.getDocumentList().size() > 0;
    }

    public static String filterNonAlphaNumeric(String input, String pattern, String delimiter) {
        return StringUtils.normalizeSpace(input.replaceAll(pattern, delimiter));
    }

    public static String getCacheKey(SearchServiceRequest serviceRequest) {
        SearchServiceRequest parentServiceRequest = serviceRequest.getParent();
        SearchServiceRequest childServiceRequest = serviceRequest;
        while (parentServiceRequest != null) {
            childServiceRequest = parentServiceRequest;
            parentServiceRequest = parentServiceRequest.getParent();
        }
        return GlobalConstants.getApplicationName().concat(
                GlobalConstants.getEnvironment().concat(
                        childServiceRequest.toCacheKey()));
    }

}
