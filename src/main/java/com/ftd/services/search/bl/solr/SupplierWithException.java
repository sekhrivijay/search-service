package com.ftd.services.search.bl.solr;

public interface SupplierWithException<T> {
    T get() throws Exception;
}
