package com.app.paul.galleryapp;

import android.app.Application;

public class MyApplication extends Application {

    private static MyApplication myApplication;



    public static MyApplication getInstance() {
        return myApplication;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        myApplication = this;
    }

}
