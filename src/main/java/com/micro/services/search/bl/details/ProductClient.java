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
import com.micro.services.search.api.response.Document;

/**
 * https://tools.publicis.sapient.com/confluence/display/FLTD/REST+EndPoint+Spec+-+ProductIdGroup+Service
 *
 * @author cdegreef
 *
 */
@Named("productClient")
public class ProductClient {
    private static final Logger LOGGER  = LoggerFactory.getLogger(ProductClient.class);

    private RestTemplate        restTemplate;
    private String              baseUrl;

    @Value("${WHAT.productService.enabled:true}")
    private boolean             enabled = true;

    @Value("${service.productService.version:0.1}")
    private String              version;

    public ProductClient(
            @Autowired RestTemplate restTemplate,
            @Value("${service.productService.baseUrl}") String baseUrl) {

        this.restTemplate = restTemplate;
        this.baseUrl = baseUrl;
    }

    @PostConstruct
    private void logIdentification() {
        LOGGER.info("url: {}, version: {}, enabled: {}", baseUrl, version, enabled);
    }

    public Map<String, Document> findDetails(Set<String> productIds) {
        /*
         * A list of documents for the provided productIds, respectively.
         */
        Map<String, Document> results = new HashMap<>(productIds.size());
        if (isEnabled()) {

            try {
                String json = contactServiceForDetails(buildFullUrl(productIds));
                @SuppressWarnings("unchecked")
                Map<String, Object> documentRecord = new Gson().fromJson(json, Map.class);
                // TODO what to do?
                System.out.println(documentRecord);
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
    String buildFullUrl(Set<String> productIds) {
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

    private HttpHeaders createHttpHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("version", version);
        return headers;
    }

    String contactServiceForDetails(String url) throws HttpClientErrorException {
        StringBuilder fullUrl = new StringBuilder();
        fullUrl.append(baseUrl);
        fullUrl.append('/');
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
