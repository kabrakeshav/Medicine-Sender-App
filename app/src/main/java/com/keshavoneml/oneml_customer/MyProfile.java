package com.keshavoneml.oneml_customer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.Toolbar;

import java.util.Calendar;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyProfile extends AppCompatActivity {

    EditText dob;
    DatePickerDialog.OnDateSetListener mDateSetListener;

    ImageView camera;
    CircleImageView profile;

    EditText name, email, phone_num;

    String mobile, userName, userEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_my_profile);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        Intent i = getIntent();
        mobile = i.getStringExtra("mobile");
        userName = i.getStringExtra("userName");
        userEmail = i.getStringExtra("userEmail");

        name = findViewById(R.id.edtUsername);
        email = findViewById(R.id.edtUserEmail);
        phone_num = findViewById(R.id.edtUserPhone);

        name.setText(userName);
        email.setText(userEmail);
        phone_num.setText(mobile);




        profile = findViewById(R.id.profilePic);
        camera = findViewById(R.id.profilePicChose);
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage(getApplicationContext());
            }
        });


        dob = findViewById(R.id.dateOfbirth);
        dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(MyProfile.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,mDateSetListener,
                        year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                Calendar selectedCal = Calendar.getInstance();
                selectedCal.set(year, month, dayOfMonth);
                Date selectedDate = selectedCal.getTime();
                Date currentDate = Calendar.getInstance().getTime();
                int diff = selectedDate.compareTo(currentDate);
                if(diff > 0)
                {
                    System.out.println(diff);
                    Toast.makeText(getApplicationContext(), "Invalid DOB", Toast.LENGTH_SHORT).show();
                    dob.getText().clear();
                    return;
                }
                month++;
                String date = dayOfMonth + " - " + month + " - " + year;
                dob.setText(date);
            }
        };
    }





    private void selectImage(Context context){
        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Remove"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose your Prescription");

        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if(options[item].equals("Take Photo")){
                    Intent takePicture = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(takePicture, 0);
                }
                else if(options[item].equals("Choose from Gallery")){
                    Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                    startActivityForResult(pickPhoto , 1);
                }
                else if(options[item].equals("Remove")){
                    profile.setImageResource(R.drawable.ic_account_circle);
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode != RESULT_CANCELED){
            switch (requestCode){
                case 0:
                    if(resultCode==RESULT_OK && data!=null){
                        Bitmap selectedImage = (Bitmap) data.getExtras().get("data");
                        profile.setImageBitmap(selectedImage);
                    }
                    break;

                case 1:
                    if(resultCode==RESULT_OK && data!=null){
                        Uri selectedImage = data.getData();
                        profile.setImageURI(selectedImage);
                    }
                    break;
            }
        }
    }


    public void updateProfile(View obj){
        Toast.makeText(getApplicationContext(), "Under Construction", Toast.LENGTH_SHORT).show();
    }
}
