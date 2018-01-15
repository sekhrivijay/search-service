package com.ftd.services.search.bl.details;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * This class is used to format the parameters to the Availability service. It
 * requires a JSON layout in the query part of the URL. Fill in an instance of
 * this class and use GSON to format the JSON layout. nulls are ok for all
 * variables.
 *
 * @author cdegreef
 *
 */
public class AvailabilityParms {

    /**
     * This class handles one specific date range for the query string. Use the
     * addDateRange method on the enclosing class to create instances of this class.
     *
     * @author cdegreef
     *
     */
    private class DeliveryDateRange {

        @SerializedName("startDate")
        @Expose
        private String startDate;

        @SerializedName("endDate")
        @Expose
        private String endDate;

        DeliveryDateRange(String start, String end) {
            startDate = start;
            endDate = end;
        }
    }

    /**
     * This class represents a group of product ids. Use the addProductId on the
     * enclosing class to add products to this group.
     *
     * @author cdegreef
     *
     */
    private class ProductIdGroup {

        @SerializedName("productIds")
        @Expose
        private List<String> productIds = new ArrayList<>();

        void add(String productId) {
            productIds.add(productId);
        }
    }

    @SerializedName("products")
    @Expose
    private List<ProductIdGroup>    productIdGroups;

    @SerializedName("deliveryDateRanges")
    @Expose
    private List<DeliveryDateRange> deliveryDateRanges;

    @SerializedName("zipCode")
    @Expose
    private String                  zipCode;

    /**
     * Add a productId to the specified group.
     *
     * @param group
     *            is the zero relative group number
     * @param productId
     *            is the product Id that should be added to the group
     */
    public void addProductId(int group, String productId) {
        if (productIdGroups == null) {
            productIdGroups = new ArrayList<>();
        }
        for (int g = productIdGroups.size(); g <= group; g++) {
            productIdGroups.add(new ProductIdGroup());
        }
        ProductIdGroup pIds = productIdGroups.get(group);
        pIds.add(productId);
    }

    /**
     * Set the dates using String types.
     *
     * @param start
     *            is the beginning of the date range.
     * @param end
     *            is the ending of the date range.
     */
    public void addDateRange(String start, String end) {
        if (deliveryDateRanges == null) {
            deliveryDateRanges = new ArrayList<>();
        }
        deliveryDateRanges.add(new DeliveryDateRange(start, end));
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }
}