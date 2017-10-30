package com.micro.services.search.util;

import com.micro.services.search.api.request.SearchServiceRequest;
import com.micro.services.search.api.response.SearchServiceResponse;
import com.micro.services.search.config.GlobalConstants;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;


public class MiscUtil {
    @Value("${spring.application.name}")
    private static String applicationName;

    @Value("${spring.profiles.active}")
    private static String environment;

    public static boolean isValidResponse(SearchServiceResponse searchServiceResponse) {
        return searchServiceResponse != null
                && searchServiceResponse.getDocuments() != null
                && searchServiceResponse.getDocuments().size() > 0;
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
        return applicationName.concat(environment.concat(childServiceRequest.toCacheKey()));
    }

}
