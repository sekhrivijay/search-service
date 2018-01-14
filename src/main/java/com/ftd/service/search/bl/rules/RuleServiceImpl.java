package com.ftd.service.search.bl.rules;

import com.codahale.metrics.annotation.ExceptionMetered;
import com.codahale.metrics.annotation.Timed;
import com.ftd.service.search.api.SearchModelWrapper;
import com.ftd.service.search.api.request.SearchServiceRequest;
import com.ftd.service.search.api.response.SearchServiceResponse;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import javax.inject.Named;

@Named("ruleService")
public class RuleServiceImpl implements RuleService {


    public static final SearchModelWrapper FALLBACK_RULE_RESPONSE = new SearchModelWrapper();

    public RuleServiceImpl() {
    }

    private RestTemplate restTemplate;

    @Value("${service.rulesService.enabled:false}")
    private boolean rulesServiceEnabled;

    @Value("${service.rulesService.baseUrl}")
    private String rulesServiceBaseUrl;


    @Autowired
    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }


    @Override
    @Timed
    @ExceptionMetered
    @HystrixCommand(groupKey = "hystrixGroup",
            commandKey = "ruleServiceKey",
            threadPoolKey = "ruleThreadPoolKey",
            fallbackMethod = "callSearchRulesServiceFallback")
    @Cacheable(cacheNames = "rules",
            key = "T(com.ftd.service.search.util.MiscUtil).getCacheKey(#searchServiceRequest)",
            condition = "#searchServiceRequest.from != T(com.ftd.service.search.api.request.From).INDEX",
            unless = "T(com.ftd.service.search.util.MiscUtil).isValidResponse(#result) == false")
    public SearchModelWrapper callSearchRulesService(SearchServiceRequest searchServiceRequest) {
        SearchModelWrapper searchModelWrapper = new SearchModelWrapper(
                searchServiceRequest,
                new SearchServiceResponse());
        if (rulesServiceEnabled) {
            HttpEntity<SearchModelWrapper> request = new HttpEntity<>(searchModelWrapper);
//            LOGGER.info("Calling rule service with ");
            ResponseEntity<SearchModelWrapper> response = restTemplate.exchange(
                    rulesServiceBaseUrl + "/executePre",
                    HttpMethod.POST, request,
                    SearchModelWrapper.class);
            return response.getBody();
        }
        return searchModelWrapper;
    }


    public SearchModelWrapper callSearchRulesServiceFallback(SearchServiceRequest searchServiceRequest) {
        return FALLBACK_RULE_RESPONSE;
    }
}
