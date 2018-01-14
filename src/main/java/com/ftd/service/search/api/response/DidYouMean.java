package com.ftd.service.search.api.response;

import java.io.Serializable;

public class DidYouMean implements Serializable {
    private String suggestedTerm;
    private String url;
    private long numberOfResults;

    public String getSuggestedTerm() {
        return suggestedTerm;
    }

    public void setSuggestedTerm(String suggestedTerm) {
        this.suggestedTerm = suggestedTerm;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public long getNumberOfResults() {
        return numberOfResults;
    }

    public void setNumberOfResults(long numberOfResults) {
        this.numberOfResults = numberOfResults;
    }

    @Override
    public String toString() {
        return "DidYouMean{" +
                "suggestedTerm='" + suggestedTerm + '\'' +
                ", url='" + url + '\'' +
                ", numberOfResults=" + numberOfResults +
                '}';
    }
}
