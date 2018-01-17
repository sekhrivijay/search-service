package com.ftd.services.search.bl.processor;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ftd.services.search.bl.clients.rules.RuleServiceResponse;
import com.ftd.services.search.api.request.Holder;
import com.ftd.services.search.api.request.SearchServiceRequest;
import com.ftd.services.search.api.response.SearchServiceResponse;
import com.ftd.services.search.bl.clients.rules.RuleClient;
import com.ftd.services.search.bl.clients.rules.RuleClientImpl;

import javax.inject.Inject;
import javax.inject.Named;
import java.lang.reflect.InvocationTargetException;

@Named("rulesDelegate")
public class RulesDelegate extends BaseDelegate {
    private static final Logger LOGGER = LoggerFactory.getLogger(RulesDelegate.class);


    private RuleClient ruleClient;

    public static final Holder FALLBACK_RULE_RESPONSE = new Holder();

    public RulesDelegate() {
        FALLBACK_RULE_RESPONSE.setCacheable(false);
    }

    @Inject
    @Named("ruleService")
    public void setRuleClient(RuleClient ruleClient) {
        this.ruleClient = ruleClient;
    }

    @Override
    public SolrQuery preProcessQuery(SolrQuery solrQuery, SearchServiceRequest searchServiceRequest) {
        RuleServiceResponse ruleServiceResponseModified = ruleClient.callSearchRulesService(
                searchServiceRequest, new SearchServiceResponse());
        if (ruleServiceResponseModified == RuleClientImpl.FALLBACK_RULE_RESPONSE) {
            searchServiceRequest.setHolder(FALLBACK_RULE_RESPONSE);
            return solrQuery;
        }
        if (ruleServiceResponseModified != null) {
            LOGGER.info("Response from Rule service " + ruleServiceResponseModified.toString());
            Holder holder = new Holder();
            if (ruleServiceResponseModified.getSearchServiceResponse() != null
                    && ruleServiceResponseModified.getSearchServiceResponse().getRedirect() != null) {
                holder.setRedirect(ruleServiceResponseModified.getSearchServiceResponse().getRedirect());
                holder.setRedirect(true);

            }
            if (ruleServiceResponseModified.getSearchServiceRequest() != null) {
                try {
                    BeanUtils.copyProperties(
                            searchServiceRequest,
                            ruleServiceResponseModified.getSearchServiceRequest());
                } catch (IllegalAccessException | InvocationTargetException e) {
                    LOGGER.error("Error copying object state from rule service ", e);
                }
            }
            searchServiceRequest.setHolder(holder);
        }
        return solrQuery;
    }


    @Override
    public SearchServiceResponse postProcessResult(SearchServiceRequest searchServiceRequest,
                                                   QueryResponse queryResponse,
                                                   SearchServiceResponse searchServiceResponse) {
        return searchServiceResponse;
    }
}
