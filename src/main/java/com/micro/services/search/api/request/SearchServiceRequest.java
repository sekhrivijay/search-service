package com.micro.services.search.api.request;

import com.micro.services.search.config.GlobalConstants;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static com.micro.services.search.config.GlobalConstants.COUNT;

public class SearchServiceRequest implements Serializable {
    private String q = "*";
    private String sort = GlobalConstants.ID;
    private String sortOrder = GlobalConstants.ASC;
    private int rows;
    private int start;
    private String[] fq;
    private boolean debug;
    private int round;
    private SearchServiceRequest parent;
    private String qt;
    private String siteId;
    private String facetSort = COUNT;
    private String[] facetFields;
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
                ", siteId='" + siteId + '\'' +
                ", facetSort='" + facetSort + '\'' +
                ", facetFields=" + Arrays.toString(facetFields) +
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
                ", facetSort='" + facetSort + '\'' +
                ", facetFields=" + Arrays.toString(facetFields) +
                ", groupFields=" + Arrays.toString(groupFields) +
                ", parameters=" + parameters +
                '}';
    }

    public String getCacheKey() {
        return String.valueOf(toCacheKey().hashCode());
    }


}
