package com.keshavoneml.oneml_customer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.core.widget.NestedScrollView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.cooltechworks.views.shimmer.ShimmerRecyclerView;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.keshavoneml.oneml_customer.DataAPI.MainPageData;
import com.keshavoneml.oneml_customer.DataAPI.SocialMediaData;
import com.keshavoneml.oneml_customer.Interface.MainPageAPI;
import com.keshavoneml.oneml_customer.Interface.SocialMediaAPI;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainPage extends AppCompatActivity {


    ImageView profilePic;
    TextView nameEmailUser;

    TextView productsTextView;

    String phone, userName, userEmail;

    Toolbar toolbar;
    DrawerLayout drawer;


    String link;

    NavigationView navigationView;
    int flag_for_nav = -1;


    LinearLayout ourTeamBanwari, ourTeamManoj, ourTeamAliraja, ourTeamYogesh;

    LinearLayout labTestLayout, medicinesLayout, askDoctorLayout, nearHostpitalLayout;

    ImageView facebook, instagram, linkedin, twitter, youtube;
    String fb = "", insta = "", twit = "", linked = "", yout = "";

    FusedLocationProviderClient fusedLocationProviderClient;

    boolean title_set;

    String lattitute, longitude;
    String temp, city;
    boolean show_temperature_toast = false;


    ImageView prod1, prod2, prod3, prod4, prod5, prod6, prod7, prod8, prod9, prod10, prod11,
            prod12, prod13;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getApplicationContext());
        title_set = false;
        if (ActivityCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            getLocation();
        } else {
            ActivityCompat.requestPermissions(MainPage.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
        }


        setContentView(R.layout.activity_main_page);


        Intent i = getIntent();
        phone = i.getStringExtra("mobileNumber");
        userName = i.getStringExtra("userName");
        userEmail = i.getStringExtra("userEmail");

        productsTextView = findViewById(R.id.productsText);

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://www.1ml.co.in/healthcare/api/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        MainPageAPI mainPageAPI = retrofit.create(MainPageAPI.class);
        Call<MainPageData> call = mainPageAPI.getDataM();

        call.enqueue(new Callback<MainPageData>() {
            @Override
            public void onResponse(Call<MainPageData> call, retrofit2.Response<MainPageData> response) {
                if(!response.isSuccessful()){
                    productsTextView.setText(response.code());
                    return;
                }

                String content = String.valueOf(response.body().getStatus());
                System.out.println("Content  = " + content);
                productsTextView.setText(content);
            }

            @Override
            public void onFailure(Call<MainPageData> call, Throwable t) {
                productsTextView.setText(t.getMessage());
            }
        });


        // 4 icons on MainPage - beneath Search Bar
        labTestLayout = findViewById(R.id.labTestOnPage);
        medicinesLayout = findViewById(R.id.medicinesOnPage);
        askDoctorLayout = findViewById(R.id.askDoctorOnPage);
        nearHostpitalLayout = findViewById(R.id.nearHospitalsOnPage);


        labTestLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLabTest();
            }
        });


        nearHostpitalLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findNearHospitalsLabs();
            }
        });


        toolbar = findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        toolbar.setSubtitle("powered by 1ML");


//        prod1 = findViewById(R.id.aarogyam1_1);
//        prod2 = findViewById(R.id.aarogyam1_2);
//        prod3 = findViewById(R.id.aarogyam1_3);
//        prod4 = findViewById(R.id.aarogyam1_4);
//        prod5 = findViewById(R.id.aarogyam1_5);
//        prod6 = findViewById(R.id.aarogyam1_6);
//        prod7 = findViewById(R.id.aarogyam1_7);
//        prod8 = findViewById(R.id.aarogyam1_8);
//        prod9 = findViewById(R.id.aarogyam1_9);
//        prod10 = findViewById(R.id.vitamin);
//        prod11 = findViewById(R.id.food_intolerance);
//        prod12 = findViewById(R.id.infectionCheckup);
//        prod13 = findViewById(R.id.mcfCovid);


