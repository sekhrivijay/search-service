package com.micro.services.search.api.response;

import java.io.Serializable;

public class Redirect implements Serializable {
    private String redirectUrl;

    public String getRedirectUrl() {
        return redirectUrl;
    }

    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }

    @Override
    public String toString() {
        return "Redirect{" +
                "redirectUrl='" + redirectUrl + '\'' +
                '}';
    }
}
