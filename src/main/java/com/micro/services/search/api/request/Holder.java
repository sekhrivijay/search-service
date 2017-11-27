package com.micro.services.search.api.request;

import com.micro.services.search.api.response.Redirect;

public class Holder {
    private Redirect redirect;

    public Redirect getRedirect() {
        return redirect;
    }

    public void setRedirect(Redirect redirect) {
        this.redirect = redirect;
    }

    @Override
    public String toString() {
        return "Holder{" +
                "redirect=" + redirect +
                '}';
    }
}
