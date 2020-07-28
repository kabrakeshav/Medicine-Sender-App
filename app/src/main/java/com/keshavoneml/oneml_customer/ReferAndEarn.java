package com.keshavoneml.oneml_customer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

public class ReferAndEarn extends AppCompatActivity {

    ImageButton imgBtnWhatsup,imgBtnShare;
    private static final int REQUEST_INVITE = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refer_and_earn);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        imgBtnWhatsup = findViewById(R.id.imgBtnWhatsup);
        imgBtnShare = findViewById(R.id.imgBtnShare);
    }

    public void clickFunction(View view) {
        switch (view.getId())
        {
            case R.id.imgBtnWhatsup:
                sendWhatsAppMsg();
                break;
            case R.id.imgBtnShare:
                onInviteUsingShare();
                break;
        }
    }

    public void sendWhatsAppMsg() {
//        Toast.makeText(getApplicationContext(), "Under Construction", Toast.LENGTH_SHORT).show();
        final Intent emailIntent = new Intent(Intent.ACTION_SEND);

        emailIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name));
//        emailIntent.putExtra(Intent.EXTRA_TEXT, "I've found a trip in Voyajo website that might be interested you, http://www.voyajo.com/viewTrip.aspx?trip=" );
        emailIntent.setType("text/plain");
        emailIntent.setPackage("com.whatsapp");
//        startActivity(Intent.createChooser(emailIntent, "Send to friend"));

        String strShareMessage;
        strShareMessage = " ---  ONEML  ---\n\nLet me recommend you this app...\nMy referral code is AB1234\n\n";
        strShareMessage = strShareMessage + "https://play.google.com/store/apps/details?id=com.keshavoneml.oneml_customer&hl=en";

        emailIntent.putExtra(Intent.EXTRA_TEXT, strShareMessage);
        startActivity(Intent.createChooser(emailIntent, "Send to friend"));

    }

    public void onInviteUsingShare() {
        final Intent emailIntent = new Intent(Intent.ACTION_SEND);

        emailIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name));
        emailIntent.setType("text/plain");

        String strShareMessage;
        strShareMessage = " ---  ONEML  ---\n\nLet me recommend you this app...\nMy referral code is AB1234\n\n";
        strShareMessage = strShareMessage + "https://play.google.com/store/apps/details?id=com.keshavoneml.oneml_customer&hl=en";

        emailIntent.putExtra(Intent.EXTRA_TEXT, strShareMessage);
        startActivity(Intent.createChooser(emailIntent, "Send to friend"));
    }

}
