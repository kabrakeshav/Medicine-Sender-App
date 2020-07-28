package com.keshavoneml.oneml_customer;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.keshavoneml.oneml_customer.DataAPI.PrivacyPolicyData;
import com.keshavoneml.oneml_customer.Interface.PrivacyPolicyAPI;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PrivacyPolicy extends AppCompatActivity {

    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy_policy);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        textView = findViewById(R.id.messageP);


        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://www.1ml.co.in/healthcare/api/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        PrivacyPolicyAPI privacyPolicy = retrofit.create(PrivacyPolicyAPI.class);
        Call<PrivacyPolicyData> call = privacyPolicy.getDataP();

        call.enqueue(new Callback<PrivacyPolicyData>() {
            @Override
            public void onResponse(Call<PrivacyPolicyData> call, Response<PrivacyPolicyData> response) {
                if(!response.isSuccessful()){
                    textView.setText(response.code());
                    return;
                }

//                System.out.println(response.headers()+"   " + response.body().getData().get("aboutus"));

                String content = String.valueOf(response.body().getData().get("privacypolicy").getAsString());
//                content = content.substring(1, content.length()-1);

                textView.setText(content);
            }

            @Override
            public void onFailure(Call<PrivacyPolicyData> call, Throwable t) {
                textView.setText(t.getMessage());
            }
        });


    }
}
