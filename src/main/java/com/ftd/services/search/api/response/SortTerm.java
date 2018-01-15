package com.ftd.services.search.api.response;

import java.io.Serializable;

public class SortTerm implements Serializable {
    private String sortBy;
    private SortDetail ascendingSortDetail;
    private SortDetail descendingSortDetail;

    public String getSortBy() {
        return sortBy;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }

    public SortDetail getAscendingSortDetail() {
        return ascendingSortDetail;
    }

    public void setAscendingSortDetail(SortDetail ascendingSortDetail) {
        this.ascendingSortDetail = ascendingSortDetail;
    }

    public SortDetail getDescendingSortDetail() {
        return descendingSortDetail;
    }

    public void setDescendingSortDetail(SortDetail descendingSortDetail) {
        this.descendingSortDetail = descendingSortDetail;
    }

    @Override
    public String toString() {
        return "SortTerm{" +
                "sortBy='" + sortBy + '\'' +
                ", ascendingSortDetail=" + ascendingSortDetail +
                ", descendingSortDetail=" + descendingSortDetail +
                '}';
    }
}