package com.keshavoneml.oneml_customer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

public class HealthPage extends AppCompatActivity {


    LinearLayout bmi, preg, vacci;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health_page);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        bmi = findViewById(R.id.bmi_layout);
        preg = findViewById(R.id.pregnancy_layout);
        vacci = findViewById(R.id.vaccination_layout);

        bmi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(HealthPage.this, BMI.class);
                startActivity(i);
            }
        });
    }
}
