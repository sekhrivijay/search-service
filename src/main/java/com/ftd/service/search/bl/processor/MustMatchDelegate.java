package com.ftd.service.search.bl.processor;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ftd.service.search.api.request.SearchServiceRequest;
import com.ftd.service.search.api.response.SearchServiceResponse;
import com.ftd.service.search.config.GlobalConstants;

import javax.inject.Named;

@Named("mustMatchDelegate")
public class MustMatchDelegate extends BaseDelegate {

    private static final Logger LOGGER = LoggerFactory.getLogger(MustMatchDelegate.class);

    @Override
    public SolrQuery preProcessQuery(SolrQuery solrQuery, SearchServiceRequest searchServiceRequest) {
        if (!searchServiceRequest.isMustMatchFiftyPercent() && !searchServiceRequest.isMustMatchSeventyFivePercent()) {
            return solrQuery;
        }
        String mustMatchPercent = GlobalConstants.MUST_MATCH_HUNDRED_PERCENT;
        if (searchServiceRequest.isMustMatchSeventyFivePercent()) {
            mustMatchPercent = GlobalConstants.RELAXED_MUST_MATCH_SEVENTY_FIVE_PERCENT;
        }
        if (searchServiceRequest.isMustMatchFiftyPercent()) {
            mustMatchPercent = GlobalConstants.RELAXED_MUST_MATCH_FIFTY_PERCENT;
        }

        LOGGER.info("Must Match results enabled for " + mustMatchPercent);
        solrQuery.setParam(GlobalConstants.MM, mustMatchPercent);
        return solrQuery;
    }

    @Override
    public SearchServiceResponse postProcessResult(SearchServiceRequest searchServiceRequest,
                                                   QueryResponse queryResponse,
                                                   SearchServiceResponse searchServiceResponse) {
        return searchServiceResponse;
    }
}
