package com.micro.services.search.api.response;

import java.io.Serializable;

public class SortDetail implements Serializable {
    private SortOrder sortOrder;
    private String url;
    private boolean selected;

    public SortOrder getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(SortOrder sortOrder) {
        this.sortOrder = sortOrder;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    @Override
    public String toString() {
        return "SortDetail{" +
                "sortOrder=" + sortOrder +
                ", url='" + url + '\'' +
                ", selected=" + selected +
                '}';
    }
}
