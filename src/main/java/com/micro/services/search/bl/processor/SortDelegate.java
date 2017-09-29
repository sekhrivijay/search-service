package com.micro.services.search.bl.processor;


import com.micro.services.search.api.request.SearchServiceRequest;
import com.micro.services.search.api.response.SearchServiceResponse;
import com.micro.services.search.config.GlobalConstants;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.response.QueryResponse;

import javax.inject.Named;

@Named("sortDelegate")
public class SortDelegate  extends BaseDelegate {
    private static Logger logger = Logger.getLogger(SortDelegate.class.getName());

    @Override
    public SolrQuery preProcessQuery(SolrQuery solrQuery, SearchServiceRequest searchServiceRequest) {
        if(StringUtils.isNotEmpty(searchServiceRequest.getSort())) {
            SolrQuery.ORDER order = SolrQuery.ORDER.asc;
            if(StringUtils.isNotEmpty(searchServiceRequest.getSortOrder()) && !searchServiceRequest.getSortOrder().equals(GlobalConstants.ASC)) {
                order = SolrQuery.ORDER.desc;
            }
            solrQuery.setSort(searchServiceRequest.getSort(), order);
        }

        return solrQuery;
    }

    @Override
    public SearchServiceResponse postProcessResult(SearchServiceRequest searchServiceRequest, QueryResponse queryResponse, SearchServiceResponse searchServiceResponse) {
        return searchServiceResponse;
    }
}
