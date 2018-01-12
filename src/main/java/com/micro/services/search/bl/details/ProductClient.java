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

import com.ftd.services.product.api.domain.response.Product;
import com.ftd.services.product.api.domain.response.ProductServiceResponse;
import com.google.gson.Gson;

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

    public Map<String, Object> findDetails(Set<String> productIds) {
        /*
         * A list of documents for the provided productIds, respectively.
         */
        Map<String, Object> results = new HashMap<>(productIds.size());
        if (isEnabled()) {

            try {
                /*
                 * The JSON string is an array of "product" instances. We want to figure out the
                 * id for each of them and put the id as the key with the product object being
                 * the value.
                 */
                String json = contactServiceForDetails(buildUniquePartOfUrl(productIds));
                ProductServiceResponse products = new Gson().fromJson(json, ProductServiceResponse.class);
                for (Product product : products.getProducts()) {
                    results.put(product.getId(), product);
                }

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
    String buildUniquePartOfUrl(Set<String> productIds) {
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

    String contactServiceForDetails(String uniquePartOfUrl) throws HttpClientErrorException {
        StringBuilder fullUrl = new StringBuilder();
        fullUrl.append(baseUrl);
        fullUrl.append('/');
        if (uniquePartOfUrl != null && uniquePartOfUrl.trim().length() > 0) {
            fullUrl.append(uniquePartOfUrl);
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
