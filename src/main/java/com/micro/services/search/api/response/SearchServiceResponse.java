package com.micro.services.search.api.response;




import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class SearchServiceResponse implements Serializable {
    private List<Map<String, String>> documents;
    private Debug debug;
    private long numFound;
    private String redirect;
    private List<FacetGroup> facetGroups;
    private List<ResponseGroup> responseGroups;

    public List<Map<String, String>> getDocuments() {
        return documents;
    }

    public void setDocuments(List<Map<String, String>> documents) {
        this.documents = documents;
    }

    public Debug getDebug() {
        return debug;
    }

    public void setDebug(Debug debug) {
        this.debug = debug;
    }

    public long getNumFound() {
        return numFound;
    }

    public void setNumFound(long numFound) {
        this.numFound = numFound;
    }

    public List<FacetGroup> getFacetGroups() {
        return facetGroups;
    }

    public void setFacetGroups(List<FacetGroup> facetGroups) {
        this.facetGroups = facetGroups;
    }

    public List<ResponseGroup> getResponseGroups() {
        return responseGroups;
    }

    public void setResponseGroups(List<ResponseGroup> responseGroups) {
        this.responseGroups = responseGroups;
    }

    public String getRedirect() {
        return redirect;
    }

    public void setRedirect(String redirect) {
        this.redirect = redirect;
    }

    @Override
    public String toString() {
        return "SearchServiceResponse{" +
                "documents=" + documents +
                ", debug=" + debug +
                ", numFound=" + numFound +
                ", redirect='" + redirect + '\'' +
                ", facetGroups=" + facetGroups +
                ", responseGroups=" + responseGroups +
                '}';
    }
}
