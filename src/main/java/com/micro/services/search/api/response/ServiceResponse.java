package com.micro.services.search.api.response;




import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class ServiceResponse implements Serializable {
    private List<Map<String, String>> documents;
    private Debug debug;
    private long numFound;
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

    @Override
    public String toString() {
        return "ServiceResponse{" +
                "documents=" + documents +
                ", debug=" + debug +
                ", numFound=" + numFound +
                ", facetGroups=" + facetGroups +
                ", responseGroups=" + responseGroups +
                '}';
    }
}
