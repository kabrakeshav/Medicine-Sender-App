package com.keshavoneml.oneml_customer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hbb20.CCPCountry;
import com.hbb20.CountryCodePicker;

public class SignUp extends AppCompatActivity {


    EditText name, email, phone;
    CountryCodePicker ccp;
    Button sign;

    DatabaseReference reff;

    Member obj;
    String user, emailId, mobile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().hide();


        setContentView(R.layout.activity_sign_up);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        ccp = findViewById(R.id.ccpSign);
        phone = findViewById(R.id.phone_num);
        sign = findViewById(R.id.signUpActivity);

        obj = new Member();

        reff = FirebaseDatabase.getInstance().getReference().child("Member");


        sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validate()){
                    obj.setName(user);;
                    obj.setEmail(emailId);
                    obj.setPhone(mobile);

                    reff.orderByChild("phone").equalTo(mobile).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists())
                                Toast.makeText(SignUp.this, "This Phone Already Registered", Toast.LENGTH_SHORT).show();
                            else{
                                reff.push().setValue(obj);
                                Toast.makeText(SignUp.this, "Registered Successfully, Sign-In now ...", Toast.LENGTH_SHORT).show();

                                Intent i = new Intent(SignUp.this, MainActivity.class);
                                startActivity(i);

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                }
            }
        });

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(SignUp.this, MainActivity.class));
        finish();
    }

    private Boolean validate(){
        Boolean result = false;

        user = name.getText().toString().trim();
        emailId = email.getText().toString().trim();
        mobile = "+" + ccp.getFullNumber() + phone.getText().toString().trim();

        System.out.println(mobile);

        if(user.isEmpty() || emailId.isEmpty() || mobile.isEmpty()){
            Toast.makeText(this, "All fields required", Toast.LENGTH_SHORT).show();
        }
        else if(user.length() < 3){
            Toast.makeText(this, "Enter a bigger name", Toast.LENGTH_SHORT).show();
        }
        else if(mobile.length() != 13){
            Toast.makeText(this, "Invalid Phone Number", Toast.LENGTH_SHORT).show();
        }
        else
            result = true;

        return result;
    }
}
