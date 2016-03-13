package com.example.jeffrey.travellingturtle;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.DecimalFormat;

public class AddLocationActivity extends AppCompatActivity implements LocationListener {

    TextView tripLocationsView;
    TextView latitudeView;
    TextView longitudeView;
    TextView tripTitleView;
    String username;
    String password;
    String tripId;
    String tripTitle;
    String tripLocations;
    LocationManager locationManager;
    String provider;

    public void addLocation(View view) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Location location = locationManager.getLastKnownLocation(provider);
        if(location != null) {
            Double lat = location.getLatitude();
            Double lng = location.getLongitude();
            DecimalFormat df = new DecimalFormat("0.00");
            String addLocation = df.format(lat) + " " + df.format(lng);

            try {
                String url = "http://54.206.28.148/api/v1/trips/" + URLEncoder.encode(tripId, "UTF-8");
                url += "/locations?username=" + URLEncoder.encode(username, "UTF-8");
                url += "&password=" + URLEncoder.encode(password, "UTF-8");
                url += "&location=" + URLEncoder.encode(addLocation, "UTF-8");
                AddTripLocationTask addLocationTask = new AddTripLocationTask();
                addLocationTask.execute(url);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(AddLocationActivity.this, "Error: no location details", Toast.LENGTH_LONG).show();
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_location);

        tripTitleView = (TextView) findViewById(R.id.tripTitleView);
        tripLocationsView = (TextView) findViewById(R.id.tripLocationsView);
        latitudeView = (TextView) findViewById(R.id.latitudeView);
        longitudeView = (TextView) findViewById(R.id.longitudeView);

        Intent intent = getIntent();
        username = intent.getStringExtra("username");
        password = intent.getStringExtra("password");
        tripId = intent.getStringExtra("tripId");
        tripTitle = intent.getStringExtra("tripTitle");
        tripLocations = intent.getStringExtra("tripLocations");
        tripTitleView.setText(tripTitle);
        tripLocationsView.setText(tripLocations);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        provider = locationManager.getBestProvider(new Criteria(), false);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Location location = locationManager.getLastKnownLocation(provider);
        if (location != null) {
            Double lat = location.getLatitude();
            Double lng = location.getLongitude();
            DecimalFormat df = new DecimalFormat("0.00");

            latitudeView.setText(df.format(lat));
            longitudeView.setText(df.format(lng));
        }
        //location through locationManager.getLastKnownLocation(provider) is null at onCreate
        //in virtual machine (not an issue with an actual device)
        //so it is handled in update and addGeolocation
        //I have to "telnet localhost 5554" then "geo fix 10 10" to set the location
        //the getLastKnownLocation(provider) will work once the location is set through telnet
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationManager.requestLocationUpdates(provider, 400, 1, this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationManager.removeUpdates(this);
    }

    @Override
    public void onLocationChanged(Location location) {
        Double lat = location.getLatitude();
        Double lng = location.getLongitude();
        DecimalFormat df = new DecimalFormat("0.00");

        Log.i("Latitude", df.format(lat));
        latitudeView.setText(df.format(lat));
        longitudeView.setText(df.format(lng));
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    public class AddTripLocationTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            String result = "";
            URL url;
            HttpURLConnection urlConnection = null;
            String charset = "UTF-8";

            try {
                url = new URL(urls[0]);

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("POST");
                urlConnection.setDoOutput(true);

                int status = urlConnection.getResponseCode();
                InputStream in;
                if (status >= 400) {
                    in = urlConnection.getErrorStream();
                } else {
                    in = urlConnection.getInputStream();
                }

                InputStreamReader reader = new InputStreamReader(in);

                int data = reader.read();

                while (data != -1) {
                    char current = (char) data;
                    result += current;
                    data = reader.read();
                }

                return result;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            Log.i("result", result);
            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(result);

                //FAILURE
                if(jsonObject.has("error")) {
                    Toast.makeText(AddLocationActivity.this, "Sorry an error occurred", Toast.LENGTH_SHORT).show();
                    return;
                }

                //SUCCESS
                JSONObject dataObject = jsonObject.getJSONObject("data");
                if(dataObject.has("id")) {
                    Toast.makeText(AddLocationActivity.this, "Addition of the trip location was successful", Toast.LENGTH_SHORT).show();
                }
                finish();

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
