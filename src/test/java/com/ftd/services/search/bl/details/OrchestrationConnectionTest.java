package com.ftd.services.search.bl.details;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.ftd.services.search.ServiceApplication;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ServiceApplication.class)
public class OrchestrationConnectionTest {

    @Autowired
    private ProductClient      productClient;

    @Autowired
    private PricingClient      pricingClient;

    @Autowired
    private AvailabilityClient availabilityClient;

    @Test
    public void productService() {
        if (productClient.isEnabled()) {
            productClient.contactServiceForDetails("notaproduct");
        }
    }

    @Test
    public void pricingService() {
        if (pricingClient.isEnabled()) {
            pricingClient.contactServiceForDetails("notaproduct");
        }
    }

    @Test
    public void availabilityService() throws Exception {
        if (availabilityClient.isEnabled()) {
            availabilityClient.contactServiceForDetails("notaproduct");
        }
    }

}
