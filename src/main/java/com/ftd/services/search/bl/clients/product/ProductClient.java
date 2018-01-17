package com.ftd.services.search.bl.clients.product;

import com.ftd.services.product.api.domain.response.ProductServiceResponse;
import com.ftd.services.search.api.request.SearchServiceRequest;
import com.ftd.services.search.api.response.SearchServiceResponse;
import org.springframework.web.client.HttpClientErrorException;

import java.util.Map;

public interface ProductClient {
    ProductServiceResponse callProductService(
            SearchServiceRequest searchServiceRequest,
            SearchServiceResponse searchServiceResponse) throws HttpClientErrorException;

    Map<String, Object> buildMap(ProductServiceResponse response);
}
