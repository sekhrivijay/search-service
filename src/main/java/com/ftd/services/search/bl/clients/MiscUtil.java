package com.ftd.services.search.bl.clients;

import com.ftd.services.product.api.domain.response.ProductServiceResponse;
import com.ftd.services.search.bl.clients.product.ProductClientImpl;
import com.ftd.services.search.bl.clients.rules.RuleServiceResponse;
import org.apache.commons.lang3.StringUtils;

import com.ftd.services.search.api.request.SearchServiceRequest;
import com.ftd.services.search.api.response.SearchServiceResponse;
import com.ftd.services.search.bl.clients.rules.RuleClientImpl;
import com.ftd.services.search.config.GlobalConstants;


public class MiscUtil {

    public static boolean isValidResponse(RuleServiceResponse ruleServiceResponse) {
        return ruleServiceResponse != RuleClientImpl.FALLBACK_RULE_RESPONSE;
    }

    public static boolean isValidResponse(ProductServiceResponse productServiceResponse) {
        return productServiceResponse != ProductClientImpl.FALLBACK_PRODUCT_RESPONSE;
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
