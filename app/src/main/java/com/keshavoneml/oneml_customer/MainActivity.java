package com.keshavoneml.oneml_customer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
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
import com.hbb20.CountryCodePicker;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    EditText ed_phone;
    FirebaseAuth mAuth;
    String codeSent;
    String phone;
    CountryCodePicker ccp;

    FusedLocationProviderClient fusedLocationProviderClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // full screen
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        getSupportActionBar().hide();

        setContentView(R.layout.activity_main);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getApplicationContext());
        if (ActivityCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            getLocation();
        } else {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
        }


        phone = "";

        mAuth = FirebaseAuth.getInstance();

        ed_phone = findViewById(R.id.phone_num);

        ccp = findViewById(R.id.ccp);


        /* TODO : FOR TEMPORARY DEVELOPMENT : TO STOP OTP, REDIRECTING DIRECTLY TO MAINPAGE.JAVA
           TODO : UNCOMMENT THIS CODE FOR SENDING OTPs */

       findViewById(R.id.verificationCode).setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
                   String p = sendVerificationCode();
                   if(p != null) {
                       Intent i = new Intent(MainActivity.this, GetOTP.class);
                       System.out.println(p);
                       i.putExtra("number", p);
                       startActivity(i);
                   }
           }
       });

    }


    private String sendVerificationCode() {
        phone = "+" + ccp.getFullNumber();
        phone += ed_phone.getText().toString();
        System.out.println(phone);
        if (ed_phone.getText().toString().isEmpty()) {
            ed_phone.setError("Phone Number Required");
            ed_phone.requestFocus();
            return null;
        }
        if (ed_phone.getText().toString().length() < 10) {
            ed_phone.setError("Invalid Phone Number");
            ed_phone.requestFocus();
            return null;
        }

        return phone;
    }


    // location

    private void getLocation() {
        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                Location location = task.getResult();
                System.out.println(location);
                if (location != null) {
                    try {
                        Geocoder geocoder = new Geocoder(MainActivity.this, Locale.getDefault());
                        List<Address> addresses = geocoder.getFromLocation(
                                location.getLatitude(), location.getLongitude(), 1
                        );

                        System.out.println("map : " + addresses.get(0).getAddressLine(0));

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Turn the Location of your mobile ON", Toast.LENGTH_SHORT).show();
                    new AlertDialog.Builder(MainActivity.this)
                            .setTitle("Location Access")
                            .setMessage("1ML wants to access your location")
                            .setPositiveButton("Allow", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    MainActivity.this.startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                                }
                            })
                            .show();

                }
            }
        });
    }
}
