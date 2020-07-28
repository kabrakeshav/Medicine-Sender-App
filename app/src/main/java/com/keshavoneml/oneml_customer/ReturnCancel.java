package com.keshavoneml.oneml_customer;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.keshavoneml.oneml_customer.DataAPI.PrivacyPolicyData;
import com.keshavoneml.oneml_customer.DataAPI.ReturnCancelData;
import com.keshavoneml.oneml_customer.Interface.PrivacyPolicyAPI;
import com.keshavoneml.oneml_customer.Interface.ReturnCancelAPI;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ReturnCancel extends AppCompatActivity {

    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_return_cancel);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        textView = findViewById(R.id.messageR);


        Gson gson = new GsonBuilder()
                .setLenient()
                .create();


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://www.1ml.co.in/healthcare/api/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        ReturnCancelAPI returnCancelAPI = retrofit.create(ReturnCancelAPI.class);
        Call<ReturnCancelData> call = returnCancelAPI.getDataP();

        call.enqueue(new Callback<ReturnCancelData>() {
            @Override
            public void onResponse(Call<ReturnCancelData> call, Response<ReturnCancelData> response) {
                if(!response.isSuccessful()){
                    textView.setText(response.code());
                    return;
                }

//                System.out.println(response.headers()+"   " + response.body().getData().get("aboutus"));

                String content = String.valueOf(response.body().getData().get("returncancellation").getAsString());
//                content = content.substring(1, content.length()-1);

                textView.setText(content);
            }

            @Override
            public void onFailure(Call<ReturnCancelData> call, Throwable t) {
                textView.setText(t.getMessage());
            }
        });

    }
}
