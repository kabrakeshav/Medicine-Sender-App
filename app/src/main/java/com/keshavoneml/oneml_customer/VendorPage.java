package com.keshavoneml.oneml_customer;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;


public class VendorPage extends AppCompatActivity {

    FusedLocationProviderClient fusedLocationProviderClient;



    ImageView imageView;
    ProgressDialog dialog;
    EditText editTitle, editDescription;
    Keystore store;

    String userid = "";

    Button buttonContinue;

    RecyclerView recycleviewdocument;
    private AdapterVendersSelect recyclerAdapter;
    LinearLayout layUploadimag;

    private ArrayList<GatterGetVendersModel> gatterGetFamilyDocumentListModels;

    String phone, quantity, name, address, pin, email, path;
    String addressCustomerLocator = "";

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor_page);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        Intent i = getIntent();
        phone = i.getStringExtra("mobileNumber");
        path = i.getStringExtra("prescription_path");
        quantity = i.getStringExtra("quantityDetails");
        name = i.getStringExtra("name");
        address = i.getStringExtra("address");
        pin = i.getStringExtra("pinCode");
        email = i.getStringExtra("email");


        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getApplicationContext());


        if(ActivityCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            getLocation();
        }
        else{
            ActivityCompat.requestPermissions(VendorPage.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
        }



//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        store = Keystore.getInstance(getApplicationContext());
//        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        dialog = new ProgressDialog(this);

        //initializing views
        imageView = findViewById(R.id.imageView);
        editTitle = findViewById(R.id.editTitle);
        editDescription = findViewById(R.id.editDescription);
        buttonContinue = findViewById(R.id.buttonContinue);
        recycleviewdocument = findViewById(R.id.recycleviewdocument);
        layUploadimag = findViewById(R.id.layUploadimag);

        userid = store.get("id");


        gatterGetFamilyDocumentListModels = new ArrayList<>();
        recyclerAdapter = new AdapterVendersSelect(gatterGetFamilyDocumentListModels, this);
        LinearLayoutManager recyclerLayoutManager = new LinearLayoutManager(getApplicationContext());
        recycleviewdocument.setLayoutManager(recyclerLayoutManager);
        recycleviewdocument.setHasFixedSize(true);
        recycleviewdocument.setAdapter(recyclerAdapter);

        getVendorList();

    }

    private void getVendorList() {

        System.out.println("Hello");

        final ProgressDialog mProgressDialog = new ProgressDialog(VendorPage.this);

        mProgressDialog.setTitle("1ML - Vendors");
        mProgressDialog.setMessage("Loading...");
        mProgressDialog.setIndeterminate(false);
        try{
            mProgressDialog.show();
        }catch (WindowManager.BadTokenException e) {
            System.out.println(e);
        }


        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://www.1ml.co.in/healthcare/api/fix-area-vendor",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject obj;
                        try {
                            obj = new JSONObject(response);
                            String strstatus = obj.getString("status");
                            if (strstatus.equals("false")) {
                                Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                            } else {

                                gatterGetFamilyDocumentListModels.clear();

                                JSONArray jsonArray = obj.getJSONArray("message");
                                for (int j = 0; j < jsonArray.length(); j++) {

                                    gatterGetFamilyDocumentListModels.add(new GatterGetVendersModel(
                                            false,
                                            jsonArray.getJSONObject(j).getString("id"),
                                            jsonArray.getJSONObject(j).getString("name"),
                                            jsonArray.getJSONObject(j).getString("pharm_name"),
                                            jsonArray.getJSONObject(j).getString("store_address"),
                                            jsonArray.getJSONObject(j).getString("average_rating"),
                                            jsonArray.getJSONObject(j).getString("store_status"), ""
                                    ));
                                    recyclerAdapter.notifyDataSetChanged();
                                    mProgressDialog.dismiss();

                                }

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), "Error Occurred", Toast.LENGTH_LONG).show();
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("a", "profile error=========" + error.toString());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("addid", addressCustomerLocator);
                params.put("type", "1");
                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);

    }


    public void clickFunction(View view) {
        switch (view.getId()) {
            case R.id.buttonContinue:
                String venderarr = "",vendername="";
                for (int j = 0; j < gatterGetFamilyDocumentListModels.size(); j++) {
                    if (gatterGetFamilyDocumentListModels.get(j).isSelected()) {
                        venderarr = venderarr + gatterGetFamilyDocumentListModels.get(j).getId() + ",";
                        vendername = vendername +  gatterGetFamilyDocumentListModels.get(j).getPharm_name() + ", ";

                    }
                }
                String VendoridList = method(venderarr);
                if (!TextUtils.isEmpty(VendoridList)) {
                    Intent i = new Intent(this, OrderDetails.class);
                    i.putExtra("perception_vendor", VendoridList);
                    i.putExtra("vendorName", vendername);
                    i.putExtra("perception_address_id", addressCustomerLocator);

                    i.putExtra("prescription_path", path);

                    i.putExtra("mobileNumber", phone);
                    i.putExtra("quantityDetails", quantity);
                    i.putExtra("name", name);
                    i.putExtra("address", address);
                    i.putExtra("pinCode", pin);
                    i.putExtra("email", email);

                    startActivity(i);
                } else {
                    Toast.makeText(getApplicationContext(), "Please select a vendor", Toast.LENGTH_SHORT).show();
                }

                break;


        }
    }

    public String method(String str) {
        if (str != null && str.length() > 0 && str.charAt(str.length() - 1) == ',') {
            str = str.substring(0, str.length() - 1);
        }
        return str;
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }





    private void getLocation() {

        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                Location location = task.getResult();
                System.out.println(location);
                if(location != null){
                    try {
                        Geocoder geocoder = new Geocoder(VendorPage.this, Locale.getDefault());
                        List<Address> addresses = geocoder.getFromLocation(
                                location.getLatitude(), location.getLongitude(), 1
                        );


                        addressCustomerLocator = addresses.get(0).getAddressLine(0);

//                        textView5.setText(Html.fromHtml(
//                                "<font color='#6200EE'<b>Address: </b><br></font>"
//                                        + addresses.get(0).getAddressLine(0)
//                        ));

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                else{
                    Toast.makeText(getApplicationContext(), "Turn the Location of your mobile ON", Toast.LENGTH_SHORT).show();
                    VendorPage.this.startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                }
            }
        });
    }

}