//        prod1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                link = "https://www.thyrocare.com/wellness/Emailer/DM_landing_page/DM_Emailer.aspx?val=UFJPSjEwMTQ0OTcsUFJPSjEwMTQ0OTgsUFJPSjEwMTQ0OTksUFJPSjEwMTQ1MDA=&dQsQa_code=9999626489";
//                callWebLink(link);
//            }
//        });
//
//
//        prod2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                link = "https://www.thyrocare.com/wellness/Emailer/DM_landing_page/DM_Emailer.aspx?VAL=UFJPSjEwMTQ4MjAsUFJPSjEwMTQ4MjEsUFJPSjEwMTQ4MjI=&dQsQa_code=9999626489";
//                callWebLink(link);
//            }
//        });
//
//
//        prod3.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                link = "https://www.thyrocare.com/wellness/emailer/emailer1.aspx?val=UFJPSjEwMTAwNjMsUFJPSjEwMTAwNjQsUFJPSjEwMDk4NTU=&dQsQa_code=9999626489";
//                callWebLink(link);
//            }
//        });
//
//
//        prod4.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                link = "https://www.thyrocare.com/wellness/Emailer/DM_landing_page/DM_Emailer.aspx?VAL=UFJPSjEwMTU0ODAsUFJPSjEwMTU0ODEsUFJPSjEwMTU0ODI=&dQsQa_code=9999626489";
//                callWebLink(link);
//            }
//        });
//
//
//        prod5.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                link = "https://www.thyrocare.com/wellness/Emailer/DM_landing_page/DM_Emailer.aspx?VAL=UFJPSjEwMTc0MzQ=&dQsQa_code=9999626489";
//                callWebLink(link);
//            }
//        });
//
//
//        prod6.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                link = "https://www.thyrocare.com/wellness/Emailer/DM_landing_page/DM_Emailer.aspx?VAL=UFJPSjEwMTI4OTcsUFJPSjEwMTI4OTgsUFJPSjEwMTI4OTksUFJPSjEwMTI5MDAsUFJPSjEwMTI5MDEsUFJPSjEwMTI5MDIsUFJPSjEwMTI5MDMsUFJPSjEwMTI5MDQsUFJPSjEwMTI5MDU=&dQsQa_code=9999626489";
//                callWebLink(link);
//            }
//        });
//
//
//        prod7.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                link = "https://www.thyrocare.com/wellness/Emailer/DM_landing_page/DM_Emailer.aspx?VAL=UFJPSjEwMTQ4MjMsUFJPSjEwMTQ4MjQsUFJPSjEwMTQ4MjUsUFJPSjEwMTQ4MjYsUFJPSjEwMTQ4MjcsUFJPSjEwMTQ4MjgsUFJPSjEwMTQ4MjksUFJPSjEwMTQ4MzAsUFJPSjEwMTQ4MzE=&dQsQa_code=9999626489";
//                callWebLink(link);
//            }
//        });
//
//
//        prod8.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                link = "https://www.thyrocare.com/wellness/Emailer/DM_landing_page/DM_Emailer.aspx?val=UFJPSjEwMTEwODgsUFJPSjEwMTEwODksUFJPSjEwMTEwOTAsUFJPSjEwMTEwOTEsUFJPSjEwMTEwOTIsUFJPSjEwMTEwOTMsUFJPSjEwMTEwOTQsUFJPSjEwMTEwOTUsUFJPSjEwMTEwOTY=&dQsQa_code=9999626489";
//                callWebLink(link);
//            }
//        });
//
//
//        prod9.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                link = "https://covid.thyrocare.com/Covid-19.aspx?source=9999626489";
//                callWebLink(link);
//            }
//        });
//
//
//        prod10.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                link = "https://www.thyrocare.com/wellness/Emailer/DM_landing_page/DM_Emailer.aspx?VAL=UFJPSjEwMTQ0OTQsUFJPSjEwMTQ0OTUsUFJPSjEwMTQ0OTY=&dQsQa_code=9999626489";
//                callWebLink(link);
//            }
//        });
//
//
//        prod11.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                link = "https://www.thyrocare.com/wellness/Emailer/DM_landing_page/DM_Emailer.aspx?VAL=UFJPSjEwMTY1NjY=&dQsQa_code=9999626489";
//                callWebLink(link);
//            }
//        });
//
//
//        prod12.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                link = "https://www.thyrocare.com/wellness/Emailer/DM_landing_page/DM_Emailer.aspx?VAL=UFJPSjEwMTc0MzU=&dQsQa_code=9999626489";
//                callWebLink(link);
//            }
//        });
//
//
//        prod13.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                link = "https://covid.thyrocare.com/Covid-19.aspx?source=9999626489";
//                callWebLink(link);
//            }
//        });


        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        // setting profile pic and name-email of user

        View headerView = navigationView.getHeaderView(0);

        profilePic = headerView.findViewById(R.id.userProfilePicNav);

        nameEmailUser = headerView.findViewById(R.id.userNameEmailNav);
        nameEmailUser.setText(userName);
        nameEmailUser.append("\n" + userEmail);

        System.out.println(phone + " " + userName + "  " + userEmail);


        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Menu m = navigationView.getMenu();
                int id = item.getItemId();


                m.findItem(R.id.myProfile).setVisible(false);
                m.findItem(R.id.referAndEarn).setVisible(false);
                m.findItem(R.id.myOrders).setVisible(false);
                m.findItem(R.id.myPrescriptions).setVisible(false);
                m.findItem(R.id.familyMember).setVisible(false);
                m.findItem(R.id.wishList).setVisible(false);

                m.findItem(R.id.nearestStoreLocator).setVisible(false);
                m.findItem(R.id.nearestBloodBank).setVisible(false);
                m.findItem(R.id.nearestDoctor).setVisible(false);
                m.findItem(R.id.nearestLab).setVisible(false);

                m.findItem(R.id.findMedicine).setVisible(false);
                m.findItem(R.id.orderWithPrescription).setVisible(false);

                m.findItem(R.id.findDoctor).setVisible(false);
                m.findItem(R.id.myAppointment).setVisible(false);

                m.findItem(R.id.bookLabTest).setVisible(false);

                m.findItem(R.id.needHelp).setVisible(false);
                m.findItem(R.id.requestProduct).setVisible(false);
                m.findItem(R.id.myCoupons).setVisible(false);
                m.findItem(R.id.articles).setVisible(false);
                m.findItem(R.id.aboutUs).setVisible(false);
                m.findItem(R.id.privacyPolicy).setVisible(false);
                m.findItem(R.id.returnCancel).setVisible(false);


                if (id == R.id.myAccount) {
                    if (flag_for_nav != 1) {
                        flag_for_nav = 1;
                        m.findItem(R.id.myProfile).setVisible(true);
                        m.findItem(R.id.referAndEarn).setVisible(true);
                        m.findItem(R.id.myOrders).setVisible(true);
                        m.findItem(R.id.myPrescriptions).setVisible(true);
                        m.findItem(R.id.familyMember).setVisible(true);
                        m.findItem(R.id.wishList).setVisible(true);

                        m.findItem(R.id.myProfile).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                Intent i = new Intent(getApplicationContext(), MyProfile.class);
                                i.putExtra("mobile", phone);
                                i.putExtra("userName", userName);
                                i.putExtra("userEmail", userEmail);
                                startActivity(i);
                                return true;
                            }
                        });

                        m.findItem(R.id.referAndEarn).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                Intent i = new Intent(getApplicationContext(), ReferAndEarn.class);
                                startActivity(i);
                                return true;
                            }
                        });

                    } else flag_for_nav = -1;
                }

                if (id == R.id.services) {
                    if (flag_for_nav != 2) {
                        flag_for_nav = 2;
                        m.findItem(R.id.nearestStoreLocator).setVisible(true);
                        m.findItem(R.id.nearestBloodBank).setVisible(true);
                        m.findItem(R.id.nearestDoctor).setVisible(true);
                        m.findItem(R.id.nearestLab).setVisible(true);

                        m.findItem(R.id.nearestStoreLocator).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                Intent i = new Intent(getApplicationContext(), VendorPageNavBar.class);
                                startActivity(i);
                                return true;
                            }
                        });

                    } else flag_for_nav = -1;
                }

                if (id == R.id.medicines) {
                    if (flag_for_nav != 3) {
                        flag_for_nav = 3;
                        m.findItem(R.id.findMedicine).setVisible(true);
                        m.findItem(R.id.orderWithPrescription).setVisible(true);
                    } else flag_for_nav = -1;
                }

                if (id == R.id.doctor) {
                    if (flag_for_nav != 4) {
                        flag_for_nav = 4;
                        m.findItem(R.id.findDoctor).setVisible(true);
                        m.findItem(R.id.myAppointment).setVisible(true);
                    } else flag_for_nav = -1;
                }

                if (id == R.id.lab_test) {
                    if (flag_for_nav != 5) {
                        flag_for_nav = 5;
                        m.findItem(R.id.bookLabTest).setVisible(true);
                    } else flag_for_nav = -1;
                }

                if (id == R.id.others) {
                    if (flag_for_nav != 6) {
                        flag_for_nav = 6;
                        m.findItem(R.id.needHelp).setVisible(true);
                        m.findItem(R.id.requestProduct).setVisible(true);
                        m.findItem(R.id.myCoupons).setVisible(true);
                        m.findItem(R.id.articles).setVisible(true);
                        m.findItem(R.id.aboutUs).setVisible(true);
                        m.findItem(R.id.privacyPolicy).setVisible(true);
                        m.findItem(R.id.returnCancel).setVisible(true);


                        m.findItem(R.id.aboutUs).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                Intent i = new Intent(getApplicationContext(), AboutUs.class);
                                startActivity(i);
                                return true;
                            }
                        });


                        m.findItem(R.id.privacyPolicy).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                Intent i = new Intent(getApplicationContext(), PrivacyPolicy.class);
                                startActivity(i);
                                return true;
                            }
                        });


                        m.findItem(R.id.returnCancel).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                Intent i = new Intent(getApplicationContext(), ReturnCancel.class);
                                startActivity(i);
                                return true;
                            }
                        });

                    } else flag_for_nav = -1;
                }

                if (id == R.id.sign_out) {
                    signOut();
                    flag_for_nav = -1;
                }

                // our Team
