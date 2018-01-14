package com.ftd.service.search.api.response;


import java.io.Serializable;
import java.util.List;

public class ResponseGroup implements Serializable {
    private String groupName;
    private List<Facet> facets;

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public List<Facet> getFacets() {
        return facets;
    }

    public void setFacets(List<Facet> facets) {
        this.facets = facets;
    }

    @Override
    public String toString() {
        return "ResponseGroup{" +
                "groupName='" + groupName + '\'' +
                ", facets=" + facets +
                '}';
    }
}
