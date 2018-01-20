package com.ftd.services.search.config;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.impl.CloudSolrClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;


@Configuration
@EnableConfigurationProperties(AppConfigProperties.class)
public class AppConfig {
    @Bean
    @ConditionalOnMissingBean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    @ConditionalOnMissingBean
    public SolrClient solrClient(
            @Value("${service.solrService.zkEnsembleDestination}") String zkEnsembleDestination,
            @Value("${service.solrService.collectionDestination}") String collectionDestination,
            @Value("${service.solrService.zkTimeoutDestination}") int zkTimeoutDestination
    ) {
        CloudSolrClient cloudSolrClient = new CloudSolrClient.Builder()
                .withZkHost(zkEnsembleDestination)
                .build();
        cloudSolrClient.setDefaultCollection(collectionDestination);
        cloudSolrClient.setZkConnectTimeout(zkTimeoutDestination);
        return cloudSolrClient;
    }

    @Value("${spring.application.name}")
    public void setApplicationName(String applicationName) {
        GlobalConstants.setApplicationName(applicationName);
    }

    @Value("${spring.profiles.active}")
    public void setEnvironment(String environment) {
        GlobalConstants.setEnvironment(environment);
    }

}
