package com.micro.services.search.bl.processor;


import com.micro.services.search.api.request.RequestType;
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


    public static final String PDP_QUERY_SUFFIX = GlobalConstants.Q_STAR_FIELD
            + GlobalConstants.TYPE_PREFIX
            + RequestType.PDP.getName();

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
        searchServiceResponse.setOriginalQuery(buildOriginalQuery(searchServiceRequest));
        List<Document> documents = buildProducts(searchServiceRequest, searchServiceResponse, queryResponse.getResults());
        if (documents.size() > 0) {
            searchServiceResponse.setDocumentList(documents);
        }
        return searchServiceResponse;
    }


    private List<Document> buildProducts(SearchServiceRequest searchServiceRequest, SearchServiceResponse searchServiceResponse, SolrDocumentList solrDocuments) {
        List<Document> documentList = new ArrayList<>();
        for (SolrDocument solrDocument : solrDocuments) {
            Collection<String> fieldNames = solrDocument.getFieldNames();
            Map<String, String> record  = new HashMap<>();
            for (String key: fieldNames) {
                record.put(key, SolrDocumentUtil.getFieldValue(solrDocument, key));
            }
            Document document = new Document();
            document.setRecord(record);
            if(searchServiceRequest.getRequestType() != RequestType.AUTOFILL) {
                setUrl(searchServiceResponse, solrDocument, document);
            }
            documentList.add(document);

        }
        return documentList;
    }

    private void setUrl(SearchServiceResponse searchServiceResponse, SolrDocument solrDocument, Document document) {
//        document.setUrl(getQuery(searchServiceResponse, getQuery(searchServiceResponse, solrDocument)));
        document.setUrl(getQuery(searchServiceResponse, solrDocument));
    }

    private String getQuery(SearchServiceResponse searchServiceResponse, SolrDocument solrDocument) {
        return GlobalConstants.ID_FIELD_FILTER
                + SolrDocumentUtil.getFieldValue(solrDocument, GlobalConstants.ID)
                + PDP_QUERY_SUFFIX;
    }

}
