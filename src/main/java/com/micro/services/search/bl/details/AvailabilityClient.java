package com.micro.services.search.bl.details;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * https://tools.publicis.sapient.com/confluence/display/FLTD/ProductAvailability
 *
 * @author cdegreef
 *
 */
@Named("availabilityClient")
public class AvailabilityClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(AvailabilityClient.class);

    private RestTemplate        restTemplate;
    private boolean             enabled;
    private String              baseUrl;

    public AvailabilityClient(
            @Named("restTemplate") RestTemplate restTemplate,
            @Value("${service.availabilityService.baseUrl}") String baseUrl,
            @Value("${service.availabilityService.enabled:false}") boolean enabled) {

        this.restTemplate = restTemplate;
        this.baseUrl = baseUrl;
        this.enabled = enabled;
    }

    /**
     * A list of documents for the provided productIds, respectively.
     */
    public List<DetailsDocument> findDetails(
            List<String> productIds,
            LocalDate startDate,
            LocalDate endDate,
            String zipCode) {
        List<DetailsDocument> results = new ArrayList<>(productIds.size());

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
    String buildFullUrl(List<String> productIds, LocalDate startDate, LocalDate endDate, String zipCode) {

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

    String contactServiceForDetails(String url) throws HttpClientErrorException {
        StringBuilder fullUrl = new StringBuilder();
        fullUrl.append(baseUrl);
        fullUrl.append('?');
        if (url != null && url.trim().length() > 0) {
            fullUrl.append(url);
        }
        ResponseEntity<String> response = restTemplate.getForEntity(fullUrl.toString(), String.class);
        LOGGER.debug("Calling {} results: {}", fullUrl.toString(), new Gson().toJson(response));
        return response.getBody();
    }

    private boolean isEnabled() {
        return enabled;
    }
}
