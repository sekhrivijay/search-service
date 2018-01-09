package com.micro.services.search.bl.details;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

/**
 * https://tools.publicis.sapient.com/confluence/display/FLTD/REST+EndPoint+Spec+-+Pricing+Service
 *
 * @author cdegreef
 *
 */
@Named("pricingClient")
public class PricingClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(PricingClient.class);

    private RestTemplate        restTemplate;
    private boolean             enabled;
    private String              baseUrl;

    public PricingClient(
            @Autowired RestTemplate restTemplate,
            @Value("${service.pricingService.baseUrl}") String baseUrl,
            @Value("${service.pricingService.enabled:false}") boolean enabled) {

        this.restTemplate = restTemplate;
        this.baseUrl = baseUrl;
        this.enabled = enabled;

        if (!isEnabled()) {
            LOGGER.warn("the pricing service is NOT enabled");
        }
    }

    public List<DetailsDocument> findDetails(
            List<String> productIds,
            String siteId,
            String memberType) {
        /*
         * A list of documents for the provided productIds, respectively.
         */
        List<DetailsDocument> results = new ArrayList<>(productIds.size());
        if (isEnabled()) {
            try {
                contactServiceForDetails(buildFullUrl(productIds, siteId, memberType));
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
     * @param siteId
     * @param memberType
     * @return
     */
    String buildFullUrl(List<String> productIds, String siteId, String memberType) {
        /*
         * Create the url with a comma separated list, removing the last unneeded comma.
         */
        StringBuilder url = new StringBuilder();
        productIds.stream().forEach(id -> url.append(id).append(','));
        if (url.length() > 0) {
            url.setLength(url.length() - 1);
        }

        url.append("?site=").append(siteId);

        if (memberType != null && memberType.trim().length() > 0) {
            url.append("&memberType=").append(memberType);
        }

        return url.toString();
    }

    String contactServiceForDetails(String url) throws HttpClientErrorException {
        StringBuilder fullUrl = new StringBuilder();
        fullUrl.append(baseUrl);
        fullUrl.append('/');
        if (url != null && url.trim().length() > 0) {
            fullUrl.append(url);
        }
        ResponseEntity<String> response = restTemplate.getForEntity(fullUrl.toString(), String.class);
        // LOGGER.debug("Calling {} results: {}", fullUrl.toString(), new
        // Gson().toJson(response));
        return response.getBody();
    }

    private boolean isEnabled() {
        return enabled;
    }
}
