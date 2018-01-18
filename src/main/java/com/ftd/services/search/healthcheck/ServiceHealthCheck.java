package com.ftd.services.search.healthcheck;


import com.ftd.services.search.api.request.From;
import com.ftd.services.search.api.request.SearchServiceRequest;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

@Component
public class ServiceHealthCheck implements HealthIndicator {

//    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceHealthCheck.class);
//    private QueryService queryService;

    private static final SearchServiceRequest SEARCH_SERVICE_REQUEST = new SearchServiceRequest();

    public ServiceHealthCheck() {
        SEARCH_SERVICE_REQUEST.setQ("*:*");
        SEARCH_SERVICE_REQUEST.setFrom(From.INDEX);
    }


//    @Autowired
//    public void setQueryService(QueryService queryService) {
//        this.queryService = queryService;
//    }
//

    @Override
    public Health health() {

        return Health.up().build();
//        try {
//            SearchServiceResponse searchServiceResponse = queryService.query(SEARCH_SERVICE_REQUEST);
//            if (searchServiceResponse == null) {
////                    || searchServiceResponse.getNumFound() == 0) {
//                return Health.down().build();
//            }
//
//        } catch (Exception e) {
//            LOGGER.error("Health check failed ... ", e);
//            return Health.down().build();
//        }
//        return Health.up().build();
    }
}