//                if (id == R.id.ourTeamMembers) {
//                    ourTeamBanwari = findViewById(R.id.banwari);
//                    ourTeamBanwari.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            Toast.makeText(getApplicationContext(), "Banwari Saini", Toast.LENGTH_SHORT).show();
//                        }
//                    });
//
//
//                    ourTeamManoj = findViewById(R.id.manoj);
//                    ourTeamManoj.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            Toast.makeText(getApplicationContext(), "Manoj Bhushan", Toast.LENGTH_SHORT).show();
//                        }
//                    });
//
//                    ourTeamAliraja = findViewById(R.id.aliraja);
//                    ourTeamAliraja.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            Toast.makeText(getApplicationContext(), "Aliraja Alam", Toast.LENGTH_SHORT).show();
//                        }
//                    });
//
//                    ourTeamYogesh = findViewById(R.id.yogesh);
//                    ourTeamYogesh.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            Toast.makeText(getApplicationContext(), "Yogesh Patel", Toast.LENGTH_SHORT).show();
//                        }
//                    });
//                }


                // temperature
                if (id == R.id.temperature) {
                    findWeather();
                    if (temp != null) {
                        item.setTitle("Temperature : " + temp + " \u2103");
                    }
                }


                // Social Media
                if (id == R.id.socialMedia) {

                    facebook = findViewById(R.id.facebook);
                    instagram = findViewById(R.id.instagram);
                    linkedin = findViewById(R.id.linkedIn);
                    twitter = findViewById(R.id.twitter);
                    youtube = findViewById(R.id.youTube);


                    Gson gson = new GsonBuilder()
                            .setLenient()
                            .create();

                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl("https://www.1ml.co.in/healthcare/api/")
                            .addConverterFactory(GsonConverterFactory.create(gson))
                            .build();


                    SocialMediaAPI socialMediaAPI = retrofit.create(SocialMediaAPI.class);
                    Call<SocialMediaData> call = socialMediaAPI.getDataS();


                    call.enqueue(new Callback<SocialMediaData>() {
                        @Override
                        public void onResponse(Call<SocialMediaData> call, retrofit2.Response<SocialMediaData> response) {
                            fb = String.valueOf(response.body().getData().get("facebook").getAsString());
                            insta = String.valueOf(response.body().getData().get("instagram").getAsString());
                            twit = String.valueOf(response.body().getData().get("twitter").getAsString());
                            linked = String.valueOf(response.body().getData().get("linkedin").getAsString());
                            yout = String.valueOf(response.body().getData().get("youtube").getAsString());

                            System.out.println(fb + "  " + insta + "  " + twit + "  " + linked + "  " + yout);
                        }

                        @Override
                        public void onFailure(Call<SocialMediaData> call, Throwable t) {
                            Toast.makeText(getApplicationContext(), "Error while connecting", Toast.LENGTH_SHORT).show();
                            System.out.println(t.getMessage());
                        }
                    });

//                    Toast.makeText(getApplicationContext(), "Click again", Toast.LENGTH_SHORT).show();


                    facebook.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            if (fb.isEmpty() || fb.equals("#"))
                                Toast.makeText(getApplicationContext(), "Link not found", Toast.LENGTH_SHORT).show();
                            else {
                                if (!fb.contains("https://")) fb = "https://" + fb;
                                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(fb));
                                startActivity(i);
                            }
                        }
                    });


                    instagram.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (insta.isEmpty() || insta.equals("#"))
                                Toast.makeText(getApplicationContext(), "Link not found", Toast.LENGTH_SHORT).show();
                        }
                    });


                    twitter.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (twit.isEmpty() || twit.equals("#"))
                                Toast.makeText(getApplicationContext(), "Link not found", Toast.LENGTH_SHORT).show();
                        }
                    });


                    linkedin.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (linked.isEmpty() || linked.equals("#"))
                                Toast.makeText(getApplicationContext(), "Link not found", Toast.LENGTH_SHORT).show();
                        }
                    });


                    youtube.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (yout.isEmpty() || yout.equals("#"))
                                Toast.makeText(getApplicationContext(), "Link not found", Toast.LENGTH_SHORT).show();
                        }
                    });

                }


                return true;
            }
        });


        BottomNavigationView bottomNav = findViewById(R.id.bottomNavigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);


    }


    // bottom navigation bar
    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.health:
                            Intent i = new Intent(MainPage.this, HealthPage.class);
                            startActivity(i);
                            break;
                        case R.id.medication:
                            Toast.makeText(getApplicationContext(), "Medication", Toast.LENGTH_SHORT).show();
                            break;
                        case R.id.Reminders:
                            Toast.makeText(getApplicationContext(), "Reminders", Toast.LENGTH_SHORT).show();
                            break;
                        case R.id.fit:
                            Toast.makeText(getApplicationContext(), "Fit", Toast.LENGTH_SHORT).show();
                            break;
                    }
                    return true;
                }
            };


    // calls webPage for displaying Our-Products
    private void callWebLink(String link2) {
        Intent i = new Intent(MainPage.this, WebInApp.class);
        i.putExtra("link", link2);
        startActivity(i);
    }


    // toolbar icons

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        String message = "";
        switch (item.getItemId()) {
            case R.id.bellIcon:
                message = "Notifications icon pressed";
                break;
            case R.id.cartIcon:
                message = "Shopping-Cart icon pressed";
                break;
        }
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            signOut();
        }
    }


    public void uploadPrescription(View obj) {
        Intent i = new Intent(MainPage.this, UploadPrescription.class);
        i.putExtra("mobileNumber", phone);
        startActivity(i);
    }


    public void signOut() {
        new AlertDialog.Builder(this)
                .setTitle("Sign-Out from 1ML")
                .setMessage("Would you like to sign-out?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent i = new Intent(MainPage.this, MainActivity.class);
                        startActivity(i);
                        finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // user doesn't want to logout
                    }
                })
                .show();
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
                        Geocoder geocoder = new Geocoder(MainPage.this, Locale.getDefault());
                        List<Address> addresses = geocoder.getFromLocation(
                                location.getLatitude(), location.getLongitude(), 1
                        );

                        System.out.println("map : " + addresses.get(0).getAddressLine(0));
                        setTitle(addresses.get(0).getAddressLine(0));

                        lattitute = Double.toString(addresses.get(0).getLatitude());
                        longitude = Double.toString(addresses.get(0).getLongitude());

                        System.out.println(addresses.get(0).getLatitude());
                        System.out.println("lat" + lattitute);

                        findWeather();


                        title_set = true;

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Turn the Location of your mobile ON", Toast.LENGTH_SHORT).show();
                    MainPage.this.startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                }
            }
        });
    }


    // weather
    public void findWeather() {
//        String url = "api.openweathermap.org/data/2.5/weather?lat={lat}&lon={lon}&appid={fa18d00ef0071b9ed7abf4eed706127c}";
        String url = "http://api.openweathermap.org/data/2.5/weather?lat=";
        url += lattitute + "&lon=";
        url += longitude + "&appid=api_key";

        JsonObjectRequest jor = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject main_object = response.getJSONObject("main");

                    Double tt = main_object.getDouble("temp") - 273.15;
                    DecimalFormat df = new DecimalFormat("0.0");
                    tt = Double.parseDouble(df.format(tt));

                    temp = String.valueOf(tt);
                    city = response.getString("name");

                    System.out.println("tempera : " + temp);

//                    temperature.setText(temp);
//                    temperature.append(" \u2103\n");
//                    temperature.append(city);

                    if (show_temperature_toast)
                        Toast.makeText(getApplicationContext(), "Temperature : " + temp +
                                " \u2103\nLocality : " + city, Toast.LENGTH_SHORT).show();

                    show_temperature_toast = true;

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(jor);

        //https://home.openweathermap.org/api_keys
    }


    public void findNearHospitalsLabs() {
        Intent i = new Intent(getApplicationContext(), NearHospitals.class);
        i.putExtra("lat", lattitute);
        i.putExtra("long", longitude);
        startActivity(i);
    }


    public void getLabTest() {
        Intent i = new Intent(getApplicationContext(), LabTestMainPage.class);
        startActivity(i);
    }
}
