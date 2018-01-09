package com.micro.services.search.bl.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.micro.services.search.api.request.SearchServiceRequest;
import com.micro.services.search.api.response.SearchServiceResponse;
import com.micro.services.search.bl.DetailsService;
import com.micro.services.search.bl.details.AvailabilityClient;
import com.micro.services.search.bl.details.DetailsDocument;
import com.micro.services.search.bl.details.PricingClient;
import com.micro.services.search.bl.details.ProductClient;

@Service("detailsService")
public class DetailsServiceImpl implements DetailsService {
    private static final Logger LOGGER   = LoggerFactory.getLogger(DetailsServiceImpl.class);

    private AvailabilityClient  availabilityClient;
    private PricingClient       pricingClient;
    private ProductClient       productClient;

    private ExecutorService     executor = Executors.newCachedThreadPool();

    @Value("${availabilityService.timeout:1000}")
    private long                availabilityServiceTimeout;

    @Value("${pricingService.timeout:1000}")
    private long                pricingServiceTimeout;

    @Value("${productService.timeout:1000}")
    private long                productServiceTimeout;

    public DetailsServiceImpl(
            @Autowired AvailabilityClient availabilityClient,
            @Autowired ProductClient productClient,
            @Autowired PricingClient pricingClient) {
        this.availabilityClient = availabilityClient;
        this.pricingClient = pricingClient;
        this.productClient = productClient;
    }

    @Override
    public void postQueryDetails(
            SearchServiceRequest searchServiceRequest,
            SearchServiceResponse searchServiceResponse) {

        // LOGGER.debug("orchestrating search service response details");

        List<String> productIds = new ArrayList<>();
        /*
         * TODO extract the product ids from the response.
         */
        productIds.add("960");
        /*
         * TODO got these parameters from the request. First, add the variables to the
         * request
         */
        String startDate = searchServiceRequest.getAvailFrom();
        String endDate = searchServiceRequest.getAvailTo();
        String zipCode = searchServiceRequest.getZipCode();
        String siteId = searchServiceRequest.getSiteId();
        String memberId = searchServiceRequest.getMemberType();
        /*
         * Gather all details in parallel
         */
        Future<List<DetailsDocument>> availableDetails = executor.submit(
                () -> availabilityClient.findDetails(productIds, startDate, endDate, zipCode));
        Future<List<DetailsDocument>> pricingDetails = executor.submit(
                () -> pricingClient.findDetails(productIds, siteId, memberId));
        Future<List<DetailsDocument>> productDetails = executor.submit(
                () -> productClient.findDetails(productIds));
        /*
         * Each future has a time limit. In the worst case, where all services time out,
         * the total time waiting will be the sum of the timeouts.
         */
        try {
            availableDetails.get(availabilityServiceTimeout, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            LOGGER.error("availabilityService error {}", e.getMessage());
        }
        try {
            productDetails.get(productServiceTimeout, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            LOGGER.error("productService error {}", e.getMessage());
        }
        try {
            pricingDetails.get(pricingServiceTimeout, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            LOGGER.error("pricingService error {}", e.getMessage());
        }

        /*
         * TODO update the searchServiceResponse documents with the details documents.
         */

    }
}
