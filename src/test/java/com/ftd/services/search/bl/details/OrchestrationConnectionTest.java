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
    private ProductClient productClient;

    @Autowired
    private PricingClient pricingClient;

    @Autowired
    private AvailabilityClient availabilityClient;

    @Test
    public void productService() {
        productClient.contactServiceForDetails("notaproduct");
    }

    @Test
    public void pricingService() {
        pricingClient.contactServiceForDetails("notaproduct");
    }

    @Test
    public void availabilityService() throws Exception {
        availabilityClient.contactServiceForDetails("notaproduct");
    }

}
