package com.micro.services.search.bl.processor;

import com.google.gson.Gson;
import com.micro.services.search.api.SearchModelWrapper;
import com.micro.services.search.api.request.Holder;
import com.micro.services.search.api.request.SearchServiceRequest;
import com.micro.services.search.api.response.SearchServiceResponse;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import javax.inject.Named;
import java.lang.reflect.InvocationTargetException;

@Named("rulesDelegate")
public class RulesDelegate extends BaseDelegate {
    private static final Logger LOGGER = LoggerFactory.getLogger(RulesDelegate.class);

    private RestTemplate restTemplate;

    @Value("${service.rulesService.enabled:false}")
    private boolean rulesServiceEnabled;

    @Value("${service.rulesService.baseUrl}")
    private String rulesServiceBaseUrl;

    public static final Holder FALLBACK_RULE_RESPONSE = new Holder();

    public RulesDelegate() {
        FALLBACK_RULE_RESPONSE.setCacheable(false);
    }

    @Autowired
    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    @HystrixCommand(groupKey = "hystrixGroup",
            commandKey = "ruleServiceKey",
            threadPoolKey = "ruleThreadPoolKey",
            fallbackMethod = "preProcessFallback")
    public SolrQuery preProcessQuery(SolrQuery solrQuery, SearchServiceRequest searchServiceRequest) {
        SearchModelWrapper searchModelWrapperModified = callSearchRulesService(searchServiceRequest);
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

    public SolrQuery preProcessFallback(SolrQuery solrQuery, SearchServiceRequest searchServiceRequest) {
        searchServiceRequest.setHolder(FALLBACK_RULE_RESPONSE);
        return solrQuery;
    }

    private SearchModelWrapper callSearchRulesService(SearchServiceRequest searchServiceRequest) {
//        LOGGER.info(searchServiceRequest.toString());
        SearchModelWrapper searchModelWrapper = new SearchModelWrapper(
                searchServiceRequest,
                new SearchServiceResponse());
        if (rulesServiceEnabled) {
            HttpEntity<SearchModelWrapper> request = new HttpEntity<>(searchModelWrapper);
            LOGGER.info("Calling rule service with ");
            LOGGER.info(new Gson().toJson(searchModelWrapper.toString()));
            ResponseEntity<SearchModelWrapper> response = restTemplate.exchange(
                    rulesServiceBaseUrl + "/executePre",
                    HttpMethod.POST, request,
                    SearchModelWrapper.class);
            return response.getBody();
        }
        return searchModelWrapper;
    }

    @Override
    public SearchServiceResponse postProcessResult(SearchServiceRequest searchServiceRequest,
                                                   QueryResponse queryResponse,
                                                   SearchServiceResponse searchServiceResponse) {
        return searchServiceResponse;
    }
}
