package com.keshavoneml.oneml_customer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class OrderQuantity extends AppCompatActivity {

    RadioGroup rg;
    RadioButton option;
    String radio_button_text;

    EditText specifyMedicine;

    RadioGroup rg_inside;
    RadioButton option_inside;
    String radio_button_text_inside;
    EditText durationSpecified_inside;

    String selectedOption;
    String phone;
    String path;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_quantity);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        Intent i = getIntent();
        phone = i.getStringExtra("mobileNumber");
        path = i.getStringExtra("prescription_path");


        specifyMedicine = findViewById(R.id.medicineNameRadioTwo);
        specifyMedicine.setVisibility(View.GONE);


        rg_inside = findViewById(R.id.radioInside1);
        rg_inside.setVisibility(View.GONE);

        durationSpecified_inside = findViewById(R.id.durationDays);
        durationSpecified_inside.setVisibility(View.GONE);


        rg = findViewById(R.id.optionChoose);
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int checkedButton = rg.getCheckedRadioButtonId();
                option = findViewById(checkedButton);
                radio_button_text = option.getText().toString();

                if(radio_button_text.equals("Order everything as per prescription")){
                    specifyMedicine.setVisibility(View.GONE);


                    selectedOption = "Order everything as per prescription - ";
                    selectedOption += "\n";

                    rg_inside.setVisibility(View.VISIBLE);
                    rg_inside.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(RadioGroup group, int checkedId) {
                            int button = rg_inside.getCheckedRadioButtonId();
                            option_inside = findViewById(button);
                            radio_button_text_inside = option_inside.getText().toString();
                            System.out.println(radio_button_text_inside);

                            if(radio_button_text_inside.equals("Specify duration of medicines (eg, 20 days)")){
                                durationSpecified_inside.setVisibility(View.VISIBLE);

                            }
                            else if(radio_button_text_inside.equals("duration and quantity specified by doctor")){
                                durationSpecified_inside.setVisibility(View.GONE);
                            }

                        }
                    });

                }

                else if(radio_button_text.equals("Let me specify medicines and quantity")){
                    rg_inside.setVisibility(View.GONE);
                    specifyMedicine.setVisibility(View.VISIBLE);

                    selectedOption = "Let me specify medicines and quantity - ";
                    selectedOption += "\n";

                }

                else if(radio_button_text.equals("Call me for details")){
                    rg_inside.setVisibility(View.GONE);
                    specifyMedicine.setVisibility(View.GONE);

                    selectedOption = "Call me for details";
                }
            }
        });

    }



    public void addressPage(View obj){
        if(rg.getCheckedRadioButtonId() == -1){
            Toast.makeText(getApplicationContext(), "select one option", Toast.LENGTH_SHORT).show();
            return;
        }

        if(radio_button_text.equals("Order everything as per prescription") && rg_inside.getCheckedRadioButtonId()==-1){
            Toast.makeText(getApplicationContext(), "select one option", Toast.LENGTH_SHORT).show();
            return;
        }
        else if(radio_button_text.equals("Order everything as per prescription") && rg_inside.getCheckedRadioButtonId()!=-1){
            int button = rg_inside.getCheckedRadioButtonId();
            option_inside = findViewById(button);
            radio_button_text_inside = option_inside.getText().toString();

            if(radio_button_text_inside.equals("duration and quantity specified by doctor")){
                selectedOption += "Duration and quantity specified by doctor";
            }

        }

        if(radio_button_text.equals("Order everything as per prescription") &&
                radio_button_text_inside.equals("Specify duration of medicines (eg, 20 days)")){
                if(durationSpecified_inside.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "specify duration (days)", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(Integer.parseInt(durationSpecified_inside.getText().toString())==0){
                    Toast.makeText(getApplicationContext(), "days should be greater than 0", Toast.LENGTH_SHORT).show();
                    return;
                }
                else
                    selectedOption += "For " + durationSpecified_inside.getText().toString() + "days";
        }



        if(radio_button_text.equals("Let me specify medicines and quantity") &&
                specifyMedicine.getText().toString().isEmpty()){
            Toast.makeText(getApplicationContext(), "specify medicine names and duration", Toast.LENGTH_SHORT).show();
            return;
        }
        else if(radio_button_text.equals("Let me specify medicines and quantity") &&
                !specifyMedicine.getText().toString().isEmpty()){
            selectedOption += specifyMedicine.getText().toString();
        }

        Intent i = new Intent(OrderQuantity.this, AddressCustomer.class);
        i.putExtra("mobileNumber", phone);
        i.putExtra("prescription_path", path);
        i.putExtra("quantityDetails", selectedOption);
        System.out.println("Selected Option : " + selectedOption);
        startActivity(i);

    }
}
