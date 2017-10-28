package com.micro.services.search.api.response;

import java.io.Serializable;

public class SortTerm implements Serializable {
    private String sortBy;
    private SortOrder sortOrder;
    private String ascendingUrl;
    private String descendingUrl;
    private boolean selected;

    public String getSortBy() {
        return sortBy;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }

    public SortOrder getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(SortOrder sortOrder) {
        this.sortOrder = sortOrder;
    }

    public String getAscendingUrl() {
        return ascendingUrl;
    }

    public void setAscendingUrl(String ascendingUrl) {
        this.ascendingUrl = ascendingUrl;
    }

    public String getDescendingUrl() {
        return descendingUrl;
    }

    public void setDescendingUrl(String descendingUrl) {
        this.descendingUrl = descendingUrl;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    @Override
    public String toString() {
        return "SortTerm{" +
                "sortBy='" + sortBy + '\'' +
                ", sortOrder=" + sortOrder +
                ", ascendingUrl='" + ascendingUrl + '\'' +
                ", descendingUrl='" + descendingUrl + '\'' +
                ", selected=" + selected +
                '}';
    }
}