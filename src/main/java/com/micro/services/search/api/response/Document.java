package com.micro.services.search.api.response;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Document implements Serializable {
    private Map<String, String> record = new HashMap<>();
    private String url;

    public Map<String, String> getRecord() {
        return record;
    }

    public void setRecord(Map<String, String> record) {
        this.record = record;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "Document{" +
                "record=" + record +
                ", url='" + url + '\'' +
                '}';
    }
}
