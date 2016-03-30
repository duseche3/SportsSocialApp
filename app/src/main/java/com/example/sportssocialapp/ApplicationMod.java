package com.example.sportssocialapp;

import android.app.Application;

import com.firebase.client.Firebase;

public class ApplicationMod extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Firebase.setAndroidContext(this);
    }
}
