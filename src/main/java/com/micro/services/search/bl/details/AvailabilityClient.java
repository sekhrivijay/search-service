package com.micro.services.search.bl.details;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.micro.services.search.api.response.Document;

/**
 * https://tools.publicis.sapient.com/confluence/display/FLTD/ProductAvailability
 *
 * @author cdegreef
 *
 */
@Named("availabilityClient")
public class AvailabilityClient {
    private static final Logger LOGGER  = LoggerFactory.getLogger(AvailabilityClient.class);

    private RestTemplate        restTemplate;
    private String              baseUrl;

    @Value("${service.availabilityService.enabled:true}")
    private boolean             enabled = true;

    @Value("${service.availabilityService.version:0.1}")
    private String              version;

    public AvailabilityClient(
            @Autowired RestTemplate restTemplate,
            @Value("${service.availabilityService.baseUrl}") String baseUrl) {

        this.restTemplate = restTemplate;
        this.baseUrl = baseUrl;
    }

    @PostConstruct
    private void logIdentification() {
        LOGGER.info("url: {}, version: {}, enabled: {}", baseUrl, version, enabled);
    }

    /**
     * A list of documents for the provided productIds, respectively.
     */
    public Map<String, Document> findDetails(
            Set<String> productIds,
            String startDate,
            String endDate,
            String zipCode) {
        Map<String, Document> results = new HashMap<>(productIds.size());

        if (isEnabled()) {
            try {
                contactServiceForDetails(buildFullUrl(productIds, startDate, endDate, zipCode));
            } catch (HttpClientErrorException e) {
                LOGGER.warn("{} returned {}", baseUrl, e.getMessage());
            }
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
    String buildFullUrl(Set<String> productIds, String startDate, String endDate, String zipCode) {

        AvailabilityParms ap = new AvailabilityParms();

        if (productIds != null) {
            productIds.stream().forEach(id -> ap.addProductId(0, id));
        }
        if (startDate != null || endDate != null) {
            ap.addDateRange(startDate, endDate);
        }
        ap.setZipCode(zipCode);

        StringBuilder url = new StringBuilder();
        url.append("params=");

        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
        url.append(gson.toJson(ap));

        return url.toString();
    }

    private HttpHeaders createHttpHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("version", version);
        return headers;
    }

    String contactServiceForDetails(String url) throws HttpClientErrorException {
        StringBuilder fullUrl = new StringBuilder();
        fullUrl.append(baseUrl);
        fullUrl.append('?');
        if (url != null && url.trim().length() > 0) {
            fullUrl.append(url);
        }
        HttpEntity<String> entity = new HttpEntity<>(createHttpHeaders());
        ResponseEntity<String> response = restTemplate.exchange(
                fullUrl.toString(), HttpMethod.GET, entity, String.class);
        return response.getBody();
    }

    private boolean isEnabled() {
        return enabled;
    }
}
