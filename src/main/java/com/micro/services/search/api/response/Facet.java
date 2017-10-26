package com.micro.services.search.api.response;

import java.io.Serializable;

public class Facet implements Serializable {
    private String facetName;
    private long facetCount;
    private String url;

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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "Facet{" +
                "facetName='" + facetName + '\'' +
                ", facetCount=" + facetCount +
                ", url='" + url + '\'' +
                '}';
    }
}