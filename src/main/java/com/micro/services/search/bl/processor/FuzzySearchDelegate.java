package com.micro.services.search.bl.processor;

import com.micro.services.search.api.request.SearchServiceRequest;
import com.micro.services.search.api.response.SearchServiceResponse;
import com.micro.services.search.config.GlobalConstants;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.response.QueryResponse;

import javax.inject.Named;

@Named("fuzzySearchDelegate")
public class FuzzySearchDelegate  extends BaseDelegate {
//    private static final Logger LOGGER = LoggerFactory.getLogger(FuzzySearchDelegate.class);

    @Override
    public SolrQuery preProcessQuery(SolrQuery solrQuery, SearchServiceRequest searchServiceRequest) {
        if (searchServiceRequest.isFuzzyCompare()) {
            solrQuery.setQuery(searchServiceRequest.getQ() + GlobalConstants.TILDE);
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