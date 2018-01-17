package com.ftd.services.search.bl.clients.solr;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.SolrInputDocument;

import java.util.List;
import java.util.concurrent.Future;

public interface EnhancedSolrClient {
    void updateJson(String jsonData);

    void updateDocs(SupplierWithException<UpdateResponse> solrProcess);

    void updateDocs(List<SolrInputDocument> solrInputDocumentList);

    Future<QueryResponse> runInThread(SolrQuery solrQuery) throws Exception;

    QueryResponse run(SolrQuery solrQuery) throws Exception;

    UpdateResponse deleteById(String id);

    int ping() throws Exception;
}
