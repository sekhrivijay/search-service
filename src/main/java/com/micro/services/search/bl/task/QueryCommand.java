package com.micro.services.search.bl.task;


import com.codahale.metrics.annotation.Timed;
import com.micro.services.search.config.GlobalConstants;
import com.micro.services.search.util.SolrUtil;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.command.AsyncResult;
import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.response.QueryResponse;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.concurrent.Future;

@Named
public class QueryCommand {
    private static Logger logger = Logger.getLogger(QueryCommand.class.getName());
    private final String SOLR_REQUEST = "generic-search.solr." + GlobalConstants.REQUEST;
    public static final QueryResponse FALLBACK_QUERY_RESPONSE = SolrUtil.getFallback();

    @Inject
    private SolrClient solrClient;

    public QueryCommand() {
    }

    @Timed(absolute = true, name = SOLR_REQUEST)
    @HystrixCommand(groupKey = "hystrixGroup", commandKey = "solrCommandKey", threadPoolKey = "solrThreadPoolKey", fallbackMethod = "getFallback")
    public Future<QueryResponse> run(SolrQuery solrQuery) throws Exception {
        return
                new AsyncResult<QueryResponse>() {
                    @Override
                    public QueryResponse invoke() {
                        return SolrUtil.runSolrCommand(solrClient, solrQuery);
                    }
                };


    }

    public QueryResponse getFallback(SolrQuery solrQuery) {
        return FALLBACK_QUERY_RESPONSE;
    }

}
