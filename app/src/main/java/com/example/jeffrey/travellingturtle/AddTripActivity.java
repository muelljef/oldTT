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

public class AddTripActivity extends AppCompatActivity {

    String username;
    String password;
    EditText titleText;
    EditText descriptionText;

    public void addTrip(View view) {
        String title = titleText.getText().toString().trim();
        String description = descriptionText.getText().toString().trim();
        String url = "http://54.206.28.148/api/v1/trips?";

        String errorMsg = "";
        if (title.length() == 0) {
            errorMsg += "You did not enter a title. ";
        }
        if (description.length() == 0) {
            errorMsg += "You did not enter a description. ";
        }
        if(errorMsg.length() > 0) {
            Toast.makeText(AddTripActivity.this, errorMsg, Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            url = url + "username=" + URLEncoder.encode(username, "UTF-8");
            url = url + "&password=" + URLEncoder.encode(password, "UTF-8");
            url = url + "&title=" + URLEncoder.encode(title, "UTF-8");
            url = url + "&description=" + URLEncoder.encode(description, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        Log.i("URL", url);

        AddTripTask addTripTask = new AddTripTask();
        addTripTask.execute(url);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_trip);

        Intent intent = getIntent();
        username = intent.getStringExtra("username");
        password = intent.getStringExtra("password");

        titleText = (EditText) findViewById(R.id.titleText);
        descriptionText = (EditText) findViewById(R.id.descriptionText);
    }

    public class AddTripTask extends AsyncTask<String, Void, String> {

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
                    Toast.makeText(AddTripActivity.this, "Sorry an error occurred", Toast.LENGTH_SHORT).show();
                    return;
                }

                //SUCCESS
                JSONObject dataObject = jsonObject.getJSONObject("data");
                if(dataObject.has("id")) {
                    Toast.makeText(AddTripActivity.this, "Addition of the trip was successful", Toast.LENGTH_SHORT).show();
                }
                titleText.setText("");
                descriptionText.setText("");
                finish();

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
