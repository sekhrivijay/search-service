package com.micro.services.search.api.response;




import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class SearchServiceResponse implements Serializable {
    private List<Map<String, String>> documents;
    private Debug debug;
    private long numFound;
    private long rows;
    private List<FacetGroup> facetGroups;
    private List<ResponseGroup> responseGroups;
    private Redirect redirect;
    private List<AutoCorrect> autoCorrectList;
    private List<DidYouMean> didYouMeanList;
    private Pagination pagination;
    private List<BreadCrumbTrail> breadCrumbTrail;
    private List<Document> documentList;
    private String originalQuery;



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

    public List<AutoCorrect> getAutoCorrectList() {
        return autoCorrectList;
    }

    public void setAutoCorrectList(List<AutoCorrect> autoCorrectList) {
        this.autoCorrectList = autoCorrectList;
    }

    public List<DidYouMean> getDidYouMeanList() {
        return didYouMeanList;
    }

    public void setDidYouMeanList(List<DidYouMean> didYouMeanList) {
        this.didYouMeanList = didYouMeanList;
    }

    public long getRows() {
        return rows;
    }

    public void setRows(long rows) {
        this.rows = rows;
    }

    public Redirect getRedirect() {
        return redirect;
    }

    public void setRedirect(Redirect redirect) {
        this.redirect = redirect;
    }

    public Pagination getPagination() {
        return pagination;
    }

    public void setPagination(Pagination pagination) {
        this.pagination = pagination;
    }

    public List<BreadCrumbTrail> getBreadCrumbTrail() {
        return breadCrumbTrail;
    }

    public void setBreadCrumbTrail(List<BreadCrumbTrail> breadCrumbTrail) {
        this.breadCrumbTrail = breadCrumbTrail;
    }

    public List<Document> getDocumentList() {
        return documentList;
    }

    public void setDocumentList(List<Document> documentList) {
        this.documentList = documentList;
    }

    public String getOriginalQuery() {
        return originalQuery;
    }

    public void setOriginalQuery(String originalQuery) {
        this.originalQuery = originalQuery;
    }

    @Override
    public String toString() {
        return "SearchServiceResponse{" +
                "documents=" + documents +
                ", debug=" + debug +
                ", numFound=" + numFound +
                ", rows=" + rows +
                ", facetGroups=" + facetGroups +
                ", responseGroups=" + responseGroups +
                ", redirect=" + redirect +
                ", autoCorrectList=" + autoCorrectList +
                ", didYouMeanList=" + didYouMeanList +
                ", pagination=" + pagination +
                ", breadCrumbTrail=" + breadCrumbTrail +
                ", documentList=" + documentList +
                ", originalQuery='" + originalQuery + '\'' +
                '}';
    }
}
