package com.keshavoneml.oneml_customer.Interface;

import com.keshavoneml.oneml_customer.DataAPI.LabTestData;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import retrofit2.http.Url;

public interface LabTestAPI {

    @GET
    Call<LabTestData> getDataL(
            @Url String url
    );

}
