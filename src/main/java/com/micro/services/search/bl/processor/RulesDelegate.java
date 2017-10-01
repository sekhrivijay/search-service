package com.micro.services.search.bl.processor;

import com.micro.services.search.api.SearchModelWrapper;
import com.micro.services.search.api.request.SearchServiceRequest;
import com.micro.services.search.api.response.SearchServiceResponse;
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

    @Value("${service.rulesService.baseUrl}")
    private String rulesServiceBaseUrl;

    @Autowired
    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public SolrQuery preProcessQuery(SolrQuery solrQuery, SearchServiceRequest searchServiceRequest) {
        SearchModelWrapper searchModelWrapper = new SearchModelWrapper();
        searchModelWrapper.setSearchServiceRequest(new SearchServiceRequest());
        HttpEntity<SearchModelWrapper> request = new HttpEntity<>(searchModelWrapper);
        LOGGER.info("Calling rule service ");
        ResponseEntity<SearchModelWrapper> response = restTemplate.exchange(
                rulesServiceBaseUrl + "/executePre",
                HttpMethod.POST, request,
                SearchModelWrapper.class);
        SearchModelWrapper searchModelWrapperModified = response.getBody();
        LOGGER.info(searchModelWrapperModified.toString());
        return solrQuery;
    }

    @Override
    public SearchServiceResponse postProcessResult(SearchServiceRequest searchServiceRequest,
                                                   QueryResponse queryResponse,
                                                   SearchServiceResponse searchServiceResponse) {
        return searchServiceResponse;
    }
}
