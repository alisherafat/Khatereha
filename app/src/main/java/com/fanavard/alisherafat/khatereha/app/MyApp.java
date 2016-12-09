package com.fanavard.alisherafat.khatereha.app;

import android.app.Application;

import com.activeandroid.ActiveAndroid;

/**
 * Application initializer for loading ORM and use it across application
 */
public class MyApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        ActiveAndroid.initialize(this);
    }
}
