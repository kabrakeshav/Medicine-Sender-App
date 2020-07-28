package com.keshavoneml.oneml_customer;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.keshavoneml.oneml_customer.DataAPI.AboutUsData;
import com.keshavoneml.oneml_customer.Interface.AboutUsAPI;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class AboutUs extends AppCompatActivity {

    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        textView = findViewById(R.id.message);

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://www.1ml.co.in/healthcare/api/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        AboutUsAPI aboutUsAPI = retrofit.create(AboutUsAPI.class);
        Call<AboutUsData> call = aboutUsAPI.getData();

        call.enqueue(new Callback<AboutUsData>() {
            @Override
            public void onResponse(Call<AboutUsData> call, Response<AboutUsData> response) {
                if(!response.isSuccessful()){
                    textView.setText(response.code());
                    return;
                }

//                System.out.println(response.headers()+"   " + response.body().getData().get("aboutus"));

                String content = String.valueOf(response.body().getData().get("aboutus").getAsString());
//                content = content.substring(1, content.length()-1);

                textView.setText(content);
            }

            @Override
            public void onFailure(Call<AboutUsData> call, Throwable t) {
                textView.setText(t.getMessage());
            }
        });

    }
}
