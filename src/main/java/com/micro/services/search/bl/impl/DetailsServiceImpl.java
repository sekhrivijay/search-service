package com.micro.services.search.bl.impl;

import java.util.HashSet;
import java.util.List;
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
    private static final long   DEFAULT_SERVICE_TIMEOUT_MILLIS = 10000;

    private static final Logger LOGGER                         = LoggerFactory.getLogger(DetailsServiceImpl.class);

    private AvailabilityClient  availabilityClient;
    private PricingClient       pricingClient;
    private ProductClient       productClient;

    private ExecutorService     executor                       = Executors.newCachedThreadPool();

    /*
     * This should always be > 0.
     */
    @Value("${service.detailsTimeout:1000}")
    private long                serviceTimeout                 = DEFAULT_SERVICE_TIMEOUT_MILLIS;

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
        LOGGER.info("service call timeout: {}", serviceTimeout);
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
         * At the end of this method the documents in this map will be updated with
         * details. The documents are references to what is in the
         * searchServiceResponse, so they too will be updated.
         */
        Map<String, Document> productMap = searchResultsAsProductIdMap(searchServiceResponse.getDocumentList());
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
        Future<Map<String, Object>> availableDetails = executor.submit(
                () -> availabilityClient.findDetails(productIds, startDate, endDate, zipCode));
        Future<Map<String, Object>> pricingDetails = executor.submit(
                () -> pricingClient.findDetails(productIds, siteId, memberId));
        Future<Map<String, Object>> productDetails = executor.submit(
                () -> productClient.findDetails(productIds));
        /*
         * Each future has a time limit. In the worst case, where all services time out,
         * the total time waiting will be the sum of the timeouts. Each set of details
         * is applied to the search results in sequence so that there is no contention.
         * This is instead of letting each future update the map itself.
         */
        applyDetailDocument("availability", availableDetails, productMap);
        applyDetailDocument("product", productDetails, productMap);
        applyDetailDocument("pricing", pricingDetails, productMap);
    }

    /**
     * Convert the search results to a map of productKey->document. The document is
     * a map of attribute names and values.
     *
     * @param documentList
     * @return
     */
    Map<String, Document> searchResultsAsProductIdMap(List<Document> documentList) {
        return documentList.stream().collect(Collectors.toMap(doc -> (String) doc.getRecord().get("id"), doc -> doc));
    }

    /**
     * This method will add new elements to the search service attribute map. The
     * prefix provides a way to ensure uniqueness across all services.
     *
     * @param prefix
     *            is the first node of the attribute name, distinct for each service
     * @param detailService
     *            is the service call that was issued in parallel
     * @param searchServiceProductDocuments
     *            is the search service attribute map, one entry per product id
     */
    void applyDetailDocument(
            String prefix,
            Future<Map<String, Object>> detailService,
            Map<String, Document> searchServiceProductDocuments) {
        try {
            /*
             * A future stores the result of the call in the "get" method. This will timeout
             * if the service does not return within the expected timeout period.
             */
            Map<String, Object> detailsByProduct = detailService.get(serviceTimeout, TimeUnit.MILLISECONDS);
            if (detailService.isCancelled()) {
                throw new Exception("parallel task was cancelled");
            }
            if (detailsByProduct == null || detailsByProduct.isEmpty()) {
                throw new Exception("service returned nothing");
            }
            /*
             * The detailsMap provided by the service call is a map of productIds and the
             * document associated with them has a map of attributeName and value.
             */
            detailsByProduct.keySet().stream().forEach(
                    productId -> addServiceResponseToSearchDocument(
                            prefix,
                            detailsByProduct.get(productId),
                            searchServiceProductDocuments.get(productId)));

        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("applying {} detected an error: {}", prefix, e.getMessage());
        }
    }

    void addServiceResponseToSearchDocument(String tag, Object clientObject, Document targetDocument) {

        assert tag != null : "tag can not be null";

        if (clientObject == null || targetDocument == null) {
            return;
        }
        Map<String, Object> target = targetDocument.getRecord();
        if (target == null) {
            return;
        }
        target.put(tag, clientObject);
    }
}
