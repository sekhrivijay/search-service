package com.sears.search.generic.service.impl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.micro.services.search.bl.details.AvailabilityClient;

public class AvailabilityClientTest {

    static final int M2017 = 2017;
    static final int M2018 = 2018;
    static final int M1    = 1;
    static final int M7    = 7;
    static final int M9    = 9;
    static final int M12   = 12;
    static final int M31   = 31;

    @Test
    public void noProducts() {
        AvailabilityClient pc = new AvailabilityClient(null, "junit", true);
        List<String> pIds = new ArrayList<>();
        String result = pc.buildFullUrl(pIds, LocalDate.of(M2018, M1, M7), LocalDate.of(M2018, M1, M9), "60532");
        Assert.assertEquals(
                "junit?params={"
                        + "\"deliveryDateRanges\":[{\"startDate\":\"2018-01-07\",\"endDate\":\"2018-01-09\"}],"
                        + "\"zipCode\":\"60532\"}",
                result);
    }

    @Test
    public void nullProducts() {
        AvailabilityClient pc = new AvailabilityClient(null, "junit", true);
        String result = pc.buildFullUrl(null, LocalDate.of(M2018, M1, M7), LocalDate.of(M2018, M1, M9), "60532");
        Assert.assertEquals(
                "junit?params={"
                        + "\"deliveryDateRanges\":[{\"startDate\":\"2018-01-07\",\"endDate\":\"2018-01-09\"}],"
                        + "\"zipCode\":\"60532\"}",
                result);
    }

    @Test
    public void noDateRanges() {
        AvailabilityClient pc = new AvailabilityClient(null, "junit", true);
        List<String> pIds = new ArrayList<>();
        String result = pc.buildFullUrl(pIds, null, null, "60532");
        Assert.assertEquals(
                "junit?params={"
                        + "\"zipCode\":\"60532\"}",
                result);
    }

    @Test
    public void endDateOnly() {
        AvailabilityClient pc = new AvailabilityClient(null, "junit", true);
        List<String> pIds = new ArrayList<>();
        String result = pc.buildFullUrl(pIds, null, LocalDate.of(M2018, M1, M7), "60532");
        Assert.assertEquals(
                "junit?params={"
                        + "\"deliveryDateRanges\":[{\"endDate\":\"2018-01-07\"}],"
                        + "\"zipCode\":\"60532\"}",
                result);
    }

    @Test
    public void startDateOnly() {
        AvailabilityClient pc = new AvailabilityClient(null, "junit", true);
        List<String> pIds = new ArrayList<>();
        String result = pc.buildFullUrl(pIds, LocalDate.of(M2018, M1, M7), null, "60532");
        Assert.assertEquals(
                "junit?params={"
                        + "\"deliveryDateRanges\":[{\"startDate\":\"2018-01-07\"}],"
                        + "\"zipCode\":\"60532\"}",
                result);
    }

    @Test
    public void noZipCode() {
        AvailabilityClient pc = new AvailabilityClient(null, "junit", true);
        List<String> pIds = new ArrayList<>();
        String result = pc.buildFullUrl(pIds, LocalDate.of(M2018, M1, M7), LocalDate.of(M2018, M1, M9), null);
        Assert.assertEquals(
                "junit?params={"
                        + "\"deliveryDateRanges\":[{\"startDate\":\"2018-01-07\",\"endDate\":\"2018-01-09\"}]}",
                result);
    }

    @Test
    public void oneProduct() {
        AvailabilityClient pc = new AvailabilityClient(null, "junit", true);
        List<String> pIds = new ArrayList<>();
        pIds.add("960");
        String result = pc.buildFullUrl(pIds, LocalDate.of(M2018, M1, M1), LocalDate.of(M2018, M1, M9), "60532");
        Assert.assertEquals(
                "junit?params={"
                        + "\"products\":[{\"productIds\":[\"960\"]}],"
                        + "\"deliveryDateRanges\":[{\"startDate\":\"2018-01-01\",\"endDate\":\"2018-01-09\"}],"
                        + "\"zipCode\":\"60532\"}",
                result);
    }

    @Test
    public void twoProducts() {
        AvailabilityClient pc = new AvailabilityClient(null, "junit", true);
        List<String> pIds = new ArrayList<>();
        pIds.add("960");
        pIds.add("961");
        String result = pc.buildFullUrl(pIds, LocalDate.of(M2017, M12, M31), LocalDate.of(M2018, M1, M1), "60532");
        Assert.assertEquals(
                "junit?params={"
                        + "\"products\":[{\"productIds\":[\"960\",\"961\"]}],"
                        + "\"deliveryDateRanges\":[{\"startDate\":\"2017-12-31\",\"endDate\":\"2018-01-01\"}],"
                        + "\"zipCode\":\"60532\"}",
                result);
    }

}
