package com.micro.services.search.bl.details;

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

/**
 * https://tools.publicis.sapient.com/confluence/display/FLTD/REST+EndPoint+Spec+-+Product+Service
 *
 * @author cdegreef
 *
 */
@Named("productClient")
public class ProductClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProductClient.class);

    private RestTemplate        restTemplate;
    private boolean             enabled;
    private String              baseUrl;

    public ProductClient(
            @Named("restTemplate") RestTemplate restTemplate,
            @Value("${service.productService.baseUrl}") String baseUrl,
            @Value("${service.productService.enabled:false}") boolean enabled) {

        this.restTemplate = restTemplate;
        this.baseUrl = baseUrl;
        this.enabled = enabled;
    }

    public List<DetailsDocument> findDetails(List<String> productIds) {
        /*
         * A list of documents for the provided productIds, respectively.
         */
        List<DetailsDocument> results = new ArrayList<>(productIds.size());
        if (isEnabled()) {

            try {
                contactServiceForDetails(buildFullUrl(productIds));
            } catch (HttpClientErrorException e) {
                LOGGER.warn("{}", e.getMessage());
            }
        }
        return results;
    }

    /**
     * Public visibility for jUnit testing, otherwise consider to be private.
     *
     * @param productIds
     * @return
     */
    public String buildFullUrl(List<String> productIds) {
        /*
         * Create the url with a comma separated list, removing the last unneeded comma.
         */
        StringBuilder url = new StringBuilder();
        url.append(baseUrl);
        url.append('/');
        productIds.stream().forEach(id -> url.append(id).append(','));
        url.setLength(url.length() - 1);
        return url.toString();
    }

    private String contactServiceForDetails(String url) throws HttpClientErrorException {
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        LOGGER.debug("Calling {} results: {}", url, new Gson().toJson(response));
        return response.getBody();
    }

    private boolean isEnabled() {
        return enabled;
    }
}
