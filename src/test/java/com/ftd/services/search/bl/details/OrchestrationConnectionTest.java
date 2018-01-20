package com.ftd.services.search.bl.details;

import com.ftd.services.search.api.request.SearchServiceRequest;
import com.ftd.services.search.api.response.Document;
import com.ftd.services.search.api.response.SearchServiceResponse;
import com.ftd.services.search.bl.clients.availibility.AvailabilityClientImpl;
import com.ftd.services.search.bl.clients.price.PricingClientImpl;
import com.ftd.services.search.bl.clients.product.ProductClientImpl;
import com.ftd.services.search.config.GlobalConstants;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.ftd.services.search.ServiceApplication;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ServiceApplication.class)
public class OrchestrationConnectionTest {

    private SearchServiceRequest searchServiceRequest;
    private SearchServiceResponse searchServiceResponse;

    @Autowired
    private ProductClientImpl productClient;

    @Autowired
    private PricingClientImpl pricingClient;

    @Autowired
    private AvailabilityClientImpl availabilityClient;

    @Before
    public void setup() {
        Document document = new Document();
        Map<String, Object> map = new HashMap<>();
        map.put(GlobalConstants.ID, "960");
        document.setRecord(map);
        searchServiceRequest = SearchServiceRequest
                .SearchServiceRequestBuilder
                .aSearchServiceRequest()
                .withSiteId(GlobalConstants.FTD)
                .build();
        searchServiceResponse = SearchServiceResponse
                .SearchServiceResponseBuilder
                .aSearchServiceResponse()
                .withDocumentList(Arrays.asList(document))
                .build();
    }

    @Test
    public void productService() {
        if (productClient.isEnabled()) {
            productClient.callProductService(searchServiceRequest, searchServiceResponse);
        }
    }

    @Test
    public void pricingService() {
        if (pricingClient.isEnabled()) {
            pricingClient.callPriceService(searchServiceRequest, searchServiceResponse);
        }
    }

    @Test
    public void availabilityService() throws Exception {
        if (availabilityClient.isEnabled()) {
            availabilityClient.callAvailabilityService(searchServiceRequest, searchServiceResponse);
        }
    }

}
