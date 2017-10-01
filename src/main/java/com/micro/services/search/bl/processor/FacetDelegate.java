package com.micro.services.search.bl.processor;


import com.micro.services.search.api.request.SearchServiceRequest;
import com.micro.services.search.api.response.Facet;
import com.micro.services.search.api.response.FacetGroup;
import com.micro.services.search.api.response.SearchServiceResponse;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.response.FacetField;
import org.apache.solr.client.solrj.response.QueryResponse;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;

import javax.inject.Named;
import java.util.ArrayList;
import java.util.List;

@Named("facetDelegate")
public class FacetDelegate implements Delegate {
//    private static final Logger LOGGER = LoggerFactory.getLogger(FacetDelegate.class);

    @Override
    public SolrQuery preProcessQuery(SolrQuery solrQuery, SearchServiceRequest searchServiceRequest) {
        String[] facetFields = searchServiceRequest.getFacetFields();
        if (facetFields != null && facetFields.length > 0) {
            solrQuery.setFacet(true);
            solrQuery.setFacetMinCount(1);
            solrQuery.addFacetField(facetFields);
            solrQuery.setFacetSort(searchServiceRequest.getFacetSort());
        }
        return solrQuery;
    }

    @Override
    public SearchServiceResponse postProcessResult(SearchServiceRequest searchServiceRequest,
                                                   QueryResponse queryResponse,
                                                   SearchServiceResponse searchServiceResponse) {
        List<FacetField> facetFieldList = queryResponse.getFacetFields();
        if (facetFieldList != null) {
            List<FacetGroup> facetGroups = new ArrayList<>();
            for (FacetField facetField : facetFieldList) {
                FacetGroup facetGroup = new FacetGroup();
                facetGroup.setGroupName(facetField.getName());
                List<Facet> facets = new ArrayList<>();
                for (FacetField.Count count : facetField.getValues()) {
                    Facet facet = new Facet();
                    facet.setFacetName(count.getName());
                    facet.setFacetCount(count.getCount());
                    facets.add(facet);
                }
                facetGroup.setFacets(facets);
                facetGroups.add(facetGroup);
            }
            searchServiceResponse.setFacetGroups(facetGroups);
        }


        return searchServiceResponse;
    }
}

