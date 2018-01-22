package com.ftd.services.search.api.request;

import static com.ftd.services.search.config.GlobalConstants.COUNT;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.ftd.services.search.config.GlobalConstants;

public class SearchServiceRequest implements Serializable {

    private static final long serialVersionUID = 7074581649415958670L;

    private String q = "*";
    private String bf;
    private String sort = GlobalConstants.ID;
    private String sortOrder = GlobalConstants.ASC;
    private int rows;
    private int start;
    private String[] fq;
    private boolean debug;
    private int round;
    private SearchServiceRequest parent;
    private String qt;
    private String siteId = GlobalConstants.FTD;
    private String facetSort = COUNT;
    private String[] facetFields;
    private String[] rangeFacetFields;
    private String[] groupFields;
    private boolean isFuzzyCompare;
    private boolean isSpellCheck;
    private boolean isMustMatchFiftyPercent;
    private boolean isMustMatchSeventyFivePercent;
    private From from = From.DEFAULT;
    private Domain domain = Domain.DESKTOP;
    private RequestType requestType = RequestType.SEARCH;
    private Map<String, List<String>> parameters;
    private Map<String, String[]> parametersOriginal;
    private Holder holder;

    private String memberType;
    private String zipCode;
    private String availFrom;
    private String availTo;

    public String getQ() {
        return q;
    }

    public void setQ(String q) {
        this.q = q;
    }

