package com.ftd.services.search.bl.clients.price;

import com.ftd.services.pricing.api.domain.response.PricingResponse;
import com.ftd.services.search.api.request.SearchServiceRequest;
import com.ftd.services.search.api.response.SearchServiceResponse;
import org.springframework.web.client.HttpClientErrorException;

import java.util.Map;

public interface PricingClient {
    PricingResponse
    callPriceService(SearchServiceRequest searchServiceRequest,
                     SearchServiceResponse searchServiceResponse) throws HttpClientErrorException;

    Map<String, Object> buildMap(PricingResponse products);
}
