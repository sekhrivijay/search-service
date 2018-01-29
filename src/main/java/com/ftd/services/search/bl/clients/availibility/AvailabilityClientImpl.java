package com.ftd.services.search.bl.clients.availibility;

import com.codahale.metrics.annotation.ExceptionMetered;
import com.codahale.metrics.annotation.Timed;
import com.ftd.services.product.api.domain.response.Product;
import com.ftd.services.search.api.request.SearchServiceRequest;
import com.ftd.services.search.api.response.SearchServiceResponse;
import com.ftd.services.search.bl.clients.BaseClient;
import com.ftd.services.search.bl.details.AvailabilityParms;
import com.ftd.services.search.config.GlobalConstants;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import javax.inject.Named;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * https://tools.publicis.sapient.com/confluence/display/FLTD/ProductAvailability
 *
 * @author cdegreef
 */
@Named("availabilityClient")
public class AvailabilityClientImpl extends BaseClient implements AvailabilityClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(AvailabilityClientImpl.class);
    public static final AvailabilityServiceResponse FALLBACK_AVAILABILITY_RESPONSE = new AvailabilityServiceResponse();
    public static final AvailabilityServiceResponse DUMMY_AVAILABILITY_RESPONSE = new AvailabilityServiceResponse();

    private RestTemplate restTemplate;
    private String baseUrl;

    @Value("${service.availabilityService.enabled:true}")
    private boolean enabled = false;

    @Value("${service.availabilityService.version:0.1}")
    private String version;

    public AvailabilityClientImpl(
            @Autowired RestTemplate restTemplate,
            @Value("${service.availabilityService.baseUrl}") String baseUrl) {

        this.restTemplate = restTemplate;
        this.baseUrl = baseUrl;
    }

    @PostConstruct
    void logIdentification() {
        logIdentification(LOGGER, baseUrl, version, enabled);
    }


    public Map<String, Object> buildMap(AvailabilityServiceResponse products) {
        Map<String, Object> results = new HashMap<>();

        try {
            /*
             * The JSON string is an array of "product" instances. We want to figure out the
             * id for each of them and put the id as the key with the product object being
             * the value.
             */
            for (Product product : products.getProducts()) {
                results.put(product.getId(), product);
            }
        } catch (Exception e) {
            LOGGER.warn("{}", e.getMessage());
        }
        return results;

    }


    /**
     * Visibility for jUnit testing, otherwise consider to be private.
     *
     * @param productIds
     * @param startDate
     * @param endDate
     * @param zipCode
     * @return
     */
    public String buildUniquePartOfUrl(
            Set<String> productIds,
            String startDate,
            String endDate,
            String zipCode)
            throws Exception {

        AvailabilityParms ap = new AvailabilityParms();

        if (productIds != null) {
            productIds.stream().forEach(id -> ap.addProductId(0, id));
        }
        if (startDate != null || endDate != null) {
            ap.addDateRange(startDate, endDate);
        }
        ap.setZipCode(zipCode);

        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
        StringBuilder url = new StringBuilder();
        url.append("params=");
        url.append(gson.toJson(ap));

        return url.toString();
    }

    @Override
    @Timed
    @ExceptionMetered
    @HystrixCommand(groupKey = "hystrixGroup",
            commandKey = "availabilityServiceKey",
            threadPoolKey = "availabilityThreadPoolKey",
            fallbackMethod = "callAvailabilityServiceFallback")
    public AvailabilityServiceResponse
    callAvailabilityService(
            SearchServiceRequest searchServiceRequest,
            SearchServiceResponse searchServiceResponse) throws Exception {

        if (!enabled) {
            return DUMMY_AVAILABILITY_RESPONSE;
        }

        String startDate = searchServiceRequest.getAvailFrom();
        String endDate = searchServiceRequest.getAvailTo();
        String zipCode = searchServiceRequest.getZipCode();
//        final Set<String> productIds = new HashSet<>();
//        productIds.add("960");
        Set<String> productIds = getPids(searchServiceResponse);

        String uniquePartOfUrl = buildUniquePartOfUrl(productIds, startDate, endDate, zipCode);
        StringBuilder fullUrl = new StringBuilder();
        fullUrl.append(baseUrl.replace(GlobalConstants.SITE_ID, searchServiceRequest.getSiteId()));
        if (uniquePartOfUrl != null && uniquePartOfUrl.trim().length() > 0) {
            fullUrl.append(GlobalConstants.QUESTION_MARK);
            fullUrl.append(URLEncoder.encode(uniquePartOfUrl, GlobalConstants.UTF_8));
        }
        HttpEntity<AvailabilityServiceResponse> entity = new HttpEntity<>(createHttpHeaders(version));
        ResponseEntity<AvailabilityServiceResponse> response = restTemplate.exchange(
                fullUrl.toString(), HttpMethod.GET, entity, AvailabilityServiceResponse.class);
        return response.getBody();
    }


    public AvailabilityServiceResponse callAvailabilityServiceFallback(
            SearchServiceRequest searchServiceRequest,
            SearchServiceResponse searchServiceResponse) throws Exception {
        return FALLBACK_AVAILABILITY_RESPONSE;
    }

    public boolean isEnabled() {
        return enabled;
    }
}
