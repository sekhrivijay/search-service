package com.ftd.services.search.bl.processor;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.response.QueryResponse;

import com.ftd.services.search.api.request.SearchServiceRequest;
import com.ftd.services.search.api.response.SearchServiceResponse;
import com.ftd.services.search.config.GlobalConstants;

import javax.inject.Named;

@Named("siteDelegate")
public class SiteDelegate extends BaseDelegate {
//    private static final Logger LOGGER = LoggerFactory.getLogger(SiteDelegate.class);

    @Override
    public SolrQuery preProcessQuery(SolrQuery solrQuery, SearchServiceRequest searchServiceRequest) {
        if (searchServiceRequest.getSiteId() != null) {
            solrQuery.addFilterQuery(
                    GlobalConstants.SITE_ID
                            + GlobalConstants.COLON
                            + searchServiceRequest.getSiteId());
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
