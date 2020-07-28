package com.keshavoneml.oneml_customer.DataAPI;

import com.google.gson.JsonArray;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MainPageData {

    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("categories")
    @Expose
    private JsonArray categories;

    @SerializedName("products")
    @Expose
    private JsonArray products;


    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public JsonArray getCategories() {
        return categories;
    }

    public JsonArray getProducts() {
        return products;
    }
}