//package com.keshavoneml.oneml_customer;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.core.app.ActivityCompat;
//
//import android.Manifest;
//import android.content.Context;
//import android.content.Intent;
//import android.content.pm.PackageManager;
//import android.location.Address;
//import android.location.Geocoder;
//import android.location.Location;
//import android.os.Bundle;
//import android.provider.Settings;
//import android.text.Html;
//import android.view.View;
//import android.webkit.WebView;
//import android.widget.Button;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.google.android.gms.flags.Flag;
//import com.google.android.gms.location.FusedLocationProviderClient;
//import com.google.android.gms.location.LocationServices;
//import com.google.android.gms.tasks.OnCompleteListener;
//import com.google.android.gms.tasks.Task;
//
//import java.io.IOException;
//import java.util.List;
//import java.util.Locale;
//
//public class VendorPage extends AppCompatActivity {
//
//    Button button;
//    TextView textView1, textView2, textView3, textView4, textView5;
////    WebView wb;
//
//    FusedLocationProviderClient fusedLocationProviderClient;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_vendor_page);
//
//        button = findViewById(R.id.locationButton);
//        textView1 = findViewById(R.id.textView1);
//        textView2 = findViewById(R.id.textView2);
//        textView3 = findViewById(R.id.textView3);
//        textView4 = findViewById(R.id.textView4);
//        textView5 = findViewById(R.id.textView5);
//
//        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getApplicationContext());
//
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // checking permission
//                if(ActivityCompat.checkSelfPermission(getApplicationContext(),
//                        Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
//                    getLocation();
//                }
//                else{
//                    ActivityCompat.requestPermissions(VendorPage.this,
//                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
//                }
//            }
//        });
//
//
////        wb = findViewById(R.id.web);
////
////        wb.loadUrl("http://1ml.co.in/healthcare/api/fix-area-vendor");
//    }
//
//
//    private void getLocation() {
//        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
//            @Override
//            public void onComplete(@NonNull Task<Location> task) {
//                Location location = task.getResult();
//                System.out.println(location);
//                if(location != null){
//                    try {
//                        Geocoder geocoder = new Geocoder(VendorPage.this, Locale.getDefault());
//                        List<Address>addresses = geocoder.getFromLocation(
//                                location.getLatitude(), location.getLongitude(), 1
//                        );
//
//                        textView1.setText(Html.fromHtml(
//                                "<font color='#6200EE'<b>Latitude: </b><br></font>"
//                                + addresses.get(0).getLatitude()
//                        ));
//
//                        textView2.setText(Html.fromHtml(
//                                "<font color='#6200EE'<b>Longitute: </b><br></font>"
//                                        + addresses.get(0).getLongitude()
//                        ));
//
//                        textView3.setText(Html.fromHtml(
//                                "<font color='#6200EE'<b>Country: </b><br></font>"
//                                        + addresses.get(0).getCountryName()
//                        ));
//
//                        textView4.setText(Html.fromHtml(
//                                "<font color='#6200EE'<b>Locality: </b><br></font>"
//                                        + addresses.get(0).getLocality()
//                        ));
//
//                        textView5.setText(Html.fromHtml(
//                                "<font color='#6200EE'<b>Address: </b><br></font>"
//                                        + addresses.get(0).getAddressLine(0)
//                        ));
//
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//                else{
//                    Toast.makeText(getApplicationContext(), "Turn the Location of your mobile ON", Toast.LENGTH_SHORT).show();
//                    VendorPage.this.startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
//                }
//            }
//        });
//    }
//}
