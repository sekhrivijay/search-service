package com.ftd.service.search.bl.rules;

import com.ftd.service.search.api.SearchModelWrapper;
import com.ftd.service.search.api.request.SearchServiceRequest;

public interface RuleService {
    SearchModelWrapper callSearchRulesService(SearchServiceRequest searchServiceRequest);
}
