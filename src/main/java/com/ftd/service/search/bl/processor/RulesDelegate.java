package com.ftd.service.search.bl.processor;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ftd.service.search.api.SearchModelWrapper;
import com.ftd.service.search.api.request.Holder;
import com.ftd.service.search.api.request.SearchServiceRequest;
import com.ftd.service.search.api.response.SearchServiceResponse;
import com.ftd.service.search.bl.rules.RuleService;
import com.ftd.service.search.bl.rules.RuleServiceImpl;

import javax.inject.Inject;
import javax.inject.Named;
import java.lang.reflect.InvocationTargetException;

@Named("rulesDelegate")
public class RulesDelegate extends BaseDelegate {
    private static final Logger LOGGER = LoggerFactory.getLogger(RulesDelegate.class);

//    private RestTemplate restTemplate;

//    @Value("${service.rulesService.enabled:false}")
//    private boolean rulesServiceEnabled;
//
//    @Value("${service.rulesService.baseUrl}")
//    private String rulesServiceBaseUrl;

    private RuleService ruleService;

    public static final Holder FALLBACK_RULE_RESPONSE = new Holder();

    public RulesDelegate() {
        FALLBACK_RULE_RESPONSE.setCacheable(false);
    }

    @Inject
    @Named("ruleService")
    public void setRuleService(RuleService ruleService) {
        this.ruleService = ruleService;
    }
//
//    @Autowired
//    public void setRestTemplate(RestTemplate restTemplate) {
//        this.restTemplate = restTemplate;
//    }

    @Override
//    @HystrixCommand(groupKey = "hystrixGroup",
//            commandKey = "ruleServiceKey",
//            threadPoolKey = "ruleThreadPoolKey",
//            fallbackMethod = "preProcessFallback")
    public SolrQuery preProcessQuery(SolrQuery solrQuery, SearchServiceRequest searchServiceRequest) {
        SearchModelWrapper searchModelWrapperModified = ruleService.callSearchRulesService(searchServiceRequest);
        if (searchModelWrapperModified == RuleServiceImpl.FALLBACK_RULE_RESPONSE) {
            searchServiceRequest.setHolder(FALLBACK_RULE_RESPONSE);
            return solrQuery;
        }
        if (searchModelWrapperModified != null) {
            LOGGER.info("Response from Rule service " + searchModelWrapperModified.toString());
            Holder holder = new Holder();
            if (searchModelWrapperModified.getSearchServiceResponse() != null
                    && searchModelWrapperModified.getSearchServiceResponse().getRedirect() != null) {
                holder.setRedirect(searchModelWrapperModified.getSearchServiceResponse().getRedirect());
                holder.setRedirect(true);

            }
            if (searchModelWrapperModified.getSearchServiceRequest() != null) {
                try {
                    BeanUtils.copyProperties(
                            searchServiceRequest,
                            searchModelWrapperModified.getSearchServiceRequest());
                } catch (IllegalAccessException | InvocationTargetException e) {
                    LOGGER.error("Error copying object state from rule service ", e);
                }
            }
            searchServiceRequest.setHolder(holder);
        }
        return solrQuery;
    }

//    public SolrQuery preProcessFallback(SolrQuery solrQuery, SearchServiceRequest searchServiceRequest) {
//        searchServiceRequest.setHolder(FALLBACK_RULE_RESPONSE);
//        return solrQuery;
//    }

//    private SearchModelWrapper callSearchRulesService(SearchServiceRequest searchServiceRequest) {
////        LOGGER.info(searchServiceRequest.toString());
//        SearchModelWrapper searchModelWrapper = new SearchModelWrapper(
//                searchServiceRequest,
//                new SearchServiceResponse());
//        if (rulesServiceEnabled) {
//            HttpEntity<SearchModelWrapper> request = new HttpEntity<>(searchModelWrapper);
////            LOGGER.info("Calling rule service with ");
//            ResponseEntity<SearchModelWrapper> response = restTemplate.exchange(
//                    rulesServiceBaseUrl + "/executePre",
//                    HttpMethod.POST, request,
//                    SearchModelWrapper.class);
//            return response.getBody();
//        }
//        return searchModelWrapper;
//    }

    @Override
    public SearchServiceResponse postProcessResult(SearchServiceRequest searchServiceRequest,
                                                   QueryResponse queryResponse,
                                                   SearchServiceResponse searchServiceResponse) {
        return searchServiceResponse;
    }
}
