package com.ftd.service.search.bl.processor;


import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.response.Group;
import org.apache.solr.client.solrj.response.GroupCommand;
import org.apache.solr.client.solrj.response.GroupResponse;
import org.apache.solr.client.solrj.response.QueryResponse;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import com.ftd.service.search.api.request.SearchServiceRequest;
import com.ftd.service.search.api.response.Facet;
import com.ftd.service.search.api.response.ResponseGroup;
import com.ftd.service.search.api.response.SearchServiceResponse;

import javax.inject.Named;

import static com.ftd.service.search.config.GlobalConstants.GROUP;
import static com.ftd.service.search.config.GlobalConstants.GROUP_FIELD;
import static com.ftd.service.search.config.GlobalConstants.GROUP_LIMIT;

import java.util.ArrayList;
import java.util.List;

@Named("groupDelegate")
public class GroupDelegate extends BaseDelegate {

//    private static final Logger LOGGER = LoggerFactory.getLogger(GroupDelegate.class);

    @Value("${service.groupLimitValue}")
    private int groupLimitValue;

    @Override
    public SolrQuery preProcessQuery(SolrQuery solrQuery, SearchServiceRequest searchServiceRequest) {
        String[] groupFields = searchServiceRequest.getGroupFields();
        if (groupFields != null && groupFields.length > 0) {
            solrQuery.set(GROUP, true);
            solrQuery.set(GROUP_LIMIT, groupLimitValue);
            for (String groupField : groupFields) {
                solrQuery.add(GROUP_FIELD, groupField);
            }
        }
        return solrQuery;
    }

    @Override
    public SearchServiceResponse postProcessResult(SearchServiceRequest searchServiceRequest,
                                                   QueryResponse queryResponse,
                                                   SearchServiceResponse searchServiceResponse) {
        GroupResponse groupResponse = queryResponse.getGroupResponse();
        if (groupResponse != null) {

            List<ResponseGroup> responseGroups = new ArrayList<>();
            List<GroupCommand> groupCommands = groupResponse.getValues();
            for (GroupCommand groupCommand : groupCommands) {
                ResponseGroup responseGroup = new ResponseGroup();
                responseGroup.setGroupName(groupCommand.getName());
                List<Group> groups = groupCommand.getValues();
                List<Facet> facets = new ArrayList<>();
                for (Group group : groups) {
                    Facet facet = new Facet();
                    facet.setFacetName(group.getGroupValue());
//                        facet.setFacetCount();
                    //facet.setDocuments(buildProducts(group.getResult()));
                    facets.add(facet);
                }
                responseGroup.setFacets(facets);
                responseGroups.add(responseGroup);
            }
//                    responseGroup.setGroupName(facetField.getName());
//                    List<Facet> facets = new ArrayList<>();
            searchServiceResponse.setResponseGroups(responseGroups);

        }
//                group.setFacets(facets);
//                responseGroups.add(group);
        return searchServiceResponse;
    }


}

