package com.ftd.services.search.bl.processor;


import com.ftd.services.search.api.request.SearchServiceRequest;
import com.ftd.services.search.api.response.SearchServiceResponse;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.response.QueryResponse;

import javax.inject.Named;

@Named("priceDelegate")
public class PriceDelegate extends BaseDelegate implements Delegate {
    @Override
    public SolrQuery preProcessQuery(SolrQuery solrQuery, SearchServiceRequest searchServiceRequest) {
        solrQuery.setFacet(true);
        solrQuery.setFacetMinCount(1);
        solrQuery.addFacetField("finalPrice");
        solrQuery.add("f.finalPrice.facet.range.start", "0.0");
        solrQuery.add("f.finalPrice.facet.range.end", "500.0");
        solrQuery.add("f.finalPrice.facet.range.gap", "25.0");
        return solrQuery;
    }

    @Override
    public SearchServiceResponse postProcessResult(
            SearchServiceRequest searchServiceRequest,
            QueryResponse queryResponse,
            SearchServiceResponse searchServiceResponse) {
        return searchServiceResponse;
    }
}
