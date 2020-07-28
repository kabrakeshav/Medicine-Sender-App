package com.keshavoneml.oneml_customer.Interface;

import com.keshavoneml.oneml_customer.DataAPI.MainPageData;

import retrofit2.Call;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface MainPageAPI {

    @Headers({"x-api-id:---", "x-api-key:---"})
    @POST("home")
    Call<MainPageData> getDataM();


}
