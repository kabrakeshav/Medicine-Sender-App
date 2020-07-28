package com.keshavoneml.oneml_customer;


// class to load and save the BITMAP of prescription

import android.graphics.Bitmap;

public class BitmapHelper {
    private Bitmap bitmap = null;
    private static final BitmapHelper instance = new BitmapHelper();


    public BitmapHelper() {
    }

    public static BitmapHelper getInstance() {
        return instance;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }
}
