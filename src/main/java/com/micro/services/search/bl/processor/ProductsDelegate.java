package com.micro.services.search.bl.processor;


import com.micro.services.search.api.request.SearchServiceRequest;
import com.micro.services.search.api.response.Document;
import com.micro.services.search.api.response.SearchServiceResponse;
import com.micro.services.search.config.GlobalConstants;
import com.micro.services.search.util.SolrDocumentUtil;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;

import javax.inject.Named;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Named("productsDelegate")
public class ProductsDelegate extends BaseDelegate {
    public static final String ID_FIELD = GlobalConstants.AMPERSAND +
            GlobalConstants.FQ +
            GlobalConstants.EQUAL +
            GlobalConstants.ID +
            GlobalConstants.COLON;

    @Override
    public SolrQuery preProcessQuery(SolrQuery solrQuery, SearchServiceRequest searchServiceRequest) {
        return solrQuery;
    }

    @Override
    public SearchServiceResponse postProcessResult(SearchServiceRequest searchServiceRequest,
                                                   QueryResponse queryResponse,
                                                   SearchServiceResponse searchServiceResponse) {
        if (queryResponse.getResults() == null) {
            return searchServiceResponse;
        }
//        List<Map<String, String>> documents = buildProducts(queryResponse.getResults());
        List<Document> documents = buildProducts(searchServiceRequest, queryResponse.getResults());
        if (documents.size() > 0) {
//            searchServiceResponse.setDocuments(documents);
            searchServiceResponse.setDocumentList(documents);
        }
        return searchServiceResponse;
    }


//    private List<Map<String, String>> buildProducts(SolrDocumentList solrDocuments) {
    private List<Document> buildProducts(SearchServiceRequest searchServiceRequest, SolrDocumentList solrDocuments) {
//        List<Map<String, String>> products = new ArrayList<>();
        List<Document> documentList = new ArrayList<>();
        for (SolrDocument solrDocument : solrDocuments) {
            Collection<String> fieldNames = solrDocument.getFieldNames();
            Map<String, String> record  = new HashMap<>();
            for (String key: fieldNames) {
                record.put(key, SolrDocumentUtil.getFieldValue(solrDocument, key));
            }
            Document document = new Document();
            document.setRecord(record);
            document.setUrl(buildOriginalQuery(searchServiceRequest) + ID_FIELD +  SolrDocumentUtil.getFieldValue(solrDocument, GlobalConstants.ID));
            documentList.add(document);

//            products.add(record);
        }
//        return products;
        return documentList;
    }

}
