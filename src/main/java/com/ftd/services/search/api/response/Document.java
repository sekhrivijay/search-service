package com.ftd.services.search.api.response;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Document implements Serializable {
    private static final long serialVersionUID = 7441169816499491466L;

    private Map<String, Object> record = new HashMap<>();
    private String url;

    public Map<String, Object> getRecord() {
        return record;
    }

    public void setRecord(Map<String, Object> record) {
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
