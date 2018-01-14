package com.ftd.service.search.util;

import org.apache.commons.lang3.StringUtils;

import com.ftd.service.search.api.SearchModelWrapper;
import com.ftd.service.search.api.request.SearchServiceRequest;
import com.ftd.service.search.api.response.SearchServiceResponse;
import com.ftd.service.search.bl.rules.RuleServiceImpl;
import com.ftd.service.search.config.GlobalConstants;


public class MiscUtil {

    public static boolean isValidResponse(SearchModelWrapper searchModelWrapper) {
        return searchModelWrapper != RuleServiceImpl.FALLBACK_RULE_RESPONSE;

    }
    public static boolean isValidResponse(SearchServiceResponse searchServiceResponse) {
        return searchServiceResponse != null
                && searchServiceResponse.isCacheable()
                && isValidData(searchServiceResponse);
    }

    public static boolean isValidData(SearchServiceResponse searchServiceResponse) {
        boolean noRedirectCase = searchServiceResponse.getRedirect() == null
                && searchServiceResponse.getDocumentList() != null
                && searchServiceResponse.getDocumentList().size() > 0;
        boolean redirectCase = searchServiceResponse.getRedirect() != null
                && searchServiceResponse.getRedirect().getRedirectUrl() != null;
        return noRedirectCase || redirectCase;

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
                        String.valueOf(childServiceRequest.getCacheKey())));
    }

}
