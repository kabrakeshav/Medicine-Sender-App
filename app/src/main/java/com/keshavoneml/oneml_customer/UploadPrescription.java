package com.keshavoneml.oneml_customer;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;


public class UploadPrescription extends AppCompatActivity {


    ImageView presc;

    CheckBox terms;

    String phone;

    ImageView cam, gal, pres_img;

    OutputStream outputStream;
    String path = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_prescription);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        ActivityCompat.requestPermissions(UploadPrescription.this, new String[]
                {Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);


        cam = findViewById(R.id.camera);
        gal = findViewById(R.id.gallery);
        pres_img = findViewById(R.id.prescriptionTag);


        cam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent takePicture = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(takePicture, 0);
            }
        });


        gal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                startActivityForResult(pickPhoto , 1);
            }
        });


        pres_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Functionality yet to add", Toast.LENGTH_SHORT).show();
            }
        });

        presc = findViewById(R.id.prescription);
        presc.setVisibility(View.GONE);


        Intent i = getIntent();
        phone = i.getStringExtra("mobileNumber");



        terms = findViewById(R.id.termsCheck);


        presc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage(getApplicationContext());
            }
        });
    }


    private void selectImage(Context context){
        final CharSequence[] options = {"Take new photo from Camera", "Choose new photo from Gallery", "Remove"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose your Prescription");

        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if(options[item].equals("Take new photo from Camera")){
                    Intent takePicture = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(takePicture, 0);
                }
                else if(options[item].equals("Choose new photo from Gallery")){
                    Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                    startActivityForResult(pickPhoto , 1);
                }
                else if(options[item].equals("Remove")){
                    presc.setImageDrawable(null);
                    presc.setBackgroundColor(Color.rgb(220, 220, 220));
                    presc.setVisibility(View.GONE);
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
                        presc.setVisibility(View.VISIBLE);
                        presc.setImageBitmap(selectedImage);
                    }
                    break;

                case 1:
                    if(resultCode==RESULT_OK && data!=null){
                        Uri selectedImage = data.getData();
                        presc.setVisibility(View.VISIBLE);
                        presc.setImageURI(selectedImage);
                    }
                    break;
            }
        }
    }


    public void nextPage(View obj){

        if (ActivityCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(UploadPrescription.this, new String[]
                    {Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);

            return;
        }


        if(!terms.isChecked()) {
            Toast.makeText(getApplicationContext(), "Terms must be agreed", Toast.LENGTH_SHORT).show();
            return;
        }
        if(presc.getDrawable() == null) {
            Toast.makeText(getApplicationContext(), "You must select a prescription to proceed ahead", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Drawable drawable = presc.getDrawable();
            Bitmap bitmap = ((BitmapDrawable)drawable).getBitmap();

            BitmapHelper.getInstance().setBitmap(bitmap);

            File filePath = Environment.getExternalStorageDirectory();
            File dir = new File(filePath.getAbsolutePath()+"/OneML/");
            dir.mkdir();
            File file = new File(dir, System.currentTimeMillis()+".jpg");

            path = filePath.getAbsolutePath()+"/OneML/" + System.currentTimeMillis()+".jpg";

            try{
                outputStream = new FileOutputStream(file);
            }catch(FileNotFoundException e){
                e.printStackTrace();
            }
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);

            try {
                outputStream.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }


            Intent i = new Intent(UploadPrescription.this, OrderQuantity.class);
            i.putExtra("mobileNumber", phone);
            i.putExtra("prescription_path", path);

            startActivity(i);
        }
    }
}
