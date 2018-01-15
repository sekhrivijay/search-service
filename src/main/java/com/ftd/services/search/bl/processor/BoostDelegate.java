package com.ftd.services.search.bl.processor;

import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.response.QueryResponse;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.ftd.services.search.api.request.SearchServiceRequest;
import com.ftd.services.search.api.response.SearchServiceResponse;
import com.ftd.services.search.config.AppConfig;
import com.ftd.services.search.config.GlobalConstants;

import javax.inject.Named;
import java.util.Map;

@Named("boostDelegate")
public class BoostDelegate extends BaseDelegate {
//    private static final Logger LOGGER = LoggerFactory.getLogger(BoostDelegate.class);

    private AppConfig appConfig;

    @Autowired
    public void setAppConfig(AppConfig appConfig) {
        this.appConfig = appConfig;
    }

    @Override
    public SolrQuery preProcessQuery(SolrQuery solrQuery, SearchServiceRequest searchServiceRequest) {
        String bf = StringUtils.EMPTY;
        if (StringUtils.isNotEmpty(searchServiceRequest.getBf())) {
            bf = searchServiceRequest.getBf();
        } else {
            Map<String, String> bfSiteMap =  appConfig.getSitesBfMap();
            String siteId = StringUtils.defaultString(searchServiceRequest.getSiteId(), GlobalConstants.DEFAULT);
            if (bfSiteMap != null && bfSiteMap.get(siteId) != null) {
                bf = bfSiteMap.get(siteId);
            }
        }
        solrQuery.setParam(GlobalConstants.BF, bf);
        return solrQuery;
    }

    @Override
    public SearchServiceResponse postProcessResult(SearchServiceRequest searchServiceRequest,
                                                   QueryResponse queryResponse,
                                                   SearchServiceResponse searchServiceResponse) {
        return searchServiceResponse;
    }
}
