package com.ftd.services.search.bl.processor;

import com.ftd.services.search.api.request.RequestType;
import com.ftd.services.search.api.request.SearchServiceRequest;
import com.ftd.services.search.api.response.SearchServiceResponse;
import com.ftd.services.search.config.GlobalConstants;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.response.QueryResponse;

import javax.inject.Named;

import static com.ftd.services.search.config.GlobalConstants.TYPE_COLON;

@Named("typeDelegate")
public class TypeDelegate extends BaseDelegate {
//    private static final Logger LOGGER = LoggerFactory.getLogger(TypeDelegate.class);

    @Override
    public SolrQuery preProcessQuery(SolrQuery solrQuery, SearchServiceRequest searchServiceRequest) {
        String type = GlobalConstants.DEFAULT;
        if (searchServiceRequest.getRequestType() == RequestType.AUTOFILL) {
            type = GlobalConstants.AUTOFILL;
        }
        solrQuery.addFilterQuery(TYPE_COLON + type);
        return solrQuery;
    }

    @Override
    public SearchServiceResponse postProcessResult(SearchServiceRequest searchServiceRequest,
                                                   QueryResponse queryResponse,
                                                   SearchServiceResponse searchServiceResponse) {

        return searchServiceResponse;
    }
}
