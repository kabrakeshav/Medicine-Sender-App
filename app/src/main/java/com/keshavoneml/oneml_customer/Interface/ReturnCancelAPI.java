package com.keshavoneml.oneml_customer.Interface;


import com.keshavoneml.oneml_customer.DataAPI.ReturnCancelData;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;

public interface ReturnCancelAPI {

    @Headers({"x-api-id: ---", "x-api-key: ---"})
    @GET("return-cancellation")
    Call<ReturnCancelData> getDataP();

}
