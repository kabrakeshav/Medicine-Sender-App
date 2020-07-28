package com.keshavoneml.oneml_customer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.keshavoneml.oneml_customer.DataAPI.LabTestData;
import com.keshavoneml.oneml_customer.Interface.LabTestAPI;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LabTestMainPage extends AppCompatActivity {

    ViewPager viewPager;
    LinearLayout sliderDotsPanel;
    int dotsCount;
    ImageView[] dots;

    TextView medicineTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lab_test_main_page);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        viewPager = findViewById(R.id.viewPager);
        sliderDotsPanel = findViewById(R.id.sliderDots);

        medicineTextView = findViewById(R.id.medicines_api);

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(this);
        viewPager.setAdapter(viewPagerAdapter);

        dotsCount = viewPagerAdapter.getCount();
        dots = new ImageView[dotsCount];


        for(int i=0;i<dotsCount;i++){
            dots[i] = new ImageView(this);
            dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_inactive_dot));

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(8, 0, 8, 0);
            sliderDotsPanel.addView(dots[i], params);
        }

        dots[0].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_active_dot));

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                for(int i=0;i<dotsCount;i++){
                    dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_inactive_dot));
                }
                dots[position].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_active_dot));
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });




        // API CALLING FOR THYROCARE

//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl("https://www.thyrocare.com/API_BETA/master.svc/key/ALL/")
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://www.thyrocare.com/apis/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl("https://www.thyrocare.com/apis/")
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();


        LabTestAPI labTestAPI = retrofit.create(LabTestAPI.class);

        Call<LabTestData> call = labTestAPI.getDataL("master.svc/key");

        call.enqueue(new Callback<LabTestData>() {
            @Override
            public void onResponse(Call<LabTestData> call, Response<LabTestData> responsee) {
                if(!responsee.isSuccessful()){
                    System.out.println(responsee);
                    medicineTextView.setText(Integer.toString(responsee.code()));
                    return;
                }

                String content = String.valueOf(responsee.body().getMasters().get("OFFER").getAsString());
//                content = content.substring(1, content.length()-1);

                medicineTextView.setText(content);

            }

            @Override
            public void onFailure(Call<LabTestData> call, Throwable t) {
                System.out.println("Message : "+t.getMessage());
            }
        });

    }
}
