package com.markmwaura.weatherapp;


import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import com.markmwaura.weatherapp.Utils.DummyContent;
import com.markmwaura.weatherapp.Utils.JSONWeatherParser;
import com.markmwaura.weatherapp.Utils.pulltozoom.PullToZoomListViewEx;
import com.markmwaura.weatherapp.adapter.ListViewAdapter;
import com.markmwaura.weatherapp.model.Weather;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by mark on 5/17/17.
 */
public class MainActivity extends AppCompatActivity {

    public static final String TAG = "Parallax media";

    ImageView iv;
    String url = "http://api.openweathermap.org/data/2.5/weather?appid=3596876d5222c1b0523f750728acf3db&q=";
    android.location.Location location;

    String longitude, latitude;
    TextView tv_city, tv_lat_long, tv_celsius, tv_fahrenheit, tv_date, tv_time;
    MyLocationListener mMyLocationListener;
    Weather weather;
    ProgressBar main_progress;
    private LocationManager locationMangaer = null;
    private LocationListener locationListener = null;
    private Boolean flag = false;
    ImageView header_parallax_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv_city = (TextView) findViewById(R.id.header_parallax_City);
        tv_lat_long = (TextView) findViewById(R.id.header_parallax_lat_long);
        tv_celsius = (TextView) findViewById(R.id.header_parallax_celsius);
        tv_fahrenheit = (TextView) findViewById(R.id.header_parallax_fahrenheit);
        tv_date = (TextView) findViewById(R.id.header_parallax_date);
        tv_time = (TextView) findViewById(R.id.header_parallax_time);
        main_progress = (ProgressBar) findViewById(R.id.mainprogressBar);
        header_parallax_image = (ImageView) findViewById(R.id.header_parallax_image);

        PullToZoomListViewEx listView = (PullToZoomListViewEx) findViewById(R.id.paralax_media_list_view);
        listView.setShowDividers(0);
        listView.setAdapter(new ListViewAdapter(this, DummyContent
                .getDummyModelList()));
        LocationPrefs();
        Toast.makeText(MainActivity.this, "Getting your Weather details", Toast.LENGTH_LONG).show();


    }

    private void LocationPrefs() {

        locationMangaer = (LocationManager)
                getSystemService(Context.LOCATION_SERVICE);


        flag = displayGpsStatus();
        if (flag) {


            locationListener = new MyLocationListener();


            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                locationMangaer.requestLocationUpdates(LocationManager
                        .GPS_PROVIDER, 7000, 7, locationListener);
                Log.i("location on ", "location on ");


            }


        } else {
            Log.w("Location Gps Status!!", "Your GPS is: OFF");
        }

    }

    private Boolean displayGpsStatus() {
        ContentResolver contentResolver = getBaseContext()
                .getContentResolver();
        boolean gpsStatus = Settings.Secure
                .isLocationProviderEnabled(contentResolver,
                        LocationManager.GPS_PROVIDER);
        if (gpsStatus) {
            return true;

        } else {
            return false;
        }
    }

    public void newRequest(String url) {


        JsonObjectRequest jsonReq = new JsonObjectRequest(Request.Method.GET, url,
                null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                if (response != null) {

                    weather = new Weather();
                    try {
                        main_progress.setVisibility(View.GONE);
                        weather = JSONWeatherParser.getWeather(response);
                        final String DEGREE = "\u00b0";
                        tv_celsius.setText(Math.round((weather.temperature.getTemp() - 273.15)) + DEGREE + " Celsius");
                        tv_fahrenheit.setText(weather.temperature.getTemp() + " Fahrenheit");

                        tv_city.setText(weather.location.getCity() + " City");
                        tv_lat_long.setText(" Latitude : " + weather.location.getLatitude() + " Longitude : " + weather.location.getLongitude());

                        Date date = new Date();
                        DateFormat df = new SimpleDateFormat(" dd-MM-yyyy ");

                        DateFormat timeformat = new SimpleDateFormat(" HH:mm ");


                        tv_date.setText("Date Today : " + df.format(date));

                        tv_time.setText("Time : " + timeformat.format(date));

                        if(weather.currentCondition.getCondition().contains("cloud")){
                            Picasso.with(MainActivity.this)
                                    .load(R.drawable.cloudy)
                                    .into(header_parallax_image);


                        }else if(weather.currentCondition.getCondition().contains("rain")){
                            Picasso.with(MainActivity.this)
                                    .load(R.drawable.rainy)
                                    .into(header_parallax_image);


                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        WeatherApplication.getInstance().getRequestQueue().add(jsonReq);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        locationMangaer.removeUpdates(locationListener);
        Log.i(TAG, "onPause, done");
    }


    /*----------Listener class to get coordinates ------------- */
    public class MyLocationListener implements LocationListener {

        public String cityName;

        @Override
        public void onLocationChanged(android.location.Location loc) {


    /*----------to get City-Name from coordinates ------------- */


            Geocoder gcd = new Geocoder(getBaseContext(),
                    Locale.getDefault());

            List<Address> addresses;
            try {
                addresses = gcd.getFromLocation(loc.getLatitude(), loc
                        .getLongitude(), 1);
                Log.i("location city", addresses.toString());
                if (addresses.size() > 0)

                {
                    System.out.println(addresses.get(0).getLocality());

                }
                cityName = addresses.get(0).getLocality();

            } catch (IOException e) {
                e.printStackTrace();
            }

            String s = longitude + "\n" + latitude +
                    "\n\nMy Currrent City is: " + cityName;
            Log.i("location is", s);

            newRequest(url + cityName);


        }

        @Override
        public void onProviderDisabled(String provider) {
            // TODO Auto-generated method stub
        }

        @Override
        public void onProviderEnabled(String provider) {
            // TODO Auto-generated method stub
        }

        @Override
        public void onStatusChanged(String provider,
                int status, Bundle extras) {
            // TODO Auto-generated method stub
        }

    }
}
