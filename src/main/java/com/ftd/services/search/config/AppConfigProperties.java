package com.ftd.services.search.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@ConfigurationProperties(prefix = "service")
@EnableConfigurationProperties
public class AppConfigProperties {
    private List<String> sortList;
    private List<String> facetList;
    private Map<String, String> sitesBfMap;

    AppConfigProperties() {
        this.sortList = new ArrayList<>();
        this.facetList = new ArrayList<>();
    }

    public List<String> getSortList() {
        return sortList;
    }

    public void setSortList(List<String> sortList) {
        this.sortList = sortList;
    }

    public Map<String, String> getSitesBfMap() {
        return sitesBfMap;
    }

    public void setSitesBfMap(Map<String, String> sitesBfMap) {
        this.sitesBfMap = sitesBfMap;
    }

    public List<String> getFacetList() {
        return facetList;
    }

    public void setFacetList(List<String> facetList) {
        this.facetList = facetList;
    }
}
