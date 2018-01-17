package com.ftd.services.search.bl.clients.price;

import com.ftd.services.search.api.request.SearchServiceRequest;
import com.ftd.services.search.api.response.SearchServiceResponse;
import org.springframework.web.client.HttpClientErrorException;

import java.util.Map;

public interface PriceClient {
    PricingServiceResponse
        callPriceService(SearchServiceRequest searchServiceRequest,
                         SearchServiceResponse searchServiceResponse) throws HttpClientErrorException;

    Map<String, Object> buildMap(PricingServiceResponse products);
}
