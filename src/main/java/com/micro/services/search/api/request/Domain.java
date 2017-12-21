package com.micro.services.search.api.request;

import com.micro.services.search.config.GlobalConstants;

public enum Domain {
    DESKTOP(GlobalConstants.DESKTOP),
    MOBILE(GlobalConstants.MOBILE);
    private String name;

    Domain(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static Domain getDomain(String name) {
        for (Domain domain : Domain.values()) {
            if (domain.getName().equals(name)) {
                return domain;
            }
        }
        return DESKTOP;
    }
}

