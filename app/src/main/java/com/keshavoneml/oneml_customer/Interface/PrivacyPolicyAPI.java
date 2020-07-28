package com.keshavoneml.oneml_customer.Interface;

import com.keshavoneml.oneml_customer.DataAPI.PrivacyPolicyData;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;

public interface PrivacyPolicyAPI {

    @Headers({"x-api-id: ---", "x-api-key: ---"})
    @GET("privacy-policy")
    Call<PrivacyPolicyData> getDataP();

}
