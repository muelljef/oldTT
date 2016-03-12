package com.example.jeffrey.travellingturtle;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
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

public class UpdateTripActivity extends AppCompatActivity {

    String username;
    String password;
    String tripId;
    String tripTitle;
    String tripDescription;
    EditText tripTitleText;
    EditText tripDescriptionText;

    public void updateTrip(View view) {
        tripTitle = tripTitleText.getText().toString().trim();
        tripDescription = tripDescriptionText.getText().toString().trim();

        UpdateTripTask updateTripTask = new UpdateTripTask();
        String url = "http://54.206.28.148/api/v1/trips/";
        try {
            url += URLEncoder.encode(tripId, "UTF-8");
            url += "?password=" + URLEncoder.encode(password, "UTF-8");
            url += "&username=" + URLEncoder.encode(username, "UTF-8");
            url += "&title=" + URLEncoder.encode(tripTitle, "UTF-8");
            url += "&description=" + URLEncoder.encode(tripDescription, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        Log.i("PUT URL", url);
        updateTripTask.execute(url);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_trip);

        tripTitleText = (EditText) findViewById(R.id.tripTitleText);
        tripDescriptionText = (EditText) findViewById(R.id.tripDescriptionText);

        Intent intent = getIntent();
        username = intent.getStringExtra("username");
        password = intent.getStringExtra("password");
        tripId = intent.getStringExtra("tripId");
        tripTitle = intent.getStringExtra("tripTitle");
        tripDescription = intent.getStringExtra("tripDescription");

        tripTitleText.setText(tripTitle);
        tripDescriptionText.setText(tripDescription);
    }

    public class UpdateTripTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            String result = "";
            URL url;
            HttpURLConnection urlConnection = null;
            String charset = "UTF-8";

            try {
                url = new URL(urls[0]);

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("PUT");

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
            Log.i("PUT result", result);

            if(result != null) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    //FAILURE
                    if(jsonObject.has("error")) {
                        Toast.makeText(UpdateTripActivity.this, "Sorry an error occurred", Toast.LENGTH_SHORT).show();
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
