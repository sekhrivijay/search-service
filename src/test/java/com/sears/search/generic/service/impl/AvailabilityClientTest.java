package com.sears.search.generic.service.impl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.micro.services.search.bl.details.AvailabilityClient;

public class AvailabilityClientTest {

    @Test
    public void noProducts() {
        AvailabilityClient pc = new AvailabilityClient(null, "junit", true);
        List<String> pIds = new ArrayList<>();
        String result = pc.buildFullUrl(pIds, LocalDate.now(), LocalDate.now(), "60532");
        Assert.assertEquals("junit", result);
    }

    @Test
    public void oneProduct() {
        AvailabilityClient pc = new AvailabilityClient(null, "junit", true);
        List<String> pIds = new ArrayList<>();
        pIds.add("960");
        String result = pc.buildFullUrl(pIds, LocalDate.now(), LocalDate.now(), "60532");
        Assert.assertEquals("junit/", result);
    }

    @Test
    public void twoProducts() {
        AvailabilityClient pc = new AvailabilityClient(null, "junit", true);
        List<String> pIds = new ArrayList<>();
        pIds.add("960");
        pIds.add("961");
        String result = pc.buildFullUrl(pIds, LocalDate.now(), LocalDate.now(), "60532");
        Assert.assertEquals("junit/", result);
    }

}
