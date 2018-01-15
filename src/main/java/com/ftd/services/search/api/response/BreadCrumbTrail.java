package com.ftd.services.search.api.response;

import java.io.Serializable;
import java.util.List;

public class BreadCrumbTrail implements Serializable {
    private List<BreadCrumbElement> breadCrumbs;
    private BreadCrumbTrailType breadCrumbTrailType;

    public List<BreadCrumbElement> getBreadCrumbs() {
        return breadCrumbs;
    }

    public void setBreadCrumbs(List<BreadCrumbElement> breadCrumbs) {
        this.breadCrumbs = breadCrumbs;
    }

    public BreadCrumbTrailType getBreadCrumbTrailType() {
        return breadCrumbTrailType;
    }

    public void setBreadCrumbTrailType(BreadCrumbTrailType breadCrumbTrailType) {
        this.breadCrumbTrailType = breadCrumbTrailType;
    }

    @Override
    public String toString() {
        return "BreadCrumbTrail{" +
                "breadCrumbs=" + breadCrumbs +
                ", breadCrumbTrailType=" + breadCrumbTrailType +
                '}';
    }
}
