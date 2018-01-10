package com.micro.services.search.bl.impl;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.micro.services.search.api.request.SearchServiceRequest;
import com.micro.services.search.api.response.Document;
import com.micro.services.search.api.response.SearchServiceResponse;
import com.micro.services.search.bl.DetailsService;
import com.micro.services.search.bl.details.AvailabilityClient;
import com.micro.services.search.bl.details.PricingClient;
import com.micro.services.search.bl.details.ProductClient;

@Service("detailsService")
public class DetailsServiceImpl implements DetailsService {

    private static final Logger LOGGER   = LoggerFactory.getLogger(DetailsServiceImpl.class);

    private AvailabilityClient  availabilityClient;
    private PricingClient       pricingClient;
    private ProductClient       productClient;

    private ExecutorService     executor = Executors.newCachedThreadPool();

    @Value("${service.availabilityService.timeout:1000}")
    private long                availabilityServiceTimeout;

    @Value("${service.pricingService.timeout:1000}")
    private long                pricingServiceTimeout;

    @Value("${service.productService.timeout:1000}")
    private long                productServiceTimeout;

    public DetailsServiceImpl(
            @Autowired AvailabilityClient availabilityClient,
            @Autowired ProductClient productClient,
            @Autowired PricingClient pricingClient) {
        this.availabilityClient = availabilityClient;
        this.pricingClient = pricingClient;
        this.productClient = productClient;
    }

    @PostConstruct
    private void logIdentification() {
        LOGGER.info("service call timeouts: availability: {}, product: {}, pricing: {}",
                availabilityServiceTimeout, productServiceTimeout, pricingServiceTimeout);
    }

    @Override
    public void postQueryDetails(
            SearchServiceRequest searchServiceRequest,
            SearchServiceResponse searchServiceResponse) {

        // LOGGER.debug("orchestrating search service response details");
        if (searchServiceResponse == null
                || searchServiceResponse.getDocumentList() == null) {
            return;
        }
        /*
         * Convert the search results to a map of productKey->document. The document is
         * a map of attribute names and values. At the end of this method the documents
         * in this map will be updated with details. The documents are references to
         * what is in the searchServiceResponse, so they too will be updated.
         */
        Map<String, Document> productMap = searchServiceResponse.getDocumentList().stream()
                .collect(Collectors.toMap(doc -> doc.getRecord().get("id"), doc -> doc));
        /*
         * Create a list of product ids that will be used in the detail services.
         */
        // Set<String> productIds = productMap.keySet();
        // TODO remove this test code
        final Set<String> productIds = new HashSet<String>();
        productIds.add("960");
        /*
         * Get orchestration parameters from the request URL
         */
        String startDate = searchServiceRequest.getAvailFrom();
        String endDate = searchServiceRequest.getAvailTo();
        String zipCode = searchServiceRequest.getZipCode();
        String siteId = searchServiceRequest.getSiteId();
        String memberId = searchServiceRequest.getMemberType();
        /*
         * Gather all details in parallel
         */
        Future<Map<String, Document>> availableDetails = executor.submit(
                () -> availabilityClient.findDetails(productIds, startDate, endDate, zipCode));
        Future<Map<String, Document>> pricingDetails = executor.submit(
                () -> pricingClient.findDetails(productIds, siteId, memberId));
        Future<Map<String, Document>> productDetails = executor.submit(
                () -> productClient.findDetails(productIds));
        /*
         * Each future has a time limit. In the worst case, where all services time out,
         * the total time waiting will be the sum of the timeouts. Each set of details
         * is applied to the search results in sequence so that there is no contention.
         * This is instead of letting each future update the map itself.
         */
        try {
            applyAvailabilityDocument(
                    availableDetails.get(availabilityServiceTimeout, TimeUnit.MILLISECONDS),
                    productMap);
        } catch (Exception e) {
            LOGGER.error("availabilityService error {}", e.getMessage());
        }
        try {
            applyProductDocument(
                    productDetails.get(productServiceTimeout, TimeUnit.MILLISECONDS),
                    productMap);
        } catch (Exception e) {
            LOGGER.error("productService error {}", e.getMessage());
        }
        try {
            applyPricingDocument(
                    pricingDetails.get(pricingServiceTimeout, TimeUnit.MILLISECONDS),
                    productMap);
        } catch (Exception e) {
            LOGGER.error("pricingService error {}", e.getMessage());
        }
    }

    void applyAvailabilityDocument(Map<String, Document> detailsMap, Map<String, Document> productMap) {
        LOGGER.debug("applyAvailabilityDocument size={}", detailsMap.size());
        // TODO
    }

    void applyPricingDocument(Map<String, Document> detailsMap, Map<String, Document> productMap) {
        LOGGER.debug("applyPricingDocument size={}", detailsMap.size());
        // TODO
    }

    void applyProductDocument(Map<String, Document> detailsMap, Map<String, Document> productMap) {
        LOGGER.debug("applyProductDocument size={}", detailsMap.size());
        // TODO
    }
}
