package com.ftd.services.search.bl.processor;


import com.ftd.services.search.api.request.SearchServiceRequest;
import com.ftd.services.search.api.response.Facet;
import com.ftd.services.search.api.response.FacetGroup;
import com.ftd.services.search.api.response.SearchServiceResponse;
import com.ftd.services.search.bl.clients.MiscUtil;
import com.ftd.services.search.config.AppConfigProperties;
import com.ftd.services.search.config.GlobalConstants;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.RangeFacet;
import org.springframework.beans.factory.annotation.Autowired;

import javax.inject.Named;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Named("rangeFacetDelegate")
public class RangeFacetDelegate extends BaseDelegate implements Delegate {


    public static final int END = 500;
    public static final int START = 0;
    public static final int GAP = 25;
    private AppConfigProperties appConfigProperties;

    @Autowired
    public void setAppConfigProperties(AppConfigProperties appConfigProperties) {
        this.appConfigProperties = appConfigProperties;
    }

    @Override
    public SolrQuery preProcessQuery(SolrQuery solrQuery, SearchServiceRequest searchServiceRequest) {
        String[] rangeFacetFieldsInput = searchServiceRequest.getRangeFacetFields();
        List<String> rangeFacetListConfigured = appConfigProperties.getRangeFacetList();

        if (MiscUtil.isNotEmpty(rangeFacetFieldsInput)) {
            Arrays.asList(rangeFacetFieldsInput)
                    .forEach(e -> addRangeFacet(solrQuery, e));
        } else if (MiscUtil.isNotEmpty(rangeFacetListConfigured)) {
            rangeFacetListConfigured.forEach(e -> addRangeFacet(solrQuery, e));
        }

        return solrQuery;
    }

    private void addRangeFacet(SolrQuery solrQuery, String field) {
        solrQuery.setFacet(true);
        solrQuery.setFacetMinCount(1);
//        solrQuery.addNumericRangeFacet("finalPrice", 0, 500, 25);
        solrQuery.addNumericRangeFacet(field, START, END, GAP);
    }

    @Override
    public SearchServiceResponse postProcessResult(
            SearchServiceRequest searchServiceRequest,
            QueryResponse queryResponse,
            SearchServiceResponse searchServiceResponse) {


        List<RangeFacet> rangeFacetList = queryResponse.getFacetRanges();

        if (rangeFacetList == null || rangeFacetList.size() == 0) {
            return searchServiceResponse;
        }


        List<FacetGroup> facetGroups = searchServiceResponse.getFacetGroups();
        if (facetGroups == null) {
            facetGroups = new ArrayList<>();
            searchServiceResponse.setFacetGroups(facetGroups);
        }

        for (RangeFacet tmpRangeFacet : rangeFacetList) {
            RangeFacet.Numeric rangeFacet = null;
            if (tmpRangeFacet instanceof RangeFacet.Numeric) {
                rangeFacet = (RangeFacet.Numeric) tmpRangeFacet;
            }
            if (rangeFacet == null) {
                continue;
            }

            FacetGroup facetGroup = new FacetGroup();
            facetGroup.setGroupName(rangeFacet.getName());
            List<Facet> facets = new ArrayList<>();
            List<RangeFacet.Count> countList = rangeFacet.getCounts();
            int index = 1;
            for (RangeFacet.Count count : countList) {
                Facet facet = new Facet();
                facet.setFacetName(count.getValue());
                facet.setFacetName(buildFacetName(rangeFacet, index));
                facet.setFacetCount(count.getCount());
                setUrl(searchServiceResponse, rangeFacet, index, facet);
                facets.add(facet);
                index++;
            }
            facetGroup.setFacets(facets);
            facetGroups.add(facetGroup);
        }

        return searchServiceResponse;
    }

    private void setUrl(SearchServiceResponse searchServiceResponse,
                        RangeFacet.Numeric rangeFacet,
                        int index,
                        Facet facet) {
        facet.setUrl(getQuery(searchServiceResponse,
                getQuery(rangeFacet, index),
//                searchEndpoint +
                GlobalConstants.QUESTION_MARK));
    }

    private String getQuery(RangeFacet.Numeric rangeFacet, int index) {
//        Number start = rangeFacet.getStart();
//        Number end = rangeFacet.getEnd();
//        Number gap = rangeFacet.getGap();
//        long previous = index * gap.longValue();
//        long next = index * gap.longValue() + gap.longValue();


        return GlobalConstants.FQ_PREFIX +
                rangeFacet.getName() +
                GlobalConstants.COLON +
                GlobalConstants.RIGHT_BRACKET +
                buildFacetName(rangeFacet, index) +
                GlobalConstants.LEFT_BRACKET;
    }

    private String buildFacetName(RangeFacet.Numeric rangeFacet, int index) {
        Number gap = rangeFacet.getGap();
        long previous = index * gap.longValue();
        long next = index * gap.longValue() + gap.longValue();
        return previous +
                GlobalConstants.SPACE +
                GlobalConstants.TO +
                GlobalConstants.SPACE +
                next;
    }

}
