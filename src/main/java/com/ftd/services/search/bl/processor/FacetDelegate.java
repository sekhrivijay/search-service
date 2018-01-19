package com.ftd.services.search.bl.processor;


import com.ftd.services.search.config.AppConfigProperties;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.response.FacetField;
import org.apache.solr.client.solrj.response.QueryResponse;

import com.ftd.services.search.api.request.SearchServiceRequest;
import com.ftd.services.search.api.response.Facet;
import com.ftd.services.search.api.response.FacetGroup;
import com.ftd.services.search.api.response.SearchServiceResponse;
import com.ftd.services.search.config.GlobalConstants;
import org.springframework.beans.factory.annotation.Autowired;

import javax.inject.Named;
import java.util.ArrayList;
import java.util.List;

//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;

@Named("facetDelegate")
public class FacetDelegate extends BaseDelegate {
//    private static final Logger LOGGER = LoggerFactory.getLogger(FacetDelegate.class);

//    @Value("${service.searchEndpoint}")
//    private String searchEndpoint;


    private AppConfigProperties appConfigProperties;

    @Autowired
    public void setAppConfigProperties(AppConfigProperties appConfigProperties) {
        this.appConfigProperties = appConfigProperties;
    }

    @Override
    public SolrQuery preProcessQuery(SolrQuery solrQuery, SearchServiceRequest searchServiceRequest) {
        String[] facetFields = searchServiceRequest.getFacetFields();
        List<String> facetList = appConfigProperties.getFacetList();
        if ((facetFields != null && facetFields.length > 0)
                || (facetList != null && facetList.size() > 0)) {
            solrQuery.setFacet(true);
            solrQuery.setFacetMinCount(1);
            solrQuery.setFacetSort(searchServiceRequest.getFacetSort());
        }

        if (facetFields != null && facetFields.length > 0) {
            solrQuery.addFacetField(facetFields);
        } else if (appConfigProperties.getFacetList() != null) {
            appConfigProperties
                    .getFacetList()
                    .forEach(solrQuery::addFacetField);
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
                    setUrl(searchServiceResponse, facetField, count, facet);
                    facets.add(facet);
                }
                facetGroup.setFacets(facets);
                facetGroups.add(facetGroup);
            }
            searchServiceResponse.setFacetGroups(facetGroups);
        }


        return searchServiceResponse;
    }

    private void setUrl(SearchServiceResponse searchServiceResponse,
                        FacetField facetField,
                        FacetField.Count count,
                        Facet facet) {
        facet.setUrl(getQuery(searchServiceResponse,
                getQuery(facetField, count),
//                searchEndpoint +
                GlobalConstants.QUESTION_MARK));
    }

    private String getQuery(FacetField facetField, FacetField.Count count) {
        return GlobalConstants.FQ_PREFIX +
                facetField.getName() +
                GlobalConstants.COLON +
                count.getName();
    }
}

