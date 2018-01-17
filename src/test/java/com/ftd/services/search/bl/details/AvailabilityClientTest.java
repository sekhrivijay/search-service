package com.ftd.services.search.bl.details;

import java.util.HashSet;
import java.util.Set;

import com.ftd.services.search.bl.clients.availibility.AvailabilityClientImpl;
import org.junit.Assert;
import org.junit.Test;

public class AvailabilityClientTest {

    @Test
    public void noProducts() throws Exception {
        AvailabilityClientImpl pc = new AvailabilityClientImpl(null, "junit");
        Set<String> pIds = new HashSet<>();
        String result = pc.buildUniquePartOfUrl(pIds, "2018-01-07", "2018-01-09", "60532");
        Assert.assertEquals(
                "params={"
                        + "\"deliveryDateRanges\":[{\"startDate\":\"2018-01-07\",\"endDate\":\"2018-01-09\"}],"
                        + "\"zipCode\":\"60532\"}",
                result);
    }

    @Test
    public void nullProducts() throws Exception {
        AvailabilityClientImpl pc = new AvailabilityClientImpl(null, "junit");
        String result = pc.buildUniquePartOfUrl(null, "2018-01-07", "2018-01-09", "60532");
        Assert.assertEquals(
                "params={"
                        + "\"deliveryDateRanges\":[{\"startDate\":\"2018-01-07\",\"endDate\":\"2018-01-09\"}],"
                        + "\"zipCode\":\"60532\"}",
                result);
    }

    @Test
    public void noDateRanges() throws Exception {
        AvailabilityClientImpl pc = new AvailabilityClientImpl(null, "junit");
        Set<String> pIds = new HashSet<>();
        String result = pc.buildUniquePartOfUrl(pIds, null, null, "60532");
        Assert.assertEquals(
                "params={"
                        + "\"zipCode\":\"60532\"}",
                result);
    }

    @Test
    public void endDateOnly() throws Exception {
        AvailabilityClientImpl pc = new AvailabilityClientImpl(null, "junit");
        Set<String> pIds = new HashSet<>();
        String result = pc.buildUniquePartOfUrl(pIds, null, "2018-01-07", "60532");
        Assert.assertEquals(
                "params={"
                        + "\"deliveryDateRanges\":[{\"endDate\":\"2018-01-07\"}],"
                        + "\"zipCode\":\"60532\"}",
                result);
    }

    @Test
    public void startDateOnly() throws Exception {
        AvailabilityClientImpl pc = new AvailabilityClientImpl(null, "junit");
        Set<String> pIds = new HashSet<>();
        String result = pc.buildUniquePartOfUrl(pIds, "2018-01-07", null, "60532");
        Assert.assertEquals(
                "params={"
                        + "\"deliveryDateRanges\":[{\"startDate\":\"2018-01-07\"}],"
                        + "\"zipCode\":\"60532\"}",
                result);
    }

    @Test
    public void noZipCode() throws Exception {
        AvailabilityClientImpl pc = new AvailabilityClientImpl(null, "junit");
        Set<String> pIds = new HashSet<>();
        String result = pc.buildUniquePartOfUrl(pIds, "2018-01-07", "2018-01-09", null);
        Assert.assertEquals(
                "params={"
                        + "\"deliveryDateRanges\":[{\"startDate\":\"2018-01-07\",\"endDate\":\"2018-01-09\"}]}",
                result);
    }

    @Test
    public void oneProduct() throws Exception {
        AvailabilityClientImpl pc = new AvailabilityClientImpl(null, "junit");
        Set<String> pIds = new HashSet<>();
        pIds.add("960");
        String result = pc.buildUniquePartOfUrl(pIds, "2018-01-01", "2018-01-09", "60532");
        Assert.assertEquals(
                "params={"
                        + "\"products\":[{\"productIds\":[\"960\"]}],"
                        + "\"deliveryDateRanges\":[{\"startDate\":\"2018-01-01\",\"endDate\":\"2018-01-09\"}],"
                        + "\"zipCode\":\"60532\"}",
                result);
    }

    @Test
    public void twoProducts() throws Exception {
        AvailabilityClientImpl pc = new AvailabilityClientImpl(null, "junit");
        Set<String> pIds = new HashSet<>();
        pIds.add("960");
        pIds.add("961");
        String result = pc.buildUniquePartOfUrl(pIds, "2017-12-31", "2018-01-01", "60532");
        Assert.assertEquals(
                "params={"
                        + "\"products\":[{\"productIds\":[\"960\",\"961\"]}],"
                        + "\"deliveryDateRanges\":[{\"startDate\":\"2017-12-31\",\"endDate\":\"2018-01-01\"}],"
                        + "\"zipCode\":\"60532\"}",
                result);
    }

}
