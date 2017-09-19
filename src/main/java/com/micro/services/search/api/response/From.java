package com.micro.services.search.api.response;


import com.micro.services.search.config.GlobalConstants;

import java.io.Serializable;

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
        for(From from: From.values()) {
            if(from.getName().equals(name)) {
                return from;
            }
        }
        return DEFAULT;
    }
}