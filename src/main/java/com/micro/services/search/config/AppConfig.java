package com.micro.services.search.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@ConfigurationProperties(prefix = "service")
@EnableConfigurationProperties
@Configuration
public class AppConfig {
    @Bean
    @ConditionalOnMissingBean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    private List<String> sortList;
    private Map<String, String> sitesBfMap;

    AppConfig() {
        this.sortList = new ArrayList<>();
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
}
