package com.ftd.service.search.bl.solr;

public interface SupplierWithException<T> {
    T get() throws Exception;
}
