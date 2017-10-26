package com.micro.services.search.api.response;

import java.io.Serializable;

public class BreadCrumbElement implements Serializable {
    private String name;
    private String url;
    private Boolean isLeaf;
    private Facet facet;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Boolean getLeaf() {
        return isLeaf;
    }

    public void setLeaf(Boolean leaf) {
        isLeaf = leaf;
    }

    public Facet getFacet() {
        return facet;
    }

    public void setFacet(Facet facet) {
        this.facet = facet;
    }

    @Override
    public String toString() {
        return "BreadCrumbElement{" +
                "name='" + name + '\'' +
                ", url='" + url + '\'' +
                ", isLeaf=" + isLeaf +
                ", facet=" + facet +
                '}';
    }
}
