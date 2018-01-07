package com.micro.services.search.bl.rules;

import com.micro.services.search.api.SearchModelWrapper;
import com.micro.services.search.api.request.SearchServiceRequest;

public interface RuleService {
    SearchModelWrapper callSearchRulesService(SearchServiceRequest searchServiceRequest);
}
