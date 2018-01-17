package com.ftd.services.search.bl.clients.solr.util;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.impl.CloudSolrClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties
public class SolrServerInitializer {
    private SolrClient solrClient;


    @Value("${service.solrService.zkEnsembleDestination}")
    private String zkEnsembleDestination;

    @Value("${service.solrService.collectionDestination}")
    private String collectionDestination;

    @Value("${service.solrService.zkTimeoutDestination}")
    private int zkTimeoutDestination;

    SolrServerInitializer() {
    }

    @Bean
    public SolrClient getSolrClient() {
        CloudSolrClient cloudSolrClient = new CloudSolrClient.Builder()
                .withZkHost(zkEnsembleDestination)
                .build();
        cloudSolrClient.setDefaultCollection(collectionDestination);
        cloudSolrClient.setZkConnectTimeout(zkTimeoutDestination);

        this.solrClient = cloudSolrClient;
        return solrClient;
    }
}