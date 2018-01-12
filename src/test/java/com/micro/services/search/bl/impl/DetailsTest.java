package com.micro.services.search.bl.impl;

import java.awt.Dimension;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import org.junit.Assert;
import org.junit.Test;

import com.micro.services.search.api.response.Document;

public class DetailsTest {

    private static final String PREFIX   = "jUnit";
    private ExecutorService     executor = Executors.newSingleThreadExecutor();

    private Map<String, Document> newProductsMap(String... ids) {
        return Arrays.stream(ids).collect(Collectors.toMap(id -> id, id -> newDoc()));
    }

    private Document newDoc() {
        Document doc = new Document();
        doc.setRecord(new HashMap<>());
        return doc;
    }

    @Test
    public void sourceIsNull() throws Exception {
        DetailsServiceImpl impl = new DetailsServiceImpl(null, null, null);
        Object source = null;
        Document target = new Document();
        impl.addServiceResponseToSearchDocument(PREFIX, source, target);
    }

    @Test
    public void targetIsNull() throws Exception {
        DetailsServiceImpl impl = new DetailsServiceImpl(null, null, null);
        Object source = new Dimension(1, 2);
        Document target = null;
        impl.addServiceResponseToSearchDocument(PREFIX, source, target);
    }

    @Test
    public void prefixIsNull() throws Exception {
        DetailsServiceImpl impl = new DetailsServiceImpl(null, null, null);
        Object source = new Dimension(1, 2);
        Document target = new Document();
        try {
            impl.addServiceResponseToSearchDocument(null, source, target);
            Assert.fail("should have thrown an exception");
        } catch (AssertionError e) {
            Assert.assertEquals("tag can not be null", e.getMessage());
        }
    }

    @Test
    public void targetRecordIsNull() throws Exception {
        DetailsServiceImpl impl = new DetailsServiceImpl(null, null, null);

        Object source = new Dimension(1, 2);
        Document target = newDoc();
        target.setRecord(null);
        impl.addServiceResponseToSearchDocument(PREFIX, source, target);

        Assert.assertTrue("target should be empty", target.getRecord() == null);
    }
}