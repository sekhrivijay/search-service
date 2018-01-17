package com.ftd.services.search.bl.clients.price;

import com.codahale.metrics.annotation.ExceptionMetered;
import com.codahale.metrics.annotation.Timed;
import com.ftd.services.product.api.domain.response.Product;
import com.ftd.services.search.api.request.SearchServiceRequest;
import com.ftd.services.search.api.response.SearchServiceResponse;
import com.ftd.services.search.bl.clients.BaseClient;
import com.ftd.services.search.config.GlobalConstants;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import javax.inject.Named;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * https://tools.publicis.sapient.com/confluence/display/FLTD/REST+EndPoint+Spec+-+Pricing+Service
 *
 * @author cdegreef
 */
@Named("pricingClient")
public class PriceClientImpl extends BaseClient implements PriceClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(PriceClientImpl.class);
    public static final PricingServiceResponse FALLBACK_PRICE_RESPONSE = new PricingServiceResponse();
    public static final PricingServiceResponse DUMMY_PRICE_RESPONSE = new PricingServiceResponse();

    private RestTemplate restTemplate;
    private String baseUrl;

    @Value("${service.pricingService.enabled:true}")
    private boolean enabled = true;

    @Value("${service.pricingService.version:0.1}")
    private String version;

    public PriceClientImpl(
            @Autowired RestTemplate restTemplate,
            @Value("${service.pricingService.baseUrl}") String baseUrl) {

        this.restTemplate = restTemplate;
        this.baseUrl = baseUrl;
    }

    @PostConstruct
    void logIdentification() {
        logIdentification(LOGGER, baseUrl, version, enabled);
    }

    public Map<String, Object> buildMap(PricingServiceResponse products) {
        /*
         * A list of documents for the provided productIds, respectively.
         */
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
        } catch (HttpClientErrorException e) {
            LOGGER.warn("{}", e.getMessage());
        }
        return results;
    }

    /**
     * Visibility for jUnit testing, otherwise consider to be private.
     *
     * @param productIds
     * @param siteId
     * @param memberType
     * @return
     */
    public String buildUniquePartOfUrl(Set<String> productIds, String siteId, String memberType) {
        /*
         * Create the url with a comma separated list, removing the last unneeded comma.
         */
        StringBuilder url = new StringBuilder();
        productIds.stream().forEach(id -> url.append(id).append(','));
        if (url.length() > 0) {
            url.setLength(url.length() - 1);
        }

        url.append(GlobalConstants.SITE_ID_PARAM).append(siteId);

        if (memberType != null && memberType.trim().length() > 0) {
            url.append(GlobalConstants.MEMBER_TYPE_PARAM).append(memberType);
        }

        return url.toString();
    }


    @Override
    @Timed
    @ExceptionMetered
    @HystrixCommand(groupKey = "hystrixGroup",
            commandKey = "priceServiceKey",
            threadPoolKey = "priceThreadPoolKey",
            fallbackMethod = "callPriceServiceFallback")
    public PricingServiceResponse callPriceService(
            SearchServiceRequest searchServiceRequest,
            SearchServiceResponse searchServiceResponse) throws HttpClientErrorException {

        if (!enabled) {
            return DUMMY_PRICE_RESPONSE;
        }
        String siteId = searchServiceRequest.getSiteId();
        String memberId = searchServiceRequest.getMemberType();
//        final Set<String> productIds = new HashSet<String>();
//        productIds.add("960");
        Set<String> productIds = getPids(searchServiceResponse);
        String uniquePartOfUrl = buildUniquePartOfUrl(productIds, siteId, memberId);
        StringBuilder fullUrl = new StringBuilder();
        fullUrl.append(baseUrl);
        fullUrl.append(GlobalConstants.FORWARD_SLASH);
        if (uniquePartOfUrl != null && uniquePartOfUrl.trim().length() > 0) {
            fullUrl.append(uniquePartOfUrl);
        }
        HttpEntity<PricingServiceResponse> entity = new HttpEntity<>(createHttpHeaders(version));
        ResponseEntity<PricingServiceResponse> response = restTemplate.exchange(
                fullUrl.toString(), HttpMethod.GET, entity, PricingServiceResponse.class);
        return response.getBody();
    }

    public PricingServiceResponse callPriceServiceFallback(
            SearchServiceRequest searchServiceRequest,
            SearchServiceResponse searchServiceResponse) throws HttpClientErrorException {
        return FALLBACK_PRICE_RESPONSE;
    }

    public boolean isEnabled() {
        return enabled;
    }
}
