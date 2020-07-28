package com.keshavoneml.oneml_customer.Interface;

import com.keshavoneml.oneml_customer.DataAPI.SocialMediaData;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;

public interface SocialMediaAPI {

    @Headers({"x-api-id: ---", "x-api-key: ---"})
    @GET("social-media")
    Call<SocialMediaData> getDataS();

}
