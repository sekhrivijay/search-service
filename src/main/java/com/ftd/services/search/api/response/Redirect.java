package com.ftd.services.search.api.response;

import java.io.Serializable;

public class Redirect implements Serializable {

    private static final long serialVersionUID = 1L;
    private String            redirectUrl;

    /**
     * This constructor is used in the expert systems rules
     *
     * @param redirectUrl
     */
    public Redirect(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }

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
