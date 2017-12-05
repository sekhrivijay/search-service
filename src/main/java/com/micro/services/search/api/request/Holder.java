package com.micro.services.search.api.request;

import com.micro.services.search.api.response.Redirect;

import java.io.Serializable;

public class Holder implements Serializable {
    private Redirect redirect;
    private boolean cacheable = true;

    public Redirect getRedirect() {
        return redirect;
    }

    public void setRedirect(Redirect redirect) {
        this.redirect = redirect;
    }

    public Boolean isCacheable() {
        return cacheable;
    }

    public void setCacheable(Boolean cacheable) {
        this.cacheable = cacheable;
    }

    @Override
    public String toString() {
        return "Holder{" +
                "redirect=" + redirect +
                ", cacheable=" + cacheable +
                '}';
    }
}
