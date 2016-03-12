package com.example.jeffrey.travellingturtle;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
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

public class TripActivity extends AppCompatActivity {

    TextView tripTitleView;
    TextView tripDescriptionView;
    String tripTitle;
    String tripId;
    String username;
    String password;
    String tripDescription;

    public void deleteTrip(View view) {

        DeleteTripTask deleteTripTask = new DeleteTripTask();
        String url = "http://54.206.28.148/api/v1/trips/";
        try {
            url += URLEncoder.encode(tripId, "UTF-8");
            url += "?password=" + URLEncoder.encode(password, "UTF-8");
            url += "&username=" + URLEncoder.encode(username, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        deleteTripTask.execute(url);

    }

    public void updateTripPage(View view) {

        //Intent updateIntent = new Intent(getApplicationContext(), );

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip);

        tripTitleView = (TextView) findViewById(R.id.tripTitleView);
        tripDescriptionView = (TextView) findViewById(R.id.tripDescriptionView);

        Intent intent = getIntent();
        tripTitle = intent.getStringExtra("tripTitle");
        tripId = intent.getStringExtra("tripId");
        username = intent.getStringExtra("username");
        password = intent.getStringExtra("password");

        String url = "http://54.206.28.148/api/v1/trips/";
        try {
            url += URLEncoder.encode(tripId, "UTF-8");
            url += "?password=" + URLEncoder.encode(password, "UTF-8");
            url += "&username=" + URLEncoder.encode(username, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        Log.i("URL", url);
        GetTripTask getTripTask = new GetTripTask();
        getTripTask.execute(url);
    }

    public class GetTripTask extends AsyncTask<String, Void, String> {

        //get the data from the url the request
        @Override
        protected String doInBackground(String... urls) {

            String result = "";
            URL url;
            HttpURLConnection urlConnection = null;

            try {
                url = new URL(urls[0]);

                urlConnection = (HttpURLConnection) url.openConnection();
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
            try {
                JSONObject jsonObject = new JSONObject(result);

                //FAILURE
                if (jsonObject.has("error")) {
                    Toast.makeText(TripActivity.this, "Sorry there was an error", Toast.LENGTH_LONG).show();
                }

                //SUCCESS
                JSONObject dataObject = jsonObject.getJSONObject("data");
                tripTitle = dataObject.getString("title");
                tripDescription = dataObject.getString("description");
                tripTitleView.setText(tripTitle);
                tripDescriptionView.setText(tripDescription);

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    public class DeleteTripTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            String result = "";
            URL url;
            HttpURLConnection urlConnection = null;
            String charset = "UTF-8";

            try {
                url = new URL(urls[0]);

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("DELETE");

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

            if(result != null) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    //FAILURE
                    if(jsonObject.has("error")) {
                        Toast.makeText(TripActivity.this, "Sorry an error occurred", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    //SUCCESS
                    finish();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
