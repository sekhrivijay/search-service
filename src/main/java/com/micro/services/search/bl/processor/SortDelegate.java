package com.micro.services.search.bl.processor;


import com.micro.services.search.api.request.ServiceRequest;
import com.micro.services.search.config.GlobalConstants;
import com.micro.services.search.api.response.ServiceResponse;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.response.QueryResponse;

import javax.inject.Named;

@Named("sortDelegate")
public class SortDelegate  extends BaseDelegate {
    private static Logger logger = Logger.getLogger(SortDelegate.class.getName());

    @Override
    public SolrQuery preProcessQuery(SolrQuery solrQuery, ServiceRequest serviceRequest) {
        if(StringUtils.isNotEmpty(serviceRequest.getSort())) {
            SolrQuery.ORDER order = SolrQuery.ORDER.asc;
            if(StringUtils.isNotEmpty(serviceRequest.getSortOrder()) && !serviceRequest.getSortOrder().equals(GlobalConstants.ASC)) {
                order = SolrQuery.ORDER.desc;
            }
            solrQuery.setSort(serviceRequest.getSort(), order);
        }

        return solrQuery;
    }

    @Override
    public ServiceResponse postProcessResult(ServiceRequest serviceRequest, QueryResponse queryResponse, ServiceResponse serviceResponse) {
        return serviceResponse;
    }
}
