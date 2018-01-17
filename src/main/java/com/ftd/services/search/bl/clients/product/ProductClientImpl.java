package com.ftd.services.search.bl.clients.product;

import com.codahale.metrics.annotation.ExceptionMetered;
import com.codahale.metrics.annotation.Timed;
import com.ftd.services.product.api.domain.response.Product;
import com.ftd.services.product.api.domain.response.ProductServiceResponse;
import com.ftd.services.search.api.request.SearchServiceRequest;
import com.ftd.services.search.api.response.SearchServiceResponse;
import com.ftd.services.search.bl.clients.BaseClient;
import com.ftd.services.search.config.GlobalConstants;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.annotation.PostConstruct;
import javax.inject.Named;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * https://tools.publicis.sapient.com/confluence/display/FLTD/REST+EndPoint+Spec+-+ProductIdGroup+Service
 *
 * @author cdegreef
 */
@Named("productClient")
public class ProductClientImpl extends BaseClient implements ProductClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductClientImpl.class);
    public static final ProductServiceResponse FALLBACK_PRODUCT_RESPONSE = new ProductServiceResponse();
    public static final ProductServiceResponse DUMMY_PRODUCT_RESPONSE = new ProductServiceResponse();

    private RestTemplate restTemplate;
    private String baseUrl;

    @Value("${service.productService.enabled:true}")
    private boolean enabled = true;

    @Value("${service.productService.version:0.1}")
    private String version;

    public ProductClientImpl(
            @Autowired RestTemplate restTemplate,
            @Value("${service.productService.baseUrl}") String baseUrl) {

        this.restTemplate = restTemplate;
        this.baseUrl = baseUrl;
    }

    @PostConstruct
    public void logIdentification() {
        logIdentification(LOGGER, baseUrl, version, enabled);
    }

    /**
     * Find details returns a map of the products with the product id being the key.
     * This is done because the service can return the products in any order rather
     * than the specific order that we need for the search response. The map will
     * make it easier to process the results.
     *
     * @param response to be looked up
     * @return a map of productId/product pairs
     */
    public Map<String, Object> buildMap(ProductServiceResponse response) {
        /*
         * A list of documents for the provided productIds, respectively.
         */
        Map<String, Object> results = new HashMap<>();
        try {

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
        return results;
    }

    /**
     * Visibility for jUnit testing, otherwise consider to be private.
     *
     * @param productIds
     * @return
     */
    public String buildUniquePartOfUrl(Set<String> productIds) {
        /*
         * Create the url with a comma separated list, removing the last unneeded comma.
         */
        StringBuilder url = new StringBuilder();
        productIds.stream().forEach(id -> url.append(id).append(GlobalConstants.COMMA));
        if (url.length() == 0) {
            return StringUtils.EMPTY;
        }
        url.setLength(url.length() - 1);
        return url.toString();
    }

    @Override
    @Timed
    @ExceptionMetered
    @HystrixCommand(groupKey = "hystrixGroup",
            commandKey = "productServiceKey",
            threadPoolKey = "productThreadPoolKey",
            fallbackMethod = "callProductServiceFallback")
    @Cacheable(cacheNames = "product",
            key = "T(com.ftd.services.search.util.MiscUtil).getCacheKey(#searchServiceRequest)",
            condition = "#searchServiceRequest.from != T(com.ftd.services.search.api.request.From).INDEX",
            unless = "T(com.ftd.services.search.util.MiscUtil).isValidResponse(#result) == false")
    public ProductServiceResponse callProductService(
            SearchServiceRequest searchServiceRequest,
            SearchServiceResponse searchServiceResponse) throws HttpClientErrorException {

        if (!enabled) {
            return DUMMY_PRODUCT_RESPONSE;
        }
        String siteId = searchServiceRequest.getSiteId();
        final Set<String> productIds = new HashSet<>();
        productIds.add("960");
        String uniquePartOfUrl = buildUniquePartOfUrl(productIds);
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(baseUrl + GlobalConstants.FORWARD_SLASH)
                .queryParam(GlobalConstants.SITE_ID, siteId)
                .path(uniquePartOfUrl);

        HttpEntity<ProductServiceResponse> entity = new HttpEntity<>(createHttpHeaders(version));
        ResponseEntity<ProductServiceResponse> response = restTemplate.exchange(
                builder.build().encode().toUri(),
                HttpMethod.GET,
                entity,
                ProductServiceResponse.class);
        return response.getBody();
    }

    public ProductServiceResponse callProductServiceFallback(
            SearchServiceRequest searchServiceRequest,
            SearchServiceResponse searchServiceResponse) throws HttpClientErrorException {
        return FALLBACK_PRODUCT_RESPONSE;
    }

    public boolean isEnabled() {
        return enabled;
    }

}
