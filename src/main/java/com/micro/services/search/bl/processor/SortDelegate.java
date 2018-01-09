package com.micro.services.search.bl.processor;


import com.micro.services.search.api.request.SearchServiceRequest;
import com.micro.services.search.api.response.SearchServiceResponse;
import com.micro.services.search.api.response.SortDetail;
import com.micro.services.search.api.response.SortOrder;
import com.micro.services.search.api.response.SortTerm;
import com.micro.services.search.config.AppConfig;
import com.micro.services.search.config.GlobalConstants;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.springframework.beans.factory.annotation.Autowired;

import javax.inject.Named;
import java.util.ArrayList;
import java.util.List;

//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;

@Named("sortDelegate")
public class SortDelegate extends BaseDelegate {
    public static final String SORT_FIELD = GlobalConstants.AMPERSAND + GlobalConstants.SORT + GlobalConstants.EQUAL;
//    private static final Logger LOGGER = LoggerFactory.getLogger(SortDelegate.class);


    private AppConfig appConfig;

//
//    @Value("${service.searchEndpoint}")
//    private String searchEndpoint;


    @Autowired
    public void setAppConfig(AppConfig appConfig) {
        this.appConfig = appConfig;
    }

    @Override
    public SolrQuery preProcessQuery(SolrQuery solrQuery, SearchServiceRequest searchServiceRequest) {
//        if (StringUtils.isNotEmpty(searchServiceRequest.getSort())) {
//            SolrQuery.ORDER order = SolrQuery.ORDER.asc;
//            if (StringUtils.isNotEmpty(searchServiceRequest.getSortOrder())
//                    && !searchServiceRequest.getSortOrder().equals(GlobalConstants.ASC)) {
//                order = SolrQuery.ORDER.desc;
//            }
//            solrQuery.setSort(searchServiceRequest.getSort(), order);
//        }

        return solrQuery;
    }


    @Override
    public SearchServiceResponse postProcessResult(SearchServiceRequest searchServiceRequest,
                                                   QueryResponse queryResponse,
                                                   SearchServiceResponse searchServiceResponse) {
        List<String> sortList = appConfig.getSortList();
        if (sortList == null) {
            return searchServiceResponse;
        }
        List<SortTerm> sortTermList = new ArrayList<>();
        sortList.forEach(e -> sortTermList.add(buildSortTerm(e, searchServiceResponse)));
        searchServiceResponse.setSortTermList(sortTermList);
        return searchServiceResponse;
    }


    private SortTerm buildSortTerm(String sortTermString, SearchServiceResponse searchServiceResponse) {
        SortTerm sortTerm = new SortTerm();
        sortTerm.setSortBy(sortTermString);
        String urlPrefix = SORT_FIELD +
                sortTermString
                + " ";
        String ascUrl = urlPrefix + SortOrder.ASCENDING.getName();
        String descUrl = urlPrefix + SortOrder.DESCENDING.getName();

        if (!queryContains(searchServiceResponse, ascUrl)) {
            if (queryContains(searchServiceResponse, descUrl)) {
                sortTerm.setAscendingSortDetail(
                        buildSortDetail(searchServiceResponse.getOriginalQuery().replace(descUrl, ascUrl)));
            } else {
                sortTerm.setAscendingSortDetail(
                        buildSortDetail(searchServiceResponse.getOriginalQuery() + ascUrl));
            }
        } else {
            sortTerm.setAscendingSortDetail(buildSortDetail(searchServiceResponse.getOriginalQuery()));
        }

        if (!queryContains(searchServiceResponse, descUrl)) {
            if (queryContains(searchServiceResponse, ascUrl)) {
                sortTerm.setDescendingSortDetail(
                        buildSortDetail(searchServiceResponse.getOriginalQuery().replace(ascUrl, descUrl)));
            } else {
                sortTerm.setDescendingSortDetail(
                        buildSortDetail(searchServiceResponse.getOriginalQuery() + descUrl));
            }
        } else {
            sortTerm.setDescendingSortDetail(buildSortDetail(searchServiceResponse.getOriginalQuery()));
        }
        return sortTerm;
    }


    private SortDetail buildSortDetail(String url) {
        SortDetail sortDetail = new SortDetail();
        sortDetail.setUrl(
                //searchEndpoint +
                GlobalConstants.QUESTION_MARK +
                        url);
        return sortDetail;
    }
}
