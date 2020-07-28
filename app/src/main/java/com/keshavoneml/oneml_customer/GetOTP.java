package com.keshavoneml.oneml_customer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.TimeUnit;

public class GetOTP extends AppCompatActivity {

    TextView phoneNum;
    EditText ed_code;
    FirebaseAuth mAuth;
    String codeSent;

    String phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().hide();


        setContentView(R.layout.activity_get_otp);

        Intent i = getIntent();
        phone = i.getStringExtra("number");


        phoneNum = findViewById(R.id.phoneNumberDisplay);
        phoneNum.setText("Sending OTP to " + phone + " in 60 seconds, please wait ...");

        mAuth = FirebaseAuth.getInstance();

        ed_code = findViewById(R.id.codeReceived);

        sendVerificationCode(phone);


        findViewById(R.id.signIn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Verifying your Phone Number and OTP, Please wait...", Toast.LENGTH_SHORT).show();
                verifyCode();
            }
        });
    }



    // disable back button

    @Override
    public void onBackPressed() {
        // doing nothing ...
    }

    public void sendVerificationCode(String phone){
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phone,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacks
    }



    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            Toast.makeText(getApplicationContext(),
                    "Either you will receive an OTP or will automatically Redirect, please wait ...", Toast.LENGTH_SHORT).show();
            signInWithPhoneAuthCredential(phoneAuthCredential);
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Toast.makeText(getApplicationContext(),
                    "Problem while generating OTP (check Internet Connection). Please Try again.", Toast.LENGTH_SHORT).show();
            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
        }

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            codeSent = s;
        }

        @Override
        public void onCodeAutoRetrievalTimeOut(String s) {
            super.onCodeAutoRetrievalTimeOut(s);
            Toast.makeText(getApplicationContext(), "Time-Out, please try again... ", Toast.LENGTH_SHORT).show();
        }
    };




    private void verifyCode(){
        String code = ed_code.getText().toString();
        if(code.isEmpty()){
            ed_code.setError("Verification Code Required");
            ed_code.requestFocus();
            return;
        }

        try {
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(codeSent, code);
            signInWithPhoneAuthCredential(credential);
        }catch (Exception e){
            System.out.println("Exception : " + e);
            Toast.makeText(getApplicationContext(), "Invalid OTP", Toast.LENGTH_SHORT).show();
        }
    }


//    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
//        mAuth.signInWithCredential(credential)
//                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if (task.isSuccessful()) {
//                            Intent i = new Intent(getApplicationContext(), MainPage.class);
//                            i.putExtra("mobileNumber", phone);
//                            startActivity(i);
//                            finish();
////                        } else {
////                            task.getException().getMessage();
////                            Toast.makeText(getApplicationContext(), "Either Phone-number or OTP is incorrect. Please try again...", Toast.LENGTH_SHORT).show();
////                        }
//                        }if (!task.isSuccessful()) {
//                            System.out.println(task.getException().getMessage());
//                        }
//                    }
//                });
//    }


    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            System.out.println(phone);
                            final DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Member");
                            userRef.orderByChild("phone").equalTo(phone)
                                    .addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                                            System.out.println(dataSnapshot);
//                                            System.out.println(dataSnapshot.getValue());
                                            if(dataSnapshot.getValue() != null){

                                                String naam = "";
                                                String emailID = "";

                                                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                                                    naam = snapshot.child("name").getValue().toString();
                                                    emailID = snapshot.child("email").getValue().toString();
                                                }


                                                System.out.println("Name : " + naam + "  Email : " + emailID);

                                                Intent i = new Intent(GetOTP.this, MainPage.class);
                                                i.putExtra("mobileNumber", phone);
                                                i.putExtra("userName", naam);
                                                i.putExtra("userEmail", emailID);
                                                startActivity(i);
                                                finish();
                                            }
                                            else{
                                                Toast.makeText(getApplicationContext(), "This phone number is not registered. You should SIGNUP first !!!", Toast.LENGTH_LONG).show();
                                                Intent i = new Intent(GetOTP.this, SignUp.class);
                                                startActivity(i);
                                                finish();
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });
                        } else {
                            task.getException();
                            Toast.makeText(getApplicationContext(), "Either Phone-number or OTP is incorrect (Have you signed-up?). Please try again...", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
