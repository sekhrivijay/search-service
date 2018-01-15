package com.ftd.services.search.api.request;


import java.io.Serializable;

import com.ftd.services.search.config.GlobalConstants;

public enum From  implements Serializable {
    INDEX(GlobalConstants.INDEX),
    CACHE(GlobalConstants.CACHE),
    DEFAULT(GlobalConstants.DEFAULT);

    private String name;

    From(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static From getFrom(String name) {
        for (From from: From.values()) {
            if (from.getName().equals(name)) {
                return from;
            }
        }
        return DEFAULT;
    }

    @Override
    public String toString() {
        return "From{" +
                "name='" + name + '\'' +
                '}';
    }
}