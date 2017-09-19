package com.micro.services.search.util;

import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrRequest;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class SolrUtil {

    private static Logger logger = Logger.getLogger(SolrUtil.class.getName());
    public static QueryResponse runSolrCommand(SolrClient solrClient, SolrQuery solrQuery) throws RuntimeException {
        QueryResponse queryResponse = null;
        try {
            //logger.info("Solr Query is  .." + solrQuery);
            long startTime = System.currentTimeMillis();
            queryResponse = solrClient.query(solrQuery, SolrRequest.METHOD.POST);
            long endTime = System.currentTimeMillis();
            logger.info("For query " + solrQuery + " Total Query Time " + (endTime - startTime) + " milli seconds");
        } catch (SolrServerException | IOException solrServerException) {
            logger.error("Could not execute solr query " + solrQuery);
            logger.error(solrServerException);
            throw new RuntimeException(solrServerException);
        }
        return queryResponse;
    }

    public static QueryResponse getFallback() {
        logger.error("Going to fallback. Empty query response ");
        return new QueryResponse();
    }

    public static QueryResponse getQueryResponse(Map<String, Future<QueryResponse>> futureMap, String key, long timeout) throws InterruptedException, ExecutionException, TimeoutException {
        QueryResponse queryResponse;
        try {
            queryResponse = futureMap.get(key).get(timeout, TimeUnit.MILLISECONDS);
        } catch (TimeoutException timeoutException) {
            futureMap.get(key).cancel(true);
            throw timeoutException;
        }
        return queryResponse;
    }
}
