package com.micro.services.search.api.response;

import java.io.Serializable;

public class Facet implements Serializable {
    private String facetName;
    private long facetCount;

    public String getFacetName() {
        return facetName;
    }

    public void setFacetName(String facetName) {
        this.facetName = facetName;
    }

    public long getFacetCount() {
        return facetCount;
    }

    public void setFacetCount(long facetCount) {
        this.facetCount = facetCount;
    }


}