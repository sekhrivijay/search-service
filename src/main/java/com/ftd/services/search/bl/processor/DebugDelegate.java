package com.ftd.services.search.bl.processor;

import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.response.QueryResponse;

import com.ftd.services.search.api.request.SearchServiceRequest;
import com.ftd.services.search.api.response.Debug;
import com.ftd.services.search.api.response.SearchServiceResponse;

import javax.inject.Named;
import java.util.ArrayList;
import java.util.List;

@Named("debugDelegate")
public class DebugDelegate  extends BaseDelegate {
    private static Logger logger = Logger.getLogger(DebugDelegate.class.getName());

    private SolrQuery solrQuery;

    @Override
    public SolrQuery preProcessQuery(SolrQuery solrQueryInput, SearchServiceRequest searchServiceRequest) {
        logger.info("Debug enabled");
        this.solrQuery = solrQueryInput;
        return solrQueryInput;
    }

    @Override
    public SearchServiceResponse postProcessResult(SearchServiceRequest searchServiceRequest,
                                                   QueryResponse queryResponse,
                                                    SearchServiceResponse searchServiceResponse) {
        Debug debug = searchServiceResponse.getDebug();
        if (debug == null) {
            debug = new Debug();
        }
        List<String> queries = debug.getQueries();
        if (queries == null) {
            queries = new ArrayList<>();
            debug.setQueries(queries);
        }
        queries.add(solrQuery.toString());
        debug.setRound(searchServiceRequest.getRound());
        debug.setSearchServiceRequest(searchServiceRequest);
        searchServiceResponse.setDebug(debug);
        return searchServiceResponse;
    }
}
