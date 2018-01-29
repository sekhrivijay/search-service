package com.ftd.services.search.bl.details;

import java.util.HashSet;
import java.util.Set;

import com.ftd.services.search.bl.clients.price.PricingClientImpl;
import org.junit.Assert;
import org.junit.Test;

public class PricingClientTest {

    @Test
    public void noProducts() {
        PricingClientImpl pc = new PricingClientImpl(null, "junit");
        Set<String> pIds = new HashSet<>();
        String result = pc.buildUniquePartOfUrl(pIds, "site1", "memberType1");
        Assert.assertEquals("?&memberType=memberType1", result);
    }

    @Test
    public void oneProduct() {
        PricingClientImpl pc = new PricingClientImpl(null, "junit");
        Set<String> pIds = new HashSet<>();
        pIds.add("960");
        String result = pc.buildUniquePartOfUrl(pIds, "site1", "memberType1");
        Assert.assertEquals("960?&memberType=memberType1", result);
    }

    @Test
    public void twoProducts() {
        PricingClientImpl pc = new PricingClientImpl(null, "junit");
        Set<String> pIds = new HashSet<>();
        pIds.add("960");
        pIds.add("961");
        String result = pc.buildUniquePartOfUrl(pIds, "site1", "memberType1");
        Assert.assertEquals("960,961?&memberType=memberType1", result);
    }

}
