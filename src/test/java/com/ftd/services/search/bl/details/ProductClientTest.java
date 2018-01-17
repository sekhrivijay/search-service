package com.ftd.services.search.bl.details;

import java.util.HashSet;
import java.util.Set;

import com.ftd.services.search.bl.clients.product.ProductClientImpl;
import org.junit.Assert;
import org.junit.Test;

public class ProductClientTest {

    @Test
    public void noProducts() {
        ProductClientImpl pc = new ProductClientImpl(null, "junit");
        Set<String> pIds = new HashSet<>();
        String result = pc.buildUniquePartOfUrl(pIds);
        Assert.assertEquals("", result);
    }

    @Test
    public void oneProduct() {
        ProductClientImpl pc = new ProductClientImpl(null, "junit");
        Set<String> pIds = new HashSet<>();
        pIds.add("960");
        String result = pc.buildUniquePartOfUrl(pIds);
        Assert.assertEquals("960", result);
    }

    @Test
    public void twoProducts() {
        ProductClientImpl pc = new ProductClientImpl(null, "junit");
        Set<String> pIds = new HashSet<>();
        pIds.add("960");
        pIds.add("961");
        String result = pc.buildUniquePartOfUrl(pIds);
        Assert.assertEquals("960,961", result);
    }

}
