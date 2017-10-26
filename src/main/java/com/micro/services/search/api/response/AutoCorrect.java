package com.micro.services.search.api.response;

import java.io.Serializable;

public class AutoCorrect implements Serializable {
    private String searchedKey;
    private String correctedValue;
    private String url;


    public String getSearchedKey() {
        return searchedKey;
    }

    public void setSearchedKey(String searchedKey) {
        this.searchedKey = searchedKey;
    }

    public String getCorrectedValue() {
        return correctedValue;
    }

    public void setCorrectedValue(String correctedValue) {
        this.correctedValue = correctedValue;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "AutoCorrect{" +
                "searchedKey='" + searchedKey + '\'' +
                ", correctedValue='" + correctedValue + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
