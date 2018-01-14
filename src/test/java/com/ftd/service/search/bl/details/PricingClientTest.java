package com.ftd.service.search.bl.details;

import java.util.HashSet;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;

public class PricingClientTest {

    @Test
    public void noProducts() {
        PricingClient pc = new PricingClient(null, "junit");
        Set<String> pIds = new HashSet<>();
        String result = pc.buildUniquePartOfUrl(pIds, "site1", "memberType1");
        Assert.assertEquals("?site=site1&memberType=memberType1", result);
    }

    @Test
    public void oneProduct() {
        PricingClient pc = new PricingClient(null, "junit");
        Set<String> pIds = new HashSet<>();
        pIds.add("960");
        String result = pc.buildUniquePartOfUrl(pIds, "site1", "memberType1");
        Assert.assertEquals("960?site=site1&memberType=memberType1", result);
    }

    @Test
    public void twoProducts() {
        PricingClient pc = new PricingClient(null, "junit");
        Set<String> pIds = new HashSet<>();
        pIds.add("960");
        pIds.add("961");
        String result = pc.buildUniquePartOfUrl(pIds, "site1", "memberType1");
        Assert.assertEquals("960,961?site=site1&memberType=memberType1", result);
    }

}
