package com.keshavoneml.oneml_customer.DataAPI;

import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LabTestData {

    @SerializedName("MASTERS")
    @Expose
    private JsonObject masters;

    public JsonObject getMasters() {
        return masters;
    }
}
