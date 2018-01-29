package com.ftd.services.search.bl.clients.price;

import com.codahale.metrics.annotation.ExceptionMetered;
import com.codahale.metrics.annotation.Timed;
import com.ftd.services.pricing.api.domain.response.Pricing;
import com.ftd.services.pricing.api.domain.response.PricingResponse;
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

import javax.annotation.PostConstruct;
import javax.inject.Named;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * https://tools.publicis.sapient.com/confluence/display/FLTD/REST+EndPoint+Spec+-+Pricing+Service
 *
 * @author cdegreef
 */
@Named("pricingClient")
public class PricingClientImpl extends BaseClient implements PricingClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(PricingClientImpl.class);
    public static final PricingResponse FALLBACK_PRICE_RESPONSE = new PricingResponse();
    public static final PricingResponse DUMMY_PRICE_RESPONSE = new PricingResponse();

    private RestTemplate restTemplate;
    private String baseUrl;

    @Value("${service.pricingService.enabled:true}")
    private boolean enabled = true;

    @Value("${service.pricingService.version:0.1}")
    private String version;

    public PricingClientImpl(
            @Autowired RestTemplate restTemplate,
            @Value("${service.pricingService.baseUrl}") String baseUrl) {

        this.restTemplate = restTemplate;
        this.baseUrl = baseUrl;
    }

    @PostConstruct
    void logIdentification() {
        logIdentification(LOGGER, baseUrl, version, enabled);
    }

    public Map<String, Pricing> buildMap(PricingResponse pricingResponse) {
        /*
         * A list of documents for the provided productIds, respectively.
         */
        Map<String, Pricing> results = new HashMap<>();
        /*
         * The JSON string is an array of "product" instances. We want to figure out the
         * id for each of them and put the id as the key with the product object being
         * the value.
         */
        if (pricingResponse != null && pricingResponse.getPricing() != null) {
            results = pricingResponse.getPricing()
                    .stream()
                    .filter(Objects::nonNull)
                    .filter(e -> StringUtils.isNotEmpty(e.getId()))
                    .collect(Collectors.toMap(Pricing::getId, e -> e));
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
        productIds.forEach(id -> url.append(id).append(GlobalConstants.COMMA));
        if (url.length() > 0) {
            url.setLength(url.length() - 1);
        }

//        url.append(GlobalConstants.SITE_ID_PARAM).append(siteId);
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
    @Cacheable(cacheNames = "pricing",
            key = "T(com.ftd.services.search.bl.clients.MiscUtil).getCacheKey(#searchServiceRequest)",
            condition = "#searchServiceRequest.from != T(com.ftd.services.search.api.request.From).INDEX",
            unless = "T(com.ftd.services.search.bl.clients.MiscUtil).isValidResponse(#result) == false")
    public PricingResponse callPriceService(
            SearchServiceRequest searchServiceRequest,
            SearchServiceResponse searchServiceResponse) throws HttpClientErrorException {

        if (!enabled) {
            return DUMMY_PRICE_RESPONSE;
        }
        try {
            String siteId = searchServiceRequest.getSiteId();
            String memberId = searchServiceRequest.getMemberType();
            Set<String> productIds = getPids(searchServiceResponse);
            String uniquePartOfUrl = buildUniquePartOfUrl(productIds, siteId, memberId);
            StringBuilder fullUrl = new StringBuilder();
            fullUrl.append(baseUrl.replace(GlobalConstants.SITE_ID, siteId));
            fullUrl.append(GlobalConstants.FORWARD_SLASH);
            if (uniquePartOfUrl != null && uniquePartOfUrl.trim().length() > 0) {
                fullUrl.append(uniquePartOfUrl);
            }
            HttpEntity<PricingResponse> entity = new HttpEntity<>(createHttpHeaders(version));
            LOGGER.info("calling pricing service " + fullUrl.toString() + " headers " + createHttpHeaders(version));
            ResponseEntity<PricingResponse> response = restTemplate.exchange(
                    fullUrl.toString(), HttpMethod.GET, entity, PricingResponse.class);
            return response.getBody();
        } catch (Exception e) {
            LOGGER.error("pricing error ", e);
            throw e;
        }
    }

    public PricingResponse callPriceServiceFallback(
            SearchServiceRequest searchServiceRequest,
            SearchServiceResponse searchServiceResponse) throws HttpClientErrorException {
        return FALLBACK_PRICE_RESPONSE;
    }

    public boolean isEnabled() {
        return enabled;
    }
}
