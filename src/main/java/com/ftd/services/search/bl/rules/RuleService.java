package com.ftd.services.search.bl.rules;

import com.ftd.services.search.api.SearchModelWrapper;
import com.ftd.services.search.api.request.SearchServiceRequest;

public interface RuleService {
    SearchModelWrapper callSearchRulesService(SearchServiceRequest searchServiceRequest);
}
