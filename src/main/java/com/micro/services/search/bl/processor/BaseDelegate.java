package com.micro.services.search.bl.processor;

import com.micro.services.search.api.request.SearchServiceRequest;
import com.micro.services.search.config.GlobalConstants;
import com.micro.services.search.util.SolrDocumentUtil;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;

import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public abstract class BaseDelegate implements Delegate {

    protected String buildOriginalQuery(SearchServiceRequest searchServiceRequest) {
        StringBuilder stringBuilder = new StringBuilder();
        for (String paramName : searchServiceRequest.getParametersOriginal().keySet()) {
            for(String value : searchServiceRequest.getParametersOriginal().get(paramName) ) {
                stringBuilder
                        .append(GlobalConstants.AMPERSAND)
                        .append(paramName)
                        .append(GlobalConstants.EQUAL)
                        .append(value);
            }
        }
        return stringBuilder.toString();
    }

}
