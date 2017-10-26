package com.micro.services.search.api.response;

import java.io.Serializable;

public class Redirect implements Serializable {
    private boolean redirect;
    private String redirectUrl;

    public boolean isRedirect() {
        return redirect;
    }

    public void setRedirect(boolean redirect) {
        this.redirect = redirect;
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
                "redirect=" + redirect +
                ", redirectUrl='" + redirectUrl + '\'' +
                '}';
    }
}
