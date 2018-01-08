package com.sears.search.generic.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.micro.services.search.bl.details.PricingClient;

public class PricingClientTest {

    @Test
    public void noProducts() {
        PricingClient pc = new PricingClient(null, "junit", true);
        List<String> pIds = new ArrayList<>();
        String result = pc.buildFullUrl(pIds, "site1", "memberType1");
        Assert.assertEquals("junit?site=site1&memberType=memberType1", result);
    }

    @Test
    public void oneProduct() {
        PricingClient pc = new PricingClient(null, "junit", true);
        List<String> pIds = new ArrayList<>();
        pIds.add("960");
        String result = pc.buildFullUrl(pIds, "site1", "memberType1");
        Assert.assertEquals("junit/960?site=site1&memberType=memberType1", result);
    }

    @Test
    public void twoProducts() {
        PricingClient pc = new PricingClient(null, "junit", true);
        List<String> pIds = new ArrayList<>();
        pIds.add("960");
        pIds.add("961");
        String result = pc.buildFullUrl(pIds, "site1", "memberType1");
        Assert.assertEquals("junit/960,961?site=site1&memberType=memberType1", result);
    }

}
