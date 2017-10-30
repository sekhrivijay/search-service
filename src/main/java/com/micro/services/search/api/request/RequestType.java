package com.micro.services.search.api.request;

public enum RequestType {
    SEARCH("search"),
    BROWSE("browse"),
    AUTOFILL("autofill");
    private String name;

    RequestType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static RequestType getRequestType(String name) {
        for (RequestType requestType: RequestType.values()) {
            if (requestType.getName().equals(name)) {
                return requestType;
            }
        }
        return SEARCH;
    }

}
