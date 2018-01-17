package com.ftd.services.search.bl.clients.rules;

import com.codahale.metrics.annotation.ExceptionMetered;
import com.codahale.metrics.annotation.Timed;
import com.ftd.services.search.api.request.SearchServiceRequest;
import com.ftd.services.search.api.response.SearchServiceResponse;
import com.ftd.services.search.bl.clients.BaseClient;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import javax.inject.Named;

@Named("ruleService")
public class RuleClientImpl extends BaseClient implements RuleClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(RuleClientImpl.class);
    public static final RuleServiceResponse FALLBACK_RULE_RESPONSE = new RuleServiceResponse();

    public RuleClientImpl() {
    }

    private RestTemplate restTemplate;

    @Value("${service.rulesService.enabled:false}")
    private boolean enabled;

    @Value("${service.rulesService.baseUrl}")
    private String baseUrl;

    @Value("${service.ruleService.version:0.1}")
    private String version;

    @Autowired
    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @PostConstruct
    void logIdentification() {
        logIdentification(LOGGER, baseUrl, version, enabled);
    }

    @Override
    @Timed
    @ExceptionMetered
    @HystrixCommand(groupKey = "hystrixGroup",
            commandKey = "ruleServiceKey",
            threadPoolKey = "ruleThreadPoolKey",
            fallbackMethod = "callSearchRulesServiceFallback")
    @Cacheable(cacheNames = "rules",
            key = "T(com.ftd.services.search.util.MiscUtil).getCacheKey(#searchServiceRequest)",
            condition = "#searchServiceRequest.from != T(com.ftd.services.search.api.request.From).INDEX",
            unless = "T(com.ftd.services.search.util.MiscUtil).isValidResponse(#result) == false")
    public RuleServiceResponse callSearchRulesService(SearchServiceRequest searchServiceRequest,
                                                      SearchServiceResponse searchServiceResponse) {
        RuleServiceResponse ruleServiceResponse = new RuleServiceResponse(
                searchServiceRequest,
                searchServiceResponse);
        if (!enabled) {
            return ruleServiceResponse;
        }
        HttpEntity<RuleServiceResponse> request = new HttpEntity<>(ruleServiceResponse);
//            LOGGER.info("Calling rule service with ");
        ResponseEntity<RuleServiceResponse> response = restTemplate.exchange(
                baseUrl + "/executePre",
                HttpMethod.POST, request,
                RuleServiceResponse.class);
        return response.getBody();
    }


    public RuleServiceResponse callSearchRulesServiceFallback(
            SearchServiceRequest searchServiceRequest,
            SearchServiceResponse searchServiceResponse) {
        return FALLBACK_RULE_RESPONSE;
    }
}
