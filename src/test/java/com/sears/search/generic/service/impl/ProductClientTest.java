package com.sears.search.generic.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.micro.services.search.bl.details.ProductClient;

public class ProductClientTest {

    @Test
    public void noProducts() {
        ProductClient pc = new ProductClient(null, "junit", true);
        List<String> pIds = new ArrayList<>();
        String result = pc.buildFullUrl(pIds);
        Assert.assertEquals("junit", result);
    }

    @Test
    public void oneProduct() {
        ProductClient pc = new ProductClient(null, "junit", true);
        List<String> pIds = new ArrayList<>();
        pIds.add("960");
        String result = pc.buildFullUrl(pIds);
        Assert.assertEquals("junit/960", result);
    }

    @Test
    public void twoProducts() {
        ProductClient pc = new ProductClient(null, "junit", true);
        List<String> pIds = new ArrayList<>();
        pIds.add("960");
        pIds.add("961");
        String result = pc.buildFullUrl(pIds);
        Assert.assertEquals("junit/960,961", result);
    }

}
