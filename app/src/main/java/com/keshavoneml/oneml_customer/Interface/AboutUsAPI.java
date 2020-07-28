package com.keshavoneml.oneml_customer.Interface;

import com.keshavoneml.oneml_customer.DataAPI.AboutUsData;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;

public interface AboutUsAPI {

    @Headers({"x-api-id: ---", "x-api-key: ---"})
    @GET("about-us")
    Call<AboutUsData> getData();

}
