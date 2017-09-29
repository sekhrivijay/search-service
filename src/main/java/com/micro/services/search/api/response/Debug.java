package com.micro.services.search.api.response;

import com.micro.services.search.api.request.SearchServiceRequest;

import java.io.Serializable;
import java.util.List;

public class Debug implements Serializable {
    private String gitInformation;
    private List<String> queries;
    private int round;
    private SearchServiceRequest searchServiceRequest;

    public String getGitInformation() {
        return gitInformation;
    }

    public void setGitInformation(String gitInformation) {
        this.gitInformation = gitInformation;
    }

    public List<String> getQueries() {
        return queries;
    }

    public void setQueries(List<String> queries) {
        this.queries = queries;
    }

    public int getRound() {
        return round;
    }

    public void setRound(int round) {
        this.round = round;
    }

    public SearchServiceRequest getSearchServiceRequest() {
        return searchServiceRequest;
    }

    public void setSearchServiceRequest(SearchServiceRequest searchServiceRequest) {
        this.searchServiceRequest = searchServiceRequest;
    }
}
