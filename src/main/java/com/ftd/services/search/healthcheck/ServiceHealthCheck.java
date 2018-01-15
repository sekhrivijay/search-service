package com.ftd.services.search.healthcheck;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

import com.ftd.services.search.api.request.From;
import com.ftd.services.search.api.request.SearchServiceRequest;
import com.ftd.services.search.api.response.SearchServiceResponse;
import com.ftd.services.search.bl.QueryService;

@Component
public class ServiceHealthCheck implements HealthIndicator {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceHealthCheck.class);
    private QueryService queryService;

    private static final SearchServiceRequest SEARCH_SERVICE_REQUEST = new SearchServiceRequest();

    public ServiceHealthCheck() {
        SEARCH_SERVICE_REQUEST.setQ("*:*");
        SEARCH_SERVICE_REQUEST.setFrom(From.INDEX);
    }


    @Autowired
    public void setQueryService(QueryService queryService) {
        this.queryService = queryService;
    }


    @Override
    public Health health() {

        try {
            SearchServiceResponse searchServiceResponse = queryService.query(SEARCH_SERVICE_REQUEST);
            if (searchServiceResponse == null) {
//                    || searchServiceResponse.getNumFound() == 0) {
                return Health.down().build();
            }

        } catch (Exception e) {
            LOGGER.error("Health check failed ... ", e);
            return Health.down().build();
        }
        return Health.up().build();
    }
}