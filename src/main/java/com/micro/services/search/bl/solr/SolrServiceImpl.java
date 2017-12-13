package com.micro.services.search.bl.solr;


import com.codahale.metrics.annotation.Timed;
import com.micro.services.search.api.request.RequestType;
import com.micro.services.search.config.GlobalConstants;
import com.micro.services.search.util.SolrUtil;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.command.AsyncResult;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.request.SolrPing;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.SolrPingResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.concurrent.Future;

@Named("solrService")
public class SolrServiceImpl implements SolrService {
    private static final Logger LOGGER = LoggerFactory.getLogger(SolrServiceImpl.class);
    private static final String SOLR_REQUEST = "generic-search.solr." + GlobalConstants.REQUEST;
    public static final QueryResponse FALLBACK_QUERY_RESPONSE = SolrUtil.getFallback();

    private SolrClient solrClient;
    private SolrUtil solrUtil;
    @Value("${service.collectionDestination}")
    private String collectionDestination;
    private SolrPing ping = new SolrPing();

    @Inject
    public void setSolrUtil(SolrUtil solrUtil) {
        this.solrUtil = solrUtil;
    }

    @Inject
    public void setSolrClient(SolrClient solrClient) {
        this.solrClient = solrClient;
    }

    public SolrServiceImpl() {
        ping.getParams().add(GlobalConstants.DISTRIB, GlobalConstants.TRUE);
        ping.getParams().add(GlobalConstants.QT, GlobalConstants.FORWARD_SLASH + RequestType.SEARCH.getName());
    }

    @Timed(absolute = true, name = SOLR_REQUEST)
    @HystrixCommand(groupKey = "hystrixGroup",
            commandKey = "solrCommandKey",
            threadPoolKey = "solrThreadPoolKey",
            fallbackMethod = "getFallback")
    public Future<QueryResponse> run(SolrQuery solrQuery) throws Exception {
        return
                new AsyncResult<QueryResponse>() {
                    @Override
                    public QueryResponse invoke() {
                        LOGGER.info("Solr Query is " + solrQuery.toQueryString());
                        return solrUtil.runSolrCommand(solrClient, solrQuery);
                    }
                };


    }

    public QueryResponse getFallback(SolrQuery solrQuery) {
        return FALLBACK_QUERY_RESPONSE;
    }


    public int ping() throws Exception {
        SolrPingResponse solrPingResponse = ping.process(solrClient, collectionDestination);
        return solrPingResponse.getStatus();
    }


}
