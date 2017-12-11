package com.micro.services.search.bl.solr;

public interface SupplierWithException<T> {
    T get() throws Exception;
}
