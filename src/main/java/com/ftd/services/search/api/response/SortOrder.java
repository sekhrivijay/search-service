package com.ftd.services.search.api.response;

import java.io.Serializable;

public enum SortOrder implements Serializable {
    ASCENDING("asc"),
    DESCENDING("desc");
    private String name;

    SortOrder(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}