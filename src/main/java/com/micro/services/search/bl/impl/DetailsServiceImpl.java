package com.micro.services.search.bl.impl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final Logger LOGGER = LoggerFactory.getLogger(DetailsServiceImpl.class);

    private AvailabilityClient  availability;
    private PricingClient       pricing;
    private ProductClient       product;

    public AvailabilityClient getAvailability() {
        return availability;
    }

    public PricingClient getPricing() {
        return pricing;
    }

    public ProductClient getProduct() {
        return product;
    }

    @Override
    public void postQueryDetails(
            SearchServiceRequest searchServiceRequest,
            SearchServiceResponse searchServiceResponse) {

        LOGGER.debug("orchestrating search service response details");

        List<String> productIds = new ArrayList<>();
        /*
         * TODO extract the product ids from the response.
         */
        productIds.add("960");
        /*
         * TODO got these parameters from the request.  First, add the variables to the request
         */
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = LocalDate.now();
        String zipCode = "60532";
        String siteId = "1";
        String memberId = "2";
        /*
         * TODO make this parallel and then apply the responses.
         */
        List<DetailsDocument> availabilityList = getAvailability().findDetails(productIds, startDate, endDate, zipCode);
        List<DetailsDocument> pricingList = getPricing().findDetails(productIds, siteId, memberId);
        List<DetailsDocument> productList = getProduct().findDetails(productIds);
        /*
         * TODO update the searchServiceResponse documents with the details documents.
         */
    }

    @Inject
    public void setAvailability(AvailabilityClient availability) {
        this.availability = availability;
    }

    @Inject
    public void setPricing(PricingClient pricing) {
        this.pricing = pricing;
    }

    @Inject
    public void setProduct(ProductClient product) {
        this.product = product;
    }
}
