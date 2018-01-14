package com.ftd.service.search.bl.solr;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.response.QueryResponse;

import java.util.concurrent.Future;

public interface SolrService {
    Future<QueryResponse> run(SolrQuery solrQuery) throws Exception;
    int ping() throws Exception;
}