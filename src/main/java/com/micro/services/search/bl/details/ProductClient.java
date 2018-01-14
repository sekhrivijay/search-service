package com.micro.services.search.bl.details;

import java.awt.Dimension;
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

    /**
     * Find details returns a map of the products with the product id being the key.
     * This is done because the service can return the products in any order rather
     * than the specific order that we need for the search response. The map will
     * make it easier to process the results.
     *
     * @param productIds
     *            to be looked up
     * @return a map of productId/product pairs
     */
    public Map<String, Object> findDetails(Set<String> productIds) {
        /*
         * A list of documents for the provided productIds, respectively.
         */
        Map<String, Object> results = new HashMap<>(productIds.size());
        if (isEnabled()) {

            try {
                /*
                 * Ask the service in one request for all of the products that are needed.
                 */
                ProductServiceResponse response = contactServiceForDetails(buildUniquePartOfUrl(productIds));
                /*
                 * The service may return a null body if none of the product ids were found.
                 */
                if (response != null) {
                    for (Product product : response.getProducts()) {
                        /*
                         * Add each product to the map.
                         */
                        results.put(product.getId(), product);
                    }
                }

            } catch (HttpClientErrorException e) {
                /*
                 * It is likely that the service may set an return code other than 200 if there
                 * were no products found. When we find out what this error is we may want to
                 * handle it special rather than produce a warning.
                 */
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

    ProductServiceResponse contactServiceForDetails(String uniquePartOfUrl) throws HttpClientErrorException {
        StringBuilder fullUrl = new StringBuilder();
        fullUrl.append(baseUrl);
        fullUrl.append('/');
        if (uniquePartOfUrl != null && uniquePartOfUrl.trim().length() > 0) {
            fullUrl.append(uniquePartOfUrl);
        }
        fullUrl.append("&dim={1}");

        Dimension dm = new Dimension(1, 2);

        HttpEntity<ProductServiceResponse> entity = new HttpEntity<>(createHttpHeaders());
        ResponseEntity<ProductServiceResponse> response = restTemplate.exchange(
                fullUrl.toString(), HttpMethod.GET, entity, ProductServiceResponse.class, dm);
        return response.getBody();
    }

    private boolean isEnabled() {
        return enabled;
    }
}
