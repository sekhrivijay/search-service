package com.ftd.service.search.bl.processor;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.response.QueryResponse;

import com.ftd.service.search.api.request.SearchServiceRequest;
import com.ftd.service.search.api.response.AutoCorrect;
import com.ftd.service.search.api.response.SearchServiceResponse;
import com.ftd.service.search.config.GlobalConstants;

import javax.inject.Named;
import java.util.Arrays;

@Named("fuzzySearchDelegate")
public class FuzzySearchDelegate extends BaseDelegate {
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
        AutoCorrect autoCorrect = new AutoCorrect();
        autoCorrect.setSearchedKey(searchServiceRequest.getQ());
        searchServiceResponse.setAutoCorrectList(Arrays.asList(autoCorrect));
        return searchServiceResponse;
    }


}