    public boolean isDebug() {
        return debug;
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    public int getRound() {
        return round;
    }

    public void setRound(int round) {
        this.round = round;
    }

    public String getQt() {
        return qt;
    }

    public void setQt(String qt) {
        this.qt = qt;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public String[] getFq() {
        return fq;
    }

    public void setFq(String[] fq) {
        this.fq = fq;
    }

    public String[] getFacetFields() {
        return facetFields;
    }

    public void setFacetFields(String[] facetFields) {
        this.facetFields = facetFields;
    }

    public String[] getGroupFields() {
        return groupFields;
    }

    public void setGroupFields(String[] groupFields) {
        this.groupFields = groupFields;
    }

    public String getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(String sortOrder) {
        this.sortOrder = sortOrder;
    }

    public String getFacetSort() {
        return facetSort;
    }

    public void setFacetSort(String facetSort) {
        this.facetSort = facetSort;
    }

    public Map<String, List<String>> getParameters() {
        return parameters;
    }

    public void setParameters(Map<String, List<String>> parameters) {
        this.parameters = parameters;
    }

    public From getFrom() {
        return from;
    }

    public void setFrom(From from) {
        this.from = from;
    }

    public Map<String, String[]> getParametersOriginal() {
        return parametersOriginal;
    }

    public void setParametersOriginal(Map<String, String[]> parametersOriginal) {
        this.parametersOriginal = parametersOriginal;
    }

    public boolean isFuzzyCompare() {
        return isFuzzyCompare;
    }

    public void setFuzzyCompare(boolean fuzzyCompare) {
        isFuzzyCompare = fuzzyCompare;
    }

    public boolean isSpellCheck() {
        return isSpellCheck;
    }

    public void setSpellCheck(boolean spellCheck) {
        isSpellCheck = spellCheck;
    }

    public boolean isMustMatchFiftyPercent() {
        return isMustMatchFiftyPercent;
    }

    public void setMustMatchFiftyPercent(boolean mustMatchFiftyPercent) {
        isMustMatchFiftyPercent = mustMatchFiftyPercent;
    }

    public boolean isMustMatchSeventyFivePercent() {
        return isMustMatchSeventyFivePercent;
    }

    public void setMustMatchSeventyFivePercent(boolean mustMatchSeventyFivePercent) {
        isMustMatchSeventyFivePercent = mustMatchSeventyFivePercent;
    }

    public SearchServiceRequest getParent() {
        return parent;
    }

    public void setParent(SearchServiceRequest parent) {
        this.parent = parent;
    }


    public RequestType getRequestType() {
        return requestType;
    }

    public void setRequestType(RequestType requestType) {
        this.requestType = requestType;
    }


    public Domain getDomain() {
        return domain;
    }

    public void setDomain(Domain domain) {
        this.domain = domain;
    }

    public Holder getHolder() {
        return holder;
    }

    public void setHolder(Holder holder) {
        this.holder = holder;
    }


    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    public String getBf() {
        return bf;
    }

    public void setBf(String bf) {
        this.bf = bf;
    }

    public String[] getRangeFacetFields() {
        return rangeFacetFields;
    }

    public void setRangeFacetFields(String[] rangeFacetFields) {
        this.rangeFacetFields = rangeFacetFields;
    }

    @Override
    public String toString() {
        return "SearchServiceRequest{" +
                "q='" + q + '\'' +
                ", sort='" + sort + '\'' +
                ", sortOrder='" + sortOrder + '\'' +
                ", rows=" + rows +
                ", start=" + start +
                ", fq=" + Arrays.toString(fq) +
                ", debug=" + debug +
                ", round=" + round +
                ", parent=" + parent +
                ", qt='" + qt + '\'' +
                ", bf='" + bf + '\'' +
                ", siteId='" + siteId + '\'' +
                ", facetSort='" + facetSort + '\'' +
                ", facetFields=" + Arrays.toString(facetFields) +
                ", rangeFacetFields=" + Arrays.toString(rangeFacetFields) +
                ", groupFields=" + Arrays.toString(groupFields) +
                ", isFuzzyCompare=" + isFuzzyCompare +
                ", isSpellCheck=" + isSpellCheck +
                ", isMustMatchFiftyPercent=" + isMustMatchFiftyPercent +
                ", isMustMatchSeventyFivePercent=" + isMustMatchSeventyFivePercent +
                ", from=" + from +
                ", holder=" + holder +
                ", domain=" + domain +
                ", requestType=" + requestType +
                ", parameters=" + parameters +
                ", parametersOriginal=" + parametersOriginal +
                ", memberType=" + memberType +
                ", availFrom=" + availFrom +
                ", availTo=" + availTo +
                ", zipCode=" + zipCode +
                '}';
    }

    public String toCacheKey() {
        return "SearchServiceRequest{" +
                "q='" + q + '\'' +
                ", sort='" + sort + '\'' +
                ", sortOrder='" + sortOrder + '\'' +
                ", rows=" + rows +
                ", start=" + start +
                ", domain=" + domain +
                ", fq='" + Arrays.toString(fq) + '\'' +
                ", siteId='" + siteId + '\'' +
                ", debug=" + debug +
                ", round=" + round +
                ", requestType=" + requestType +
                ", qt='" + qt + '\'' +
                ", bf='" + bf + '\'' +
                ", facetSort='" + facetSort + '\'' +
                ", facetFields=" + Arrays.toString(facetFields) +
                ", groupFields=" + Arrays.toString(groupFields) +
                ", rangeFacetFields=" + Arrays.toString(rangeFacetFields) +
                ", parameters=" + parameters +
                '}';
    }

    public String getCacheKey() {
        return String.valueOf(toCacheKey().hashCode());
    }

    public String getMemberType() {
        return memberType;
    }

    public void setMemberType(String memberType) {
        this.memberType = memberType;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getAvailFrom() {
        return availFrom;
    }

    public void setAvailFrom(String availFrom) {
        this.availFrom = availFrom;
    }

    public String getAvailTo() {
        return availTo;
    }

    public void setAvailTo(String availTo) {
        this.availTo = availTo;
    }


    public static final class SearchServiceRequestBuilder {
        private String bf;
        private String sort = GlobalConstants.ID;
        private String sortOrder = GlobalConstants.ASC;
        private int rows;
        private int start;
        private String[] fq;
        private String q = "*";
        private boolean debug;
        private int round;
        private SearchServiceRequest parent;
        private String qt;
        private String siteId = GlobalConstants.FTD;
        private String facetSort = COUNT;
        private String[] facetFields;
        private boolean isMustMatchSeventyFivePercent;
        private String[] rangeFacetFields;
        private String[] groupFields;
        private boolean isFuzzyCompare;
        private boolean isSpellCheck;
        private boolean isMustMatchFiftyPercent;
        private From from = From.DEFAULT;
        private Domain domain = Domain.DESKTOP;
        private RequestType requestType = RequestType.SEARCH;
        private Map<String, List<String>> parameters;
        private Map<String, String[]> parametersOriginal;
        private Holder holder;
        private String memberType;
        private String zipCode;
        private String availFrom;
        private String availTo;

        private SearchServiceRequestBuilder() {
        }

        public static SearchServiceRequestBuilder aSearchServiceRequest() {
            return new SearchServiceRequestBuilder();
        }

        public SearchServiceRequestBuilder withQ(String q) {
            this.q = q;
            return this;
        }

        public SearchServiceRequestBuilder withBf(String bf) {
            this.bf = bf;
            return this;
        }

        public SearchServiceRequestBuilder withSort(String sort) {
            this.sort = sort;
            return this;
        }

        public SearchServiceRequestBuilder withSortOrder(String sortOrder) {
            this.sortOrder = sortOrder;
            return this;
        }

        public SearchServiceRequestBuilder withRows(int rows) {
            this.rows = rows;
            return this;
        }

        public SearchServiceRequestBuilder withStart(int start) {
            this.start = start;
            return this;
        }

        public SearchServiceRequestBuilder withFq(String[] fq) {
            this.fq = fq;
            return this;
        }

        public SearchServiceRequestBuilder withDebug(boolean debug) {
            this.debug = debug;
            return this;
        }

        public SearchServiceRequestBuilder withRound(int round) {
            this.round = round;
            return this;
        }

        public SearchServiceRequestBuilder withParent(SearchServiceRequest parent) {
            this.parent = parent;
            return this;
        }

        public SearchServiceRequestBuilder withQt(String qt) {
            this.qt = qt;
            return this;
        }

        public SearchServiceRequestBuilder withSiteId(String siteId) {
            this.siteId = siteId;
            return this;
        }

        public SearchServiceRequestBuilder withFacetSort(String facetSort) {
            this.facetSort = facetSort;
            return this;
        }

        public SearchServiceRequestBuilder withFacetFields(String[] facetFields) {
            this.facetFields = facetFields;
            return this;
        }

        public SearchServiceRequestBuilder withRangeFacetFields(String[] rangeFacetFields) {
            this.rangeFacetFields = rangeFacetFields;
            return this;
        }

        public SearchServiceRequestBuilder withGroupFields(String[] groupFields) {
            this.groupFields = groupFields;
            return this;
        }

        public SearchServiceRequestBuilder withIsFuzzyCompare(boolean isFuzzyCompare) {
            this.isFuzzyCompare = isFuzzyCompare;
            return this;
        }

        public SearchServiceRequestBuilder withIsSpellCheck(boolean isSpellCheck) {
            this.isSpellCheck = isSpellCheck;
            return this;
        }

        public SearchServiceRequestBuilder withIsMustMatchFiftyPercent(boolean isMustMatchFiftyPercent) {
            this.isMustMatchFiftyPercent = isMustMatchFiftyPercent;
            return this;
        }

        public SearchServiceRequestBuilder withIsMustMatchSeventyFivePercent(boolean isMustMatchSeventyFivePercent) {
            this.isMustMatchSeventyFivePercent = isMustMatchSeventyFivePercent;
            return this;
        }

        public SearchServiceRequestBuilder withFrom(From from) {
            this.from = from;
            return this;
        }

        public SearchServiceRequestBuilder withDomain(Domain domain) {
            this.domain = domain;
            return this;
        }

        public SearchServiceRequestBuilder withRequestType(RequestType requestType) {
            this.requestType = requestType;
            return this;
        }

        public SearchServiceRequestBuilder withParameters(Map<String, List<String>> parameters) {
            this.parameters = parameters;
            return this;
        }

        public SearchServiceRequestBuilder withParametersOriginal(Map<String, String[]> parametersOriginal) {
            this.parametersOriginal = parametersOriginal;
            return this;
        }

        public SearchServiceRequestBuilder withHolder(Holder holder) {
            this.holder = holder;
            return this;
        }

        public SearchServiceRequestBuilder withMemberType(String memberType) {
            this.memberType = memberType;
            return this;
        }

        public SearchServiceRequestBuilder withZipCode(String zipCode) {
            this.zipCode = zipCode;
            return this;
        }

        public SearchServiceRequestBuilder withAvailFrom(String availFrom) {
            this.availFrom = availFrom;
            return this;
        }

        public SearchServiceRequestBuilder withAvailTo(String availTo) {
            this.availTo = availTo;
            return this;
        }

        public SearchServiceRequest build() {
            SearchServiceRequest searchServiceRequest = new SearchServiceRequest();
            searchServiceRequest.setQ(q);
            searchServiceRequest.setBf(bf);
            searchServiceRequest.setSort(sort);
            searchServiceRequest.setSortOrder(sortOrder);
            searchServiceRequest.setRows(rows);
            searchServiceRequest.setStart(start);
            searchServiceRequest.setFq(fq);
            searchServiceRequest.setDebug(debug);
            searchServiceRequest.setRound(round);
            searchServiceRequest.setParent(parent);
            searchServiceRequest.setQt(qt);
            searchServiceRequest.setSiteId(siteId);
            searchServiceRequest.setFacetSort(facetSort);
            searchServiceRequest.setFacetFields(facetFields);
            searchServiceRequest.setRangeFacetFields(rangeFacetFields);
            searchServiceRequest.setGroupFields(groupFields);
            searchServiceRequest.setFrom(from);
            searchServiceRequest.setDomain(domain);
            searchServiceRequest.setRequestType(requestType);
            searchServiceRequest.setParameters(parameters);
            searchServiceRequest.setParametersOriginal(parametersOriginal);
            searchServiceRequest.setHolder(holder);
            searchServiceRequest.setMemberType(memberType);
            searchServiceRequest.setZipCode(zipCode);
            searchServiceRequest.setAvailFrom(availFrom);
            searchServiceRequest.setAvailTo(availTo);
            searchServiceRequest.isFuzzyCompare = this.isFuzzyCompare;
            searchServiceRequest.isMustMatchSeventyFivePercent = this.isMustMatchSeventyFivePercent;
            searchServiceRequest.isMustMatchFiftyPercent = this.isMustMatchFiftyPercent;
            searchServiceRequest.isSpellCheck = this.isSpellCheck;
            return searchServiceRequest;
        }
    }
}
