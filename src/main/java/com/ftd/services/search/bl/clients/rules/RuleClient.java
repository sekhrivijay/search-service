package com.ftd.services.search.bl.clients.rules;

import com.ftd.services.search.api.request.SearchServiceRequest;
import com.ftd.services.search.api.response.SearchServiceResponse;

public interface RuleClient {
    RuleServiceResponse callSearchRulesService(SearchServiceRequest searchServiceRequest,
                                               SearchServiceResponse searchServiceResponse);
}
