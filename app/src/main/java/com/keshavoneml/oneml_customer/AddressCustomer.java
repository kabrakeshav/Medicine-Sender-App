package com.keshavoneml.oneml_customer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class AddressCustomer extends AppCompatActivity {

    EditText name, address, pin, email;

    String phone, quantity, path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_customer);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        Intent i = getIntent();
        phone = i.getStringExtra("mobileNumber");
        path = i.getStringExtra("prescription_path");
        quantity = i.getStringExtra("quantityDetails");


        name = findViewById(R.id.name);
        address = findViewById(R.id.address);
        pin = findViewById(R.id.pinCode);
        email = findViewById(R.id.emailAddress);

    }


    public void vendorPage(View obj){

        // This should be uncommented at the validation (final release) of app

        if(name.getText().toString().isEmpty() || address.getText().toString().isEmpty() ||
            pin.getText().toString().isEmpty() || email.getText().toString().isEmpty()){
            Toast.makeText(getApplicationContext(), "All fields are mandatory", Toast.LENGTH_SHORT).show();
            return;
        }

        if(!email.getText().toString().matches("[a-zA-Z0-9._-]+@[a-z]+.[a-z]+")){
            Toast.makeText(getApplicationContext(),"Invalid email address", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent i = new Intent(AddressCustomer.this, VendorPage.class);
        i.putExtra("mobileNumber", phone);
        i.putExtra("prescription_path", path);
        i.putExtra("quantityDetails", quantity);
        i.putExtra("name", name.getText().toString());
        i.putExtra("address", address.getText().toString());
        i.putExtra("pinCode", pin.getText().toString());
        i.putExtra("email", email.getText().toString());
        startActivity(i);
    }
}
