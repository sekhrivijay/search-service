package com.micro.services.search.bl.processor;


import com.micro.services.search.api.request.ServiceRequest;
import com.micro.services.search.api.response.Facet;
import com.micro.services.search.api.response.ResponseGroup;
import com.micro.services.search.api.response.ServiceResponse;
import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.response.Group;
import org.apache.solr.client.solrj.response.GroupCommand;
import org.apache.solr.client.solrj.response.GroupResponse;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.springframework.beans.factory.annotation.Value;

import javax.inject.Named;
import java.util.ArrayList;
import java.util.List;

import static com.micro.services.search.config.GlobalConstants.*;

@Named("groupDelegate")
public class GroupDelegate extends BaseDelegate {

    private static Logger logger = Logger.getLogger(SortDelegate.class.getName());

    @Value("${service.groupLimitValue}")
    private int groupLimitValue;

    @Override
    public SolrQuery preProcessQuery(SolrQuery solrQuery, ServiceRequest serviceRequest) {
        String[] groupFields = serviceRequest.getGroupFields();
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
    public ServiceResponse postProcessResult(ServiceRequest serviceRequest, QueryResponse queryResponse, ServiceResponse serviceResponse) {
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
            serviceResponse.setResponseGroups(responseGroups);

        }
//                group.setFacets(facets);
//                responseGroups.add(group);
        return serviceResponse;
    }


}

