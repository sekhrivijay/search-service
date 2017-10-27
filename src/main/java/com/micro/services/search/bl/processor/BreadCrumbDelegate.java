package com.micro.services.search.bl.processor;

import com.micro.services.search.api.request.SearchServiceRequest;
import com.micro.services.search.api.response.BreadCrumbElement;
import com.micro.services.search.api.response.BreadCrumbTrail;
import com.micro.services.search.api.response.Facet;
import com.micro.services.search.api.response.FacetGroup;
import com.micro.services.search.api.response.SearchServiceResponse;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.springframework.beans.factory.annotation.Value;

import javax.inject.Named;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Named("breadCrumbDelegate")
public class BreadCrumbDelegate extends BaseDelegate {


    @Value("${service.breadCrumbList}")
    private List<String> breadCrumbs;

    @Override
    public SolrQuery preProcessQuery(SolrQuery solrQuery, SearchServiceRequest searchServiceRequest) {
        return solrQuery;
    }

    @Override
    public SearchServiceResponse postProcessResult(SearchServiceRequest searchServiceRequest,
                                                   QueryResponse queryResponse,
                                                   SearchServiceResponse searchServiceResponse) {


        List<BreadCrumbTrail> breadCrumbTrailList = new ArrayList<>();
        for(String breadCrumbName: breadCrumbs) {
            BreadCrumbTrail breadCrumbTrail = getBreadCrumbTrail(breadCrumbName, searchServiceResponse);
            breadCrumbTrailList.add(breadCrumbTrail);
        }

        searchServiceResponse.setBreadCrumbTrail(breadCrumbTrailList);
        return searchServiceResponse;
    }

    private BreadCrumbTrail getBreadCrumbTrail(String breadCrumbName, SearchServiceResponse searchServiceResponse) {
        BreadCrumbTrail breadCrumbTrail = new BreadCrumbTrail();
        List<BreadCrumbElement> breadCrumbElementList = getBreadCrumbElements(breadCrumbName, searchServiceResponse);
        breadCrumbTrail.setBreadCrumbs(breadCrumbElementList);
        return breadCrumbTrail;
    }

    private List<BreadCrumbElement> getBreadCrumbElements(String breadCrumbName, SearchServiceResponse searchServiceResponse) {
        List<BreadCrumbElement> breadCrumbElementList = new ArrayList<>();
//        BreadCrumbElement breadCrumbElement = getBreadCrumbElement(breadCrumbName, searchServiceResponse);

        List<FacetGroup> facetGroupList = searchServiceResponse.getFacetGroups();
        if(facetGroupList == null) {
            return breadCrumbElementList;
        }
        Optional<FacetGroup> facetGroup = facetGroupList.stream()
                .filter(e -> e.getGroupName().equals(breadCrumbName))
                .findFirst();
        if(facetGroup.isPresent()) {
            List<Facet> facetList = facetGroup.get().getFacets();
            for(Facet facet: facetList) {
                BreadCrumbElement breadCrumbElement = new BreadCrumbElement();
                breadCrumbElement.setName(facet.getFacetName());
                breadCrumbElement.setFacet(facet);
                breadCrumbElementList.add(breadCrumbElement);
            }
        }

        return breadCrumbElementList;
    }

//    private BreadCrumbElement getBreadCrumbElement(String breadCrumbName, SearchServiceResponse searchServiceResponse) {
////        BreadCrumbElement breadCrumbElement = new BreadCrumbElement();
////        breadCrumbElement.setName(breadCrumbName);
//        List<FacetGroup> facetGroupList = searchServiceResponse.getFacetGroups();
//        Optional<FacetGroup> facetGroup = facetGroupList.stream()
//                .filter(e -> e.getGroupName().equals(breadCrumbName))
//                .findFirst();
//        if(facetGroup.isPresent()) {
//            List<Facet> facetList = facetGroup.get().getFacets();
//            for(Facet facet: facetList) {
//                BreadCrumbElement breadCrumbElement = new BreadCrumbElement();
//                breadCrumbElement.setName(facet.getFacetName());
//                breadCrumbElement.setFacet(facet);
//            }
//        }
//        return breadCrumbElement;
//    }


}
