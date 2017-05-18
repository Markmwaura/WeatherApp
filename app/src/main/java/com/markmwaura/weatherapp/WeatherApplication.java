package com.markmwaura.weatherapp;

import android.app.Application;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by mark on 5/18/17.
 */

public class WeatherApplication extends Application {
    public static final String TAG = WeatherApplication.class.getSimpleName();
    public static WeatherApplication mInstance;
    private RequestQueue mRequestQueue;

    public static synchronized WeatherApplication getInstance() {
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mInstance = this;


    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }


}
