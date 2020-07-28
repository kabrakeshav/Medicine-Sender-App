package com.keshavoneml.oneml_customer.DataAPI;

import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SocialMediaData {

    @SerializedName("data")
    @Expose
    private JsonObject data;


    public JsonObject getData() {
        return data;
    }
}
