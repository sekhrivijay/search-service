package com.micro.services.search.util;


import com.micro.services.search.config.GlobalConstants;
import org.apache.solr.common.SolrDocument;

import java.util.ArrayList;
import java.util.List;

public class SolrDocumentUtil {

    public static String getFieldValue(SolrDocument solrDocument, String field) {
        Object object = solrDocument.getFieldValue(field);
        if(object == null) {
            return null;
        }
        return String.valueOf(object);
    }

    public static List<String> getFieldValues(SolrDocument solrDocument, String field) {
        if(solrDocument == null) {
            return null;
        }
        Object o = solrDocument.getFieldValues(field);
        if(o == null) {
            return null;
        }
        List<String> toReturn = new ArrayList<>();
        for(Object object: solrDocument.getFieldValues(field)) {
            if(object != null) {
                toReturn.add(String.valueOf(object));
            }
        }
        return toReturn;
    }

    public static String getId(SolrDocument solrDocument) {
        return getFieldValue(solrDocument, GlobalConstants.ID);
    }
}
