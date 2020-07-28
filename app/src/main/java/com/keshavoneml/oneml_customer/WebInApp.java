package com.keshavoneml.oneml_customer;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WebInApp extends AppCompatActivity {

    WebView wb;
    ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setContentView(R.layout.activity_web_in_app);


        wb = findViewById(R.id.web_id);


        Intent i = getIntent();
        String link = i.getStringExtra("link");

        wb.getSettings().setJavaScriptEnabled(true);
        wb.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                mProgressDialog = new ProgressDialog(WebInApp.this);
                mProgressDialog.setTitle("1ML - Our Products");
                mProgressDialog.setMessage("Loading...");
                mProgressDialog.setIndeterminate(false);
                try{
                    mProgressDialog.show();
                }catch (WindowManager.BadTokenException e) {
                    System.out.println(e);
                }
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                mProgressDialog.dismiss();
                wb.loadUrl("javascript:(function() { " +
                        "document.getElementsByTagName('header'); " +
                        "})()");
            }
        });
        wb.loadUrl(link);
    }


}