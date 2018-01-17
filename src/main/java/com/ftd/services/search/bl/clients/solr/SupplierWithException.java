package com.ftd.services.search.bl.clients.solr;

public interface SupplierWithException<T> {
    T get() throws Exception;
}
