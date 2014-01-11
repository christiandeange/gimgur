package com.deange.gimgur;

import android.app.Application;
import android.util.Log;

import com.deange.gimgur.misc.GsonController;
import com.squareup.picasso.Picasso;

public class GIApplication extends Application {

    private static final String TAG = GIApplication.class.getSimpleName();

    @Override
    public void onCreate() {
        Log.v(TAG, "onCreate()");
        super.onCreate();

        // Force initialize the gson singleton
        GsonController.getInstance();

        new Picasso.Builder().memoryCache()
    }

}
