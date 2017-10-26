package com.micro.services.search.api.response;

import java.io.Serializable;

public class Page implements Serializable {
    private long number;
    private boolean active;
    private String url;

    public long getNumber() {
        return number;
    }

    public void setNumber(long number) {
        this.number = number;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "Page{" +
                "number=" + number +
                ", active=" + active +
                ", url='" + url + '\'' +
                '}';
    }
}
