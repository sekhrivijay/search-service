package com.ftd.services.search.bl.details;

import java.util.HashSet;
import java.util.Set;

import com.ftd.services.search.bl.clients.price.PriceClientImpl;
import org.junit.Assert;
import org.junit.Test;

public class PriceClientTest {

    @Test
    public void noProducts() {
        PriceClientImpl pc = new PriceClientImpl(null, "junit");
        Set<String> pIds = new HashSet<>();
        String result = pc.buildUniquePartOfUrl(pIds, "site1", "memberType1");
        Assert.assertEquals("?siteId=site1&memberType=memberType1", result);
    }

    @Test
    public void oneProduct() {
        PriceClientImpl pc = new PriceClientImpl(null, "junit");
        Set<String> pIds = new HashSet<>();
        pIds.add("960");
        String result = pc.buildUniquePartOfUrl(pIds, "site1", "memberType1");
        Assert.assertEquals("960?siteId=site1&memberType=memberType1", result);
    }

    @Test
    public void twoProducts() {
        PriceClientImpl pc = new PriceClientImpl(null, "junit");
        Set<String> pIds = new HashSet<>();
        pIds.add("960");
        pIds.add("961");
        String result = pc.buildUniquePartOfUrl(pIds, "site1", "memberType1");
        Assert.assertEquals("960,961?siteId=site1&memberType=memberType1", result);
    }

}
