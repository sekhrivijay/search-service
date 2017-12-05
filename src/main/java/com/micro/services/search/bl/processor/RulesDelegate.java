package com.micro.services.search.bl.processor;

import com.micro.services.search.api.SearchModelWrapper;
import com.micro.services.search.api.request.Holder;
import com.micro.services.search.api.request.SearchServiceRequest;
import com.micro.services.search.api.response.SearchServiceResponse;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
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
        if (searchModelWrapperModified != null
                && searchModelWrapperModified.getSearchServiceResponse() != null
                && searchModelWrapperModified.getSearchServiceResponse().getRedirect() != null) {
            LOGGER.info(searchModelWrapperModified.toString());
            Holder holder = new Holder();
            holder.setRedirect(searchModelWrapperModified.getSearchServiceResponse().getRedirect());
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
        if (rulesServiceEnabled) {
            SearchModelWrapper searchModelWrapper = new SearchModelWrapper();
            searchModelWrapper.setSearchServiceRequest(searchServiceRequest);
            searchModelWrapper.setSearchServiceResponse(new SearchServiceResponse());
            HttpEntity<SearchModelWrapper> request = new HttpEntity<>(searchModelWrapper);
            LOGGER.info("Calling rule service ");
            ResponseEntity<SearchModelWrapper> response = restTemplate.exchange(
                    rulesServiceBaseUrl + "/executePre",
                    HttpMethod.POST, request,
                    SearchModelWrapper.class);
            return response.getBody();
        }
        return new SearchModelWrapper();
    }

    @Override
    public SearchServiceResponse postProcessResult(SearchServiceRequest searchServiceRequest,
                                                   QueryResponse queryResponse,
                                                   SearchServiceResponse searchServiceResponse) {
        return searchServiceResponse;
    }
}
