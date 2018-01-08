package com.micro.services.search.bl.details;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class DetailsDocument implements Serializable {
    private static final long   serialVersionUID = -3206897168439133737L;

    private Map<String, String> record           = new HashMap<>();

    public Map<String, String> getRecord() {
        return record;
    }

    public void setRecord(Map<String, String> record) {
        this.record = record;
    }
}
