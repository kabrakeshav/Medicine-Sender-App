package com.keshavoneml.oneml_customer;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import java.util.Objects;


/* SHOWING GOOGLE MAP IN APP */

public class NearHospitals extends AppCompatActivity{

    WebView wb;

    double lat, lng;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_near_hospitals);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Intent i = getIntent();
        lat = Double.parseDouble(Objects.requireNonNull(i.getStringExtra("lat")));
        lng = Double.parseDouble(Objects.requireNonNull(i.getStringExtra("long")));

        wb = findViewById(R.id.map);

        wb.setWebViewClient(new WebViewClient());
        wb.getSettings().setJavaScriptEnabled(true);

        wb.loadUrl("https://www.google.com/maps/search/diagnostic-labs/@"+lat+
                        ","+lng+",13z");

        System.out.println("https://www.google.com/maps/search/diagnostic-labs/@"+lat+
                ","+lng+",13z");



    }

}


/* ********************************************************************** */
/* YOUTUBE TUTORIAL ... NOT WORKING */

//public class NearHospitals extends AppCompatActivity implements OnMapReadyCallback, LocationListener,
//        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
//
//    private GoogleMap gMap;
//
//    GoogleApiClient client;
//    LocationRequest request;
//    LatLng latLng;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_near_hospitals);
//
//
//        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
//                .findFragmentById(R.id.map);
//
//
//        mapFragment.getMapAsync(this);
//    }
//
//
//    public void findHospitals(View obj){
//        StringBuilder stringBuilder = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
//        stringBuilder.append("location="+latLng.latitude+","+latLng.longitude);
//        stringBuilder.append("&radius=10000");
//        stringBuilder.append("&types="+"restaurant");
//        stringBuilder.append("&key="+getResources().getString(R.string.google_places_api));
//
//        String url = stringBuilder.toString();
//        System.out.println("URL : " + url);
//
//        Object dataTransfer[] = new Object[2];
//        dataTransfer[0] = gMap;
//        dataTransfer[1] = url;
//
//        GetNearbyPlaces getNearbyPlaces = new GetNearbyPlaces(this);
//        getNearbyPlaces.execute(dataTransfer);
//
//    }
//
//
//    public void findHospitalss(View obj){
////        String link = "https://www.google.com/maps/search/hospital/@"
////                            +latLng.latitude+","+latLng.longitude+",13z";
////        System.out.println(link);
////        callWebLink(link);
//
//    }
//
//
//    // calls webPage for displaying Our-Products
//    private void callWebLink(String link) {
////        Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
////        startActivity(i);
//        Uri gmmIntentUri = Uri.parse("geo:0,0?z=13&q=hospitals");
//        Intent mapIntent = new Intent(Intent.ACTION_MAIN, gmmIntentUri);
//        mapIntent.setPackage("com.google.android.apps.maps");
//        startActivity(mapIntent);
//    }
//
//
//    @Override
//    public void onConnected(@Nullable Bundle bundle) {
//        request = new LocationRequest().create();
//        request.setInterval(1000);
//        request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
//
//        if (ActivityCompat.checkSelfPermission(getApplicationContext(),
//                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            return;
//        }
//
//        LocationServices.FusedLocationApi.requestLocationUpdates(client, request, this);
//
//    }
//
//    @Override
//    public void onConnectionSuspended(int i) {
//
//    }
//
//    @Override
//    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
//
//    }
//
//    @Override
//    public void onLocationChanged(Location location) {
//        if(location == null){
//            Toast.makeText(getApplicationContext(), "Location not found !", Toast.LENGTH_SHORT).show();
//        }
//        else {
//            latLng  = new LatLng(location.getLatitude(), location.getLongitude());
//            CameraUpdate update = CameraUpdateFactory.newLatLngZoom(latLng, 15);
//
//            gMap.animateCamera(update);
//
//            MarkerOptions markerOptions = new MarkerOptions();
//            markerOptions.position(latLng);
//            markerOptions.title("Current Location");
//            gMap.addMarker(markerOptions);
//
//        }
//    }
//
//    @Override
//    public void onMapReady(GoogleMap googleMap) {
//        gMap = googleMap;
//        client = new GoogleApiClient.Builder(this)
//                .addApi(LocationServices.API)
//                .addConnectionCallbacks(this)
//                .addOnConnectionFailedListener(this)
//                .build();
//
//        client.connect();
//    }
//}
