package com.ftd.services.search.api.response;


import java.io.Serializable;
import java.util.List;

public class SearchServiceResponse implements Serializable {
    private Debug debug;
    private Long numFound;
    private Integer rows;
    private List<FacetGroup> facetGroups;
    private List<ResponseGroup> responseGroups;
    private Redirect redirect;
    private List<AutoCorrect> autoCorrectList;
    private List<DidYouMean> didYouMeanList;
    private Pagination pagination;
    private List<BreadCrumbTrail> breadCrumbTrail;
    private List<Document> documentList;
    private String originalQuery;
    private List<SortTerm> sortTermList;
    private Boolean cacheable = true;


    public Debug getDebug() {
        return debug;
    }

    public void setDebug(Debug debug) {
        this.debug = debug;
    }

    public Long getNumFound() {
        return numFound;
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

    public void setNumFound(Long numFound) {
        this.numFound = numFound;
    }

    public Integer getRows() {
        return rows;
    }

    public void setRows(Integer rows) {
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

    public List<SortTerm> getSortTermList() {
        return sortTermList;
    }

    public void setSortTermList(List<SortTerm> sortTermList) {
        this.sortTermList = sortTermList;
    }

    public Boolean isCacheable() {
        return cacheable;
    }

    public void setCacheable(Boolean cacheable) {
        this.cacheable = cacheable;
    }

    @Override
    public String toString() {
        return "SearchServiceResponse{" +
                "debug=" + debug +
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
                ", sortTermList=" + sortTermList +
                ", cacheable=" + cacheable +
                '}';
    }

    public static final class SearchServiceResponseBuilder {
        private Debug debug;
        private Long numFound;
        private Integer rows;
        private List<FacetGroup> facetGroups;
        private List<ResponseGroup> responseGroups;
        private Redirect redirect;
        private List<AutoCorrect> autoCorrectList;
        private List<DidYouMean> didYouMeanList;
        private Pagination pagination;
        private List<BreadCrumbTrail> breadCrumbTrail;
        private List<Document> documentList;
        private String originalQuery;
        private List<SortTerm> sortTermList;
        private Boolean cacheable = true;

        private SearchServiceResponseBuilder() {
        }

        public static SearchServiceResponseBuilder aSearchServiceResponse() {
            return new SearchServiceResponseBuilder();
        }

        public SearchServiceResponseBuilder withDebug(Debug debug) {
            this.debug = debug;
            return this;
        }

        public SearchServiceResponseBuilder withNumFound(Long numFound) {
            this.numFound = numFound;
            return this;
        }

        public SearchServiceResponseBuilder withRows(Integer rows) {
            this.rows = rows;
            return this;
        }

        public SearchServiceResponseBuilder withFacetGroups(List<FacetGroup> facetGroups) {
            this.facetGroups = facetGroups;
            return this;
        }

        public SearchServiceResponseBuilder withResponseGroups(List<ResponseGroup> responseGroups) {
            this.responseGroups = responseGroups;
            return this;
        }

        public SearchServiceResponseBuilder withRedirect(Redirect redirect) {
            this.redirect = redirect;
            return this;
        }

        public SearchServiceResponseBuilder withAutoCorrectList(List<AutoCorrect> autoCorrectList) {
            this.autoCorrectList = autoCorrectList;
            return this;
        }

        public SearchServiceResponseBuilder withDidYouMeanList(List<DidYouMean> didYouMeanList) {
            this.didYouMeanList = didYouMeanList;
            return this;
        }

        public SearchServiceResponseBuilder withPagination(Pagination pagination) {
            this.pagination = pagination;
            return this;
        }

        public SearchServiceResponseBuilder withBreadCrumbTrail(List<BreadCrumbTrail> breadCrumbTrail) {
            this.breadCrumbTrail = breadCrumbTrail;
            return this;
        }

        public SearchServiceResponseBuilder withDocumentList(List<Document> documentList) {
            this.documentList = documentList;
            return this;
        }

        public SearchServiceResponseBuilder withOriginalQuery(String originalQuery) {
            this.originalQuery = originalQuery;
            return this;
        }

        public SearchServiceResponseBuilder withSortTermList(List<SortTerm> sortTermList) {
            this.sortTermList = sortTermList;
            return this;
        }

        public SearchServiceResponseBuilder withCacheable(Boolean cacheable) {
            this.cacheable = cacheable;
            return this;
        }

        public SearchServiceResponse build() {
            SearchServiceResponse searchServiceResponse = new SearchServiceResponse();
            searchServiceResponse.setDebug(debug);
            searchServiceResponse.setNumFound(numFound);
            searchServiceResponse.setRows(rows);
            searchServiceResponse.setFacetGroups(facetGroups);
            searchServiceResponse.setResponseGroups(responseGroups);
            searchServiceResponse.setRedirect(redirect);
            searchServiceResponse.setAutoCorrectList(autoCorrectList);
            searchServiceResponse.setDidYouMeanList(didYouMeanList);
            searchServiceResponse.setPagination(pagination);
            searchServiceResponse.setBreadCrumbTrail(breadCrumbTrail);
            searchServiceResponse.setDocumentList(documentList);
            searchServiceResponse.setOriginalQuery(originalQuery);
            searchServiceResponse.setSortTermList(sortTermList);
            searchServiceResponse.setCacheable(cacheable);
            return searchServiceResponse;
        }
    }
}
