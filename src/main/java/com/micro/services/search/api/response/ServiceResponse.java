package com.micro.services.search.api.response;




import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class ServiceResponse implements Serializable {
    private List<Map<String, String>> deals;
    private Debug debug;
    private long numFound;
    private List<FacetGroup> facetGroups;
    private List<ResponseGroup> responseGroups;

    public List<Map<String, String>> getDeals() {
        return deals;
    }

    public void setDeals(List<Map<String, String>> deals) {
        this.deals = deals;
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
                "deals=" + deals +
                ", debug=" + debug +
                ", numFound=" + numFound +
                ", facetGroups=" + facetGroups +
                ", responseGroups=" + responseGroups +
                '}';
    }
}
