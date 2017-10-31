package com.micro.services.search.api.request;

import com.micro.services.search.config.GlobalConstants;

public enum Site {
    DESKTOP(GlobalConstants.DESKTOP),
    MOBILE(GlobalConstants.MOBILE);
    private String name;

    Site(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static Site getSite(String name) {
        for (Site site: Site.values()) {
            if (site.getName().equals(name)) {
                return site;
            }
        }
        return DESKTOP;
    }
}

