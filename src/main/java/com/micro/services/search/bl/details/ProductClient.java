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
 * https://tools.publicis.sapient.com/confluence/display/FLTD/REST+EndPoint+Spec+-+ProductIdGroup+Service
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
     * Visibility for jUnit testing, otherwise consider to be private.
     *
     * @param productIds
     * @return
     */
    String buildFullUrl(List<String> productIds) {
        /*
         * Create the url with a comma separated list, removing the last unneeded comma.
         */
        StringBuilder url = new StringBuilder();
        productIds.stream().forEach(id -> url.append(id).append(','));
        if (url.length() == 0) {
            return "";
        }
        url.setLength(url.length() - 1);
        return url.toString();
    }

    String contactServiceForDetails(String url) throws HttpClientErrorException {
        StringBuilder fullUrl = new StringBuilder();
        fullUrl.append(baseUrl);
        fullUrl.append('/');
        if (url != null && url.trim().length() > 0) {
            fullUrl.append(url);
        }
        LOGGER.debug("Calling {}", fullUrl.toString());
        ResponseEntity<String> response = restTemplate.getForEntity(fullUrl.toString(), String.class);
        LOGGER.debug("results: {}", new Gson().toJson(response));
        return response.getBody();
    }

    private boolean isEnabled() {
        return enabled;
    }
}
