package com.example.jeffrey.travellingturtle;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class RegisterActivity extends AppCompatActivity {

    EditText registerUsernameText;
    EditText registerPasswordText;

    public void register(View view) {

        String username = registerUsernameText.getText().toString().trim();
        String password = registerPasswordText.getText().toString().trim();
        String url = "http://54.206.28.148/api/v1/users?";

        String errorMsg = "";

        if (username.length() == 0) {
            errorMsg += "You did not enter a username. ";
        }
        if (password.length() == 0) {
            errorMsg += "You did not enter a password. ";
        }
        if(errorMsg.length() > 0) {
            Toast.makeText(RegisterActivity.this, errorMsg, Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            url = url + "username=" + URLEncoder.encode(username, "UTF-8");
            url = url + "&password=" + URLEncoder.encode(password, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        Toast.makeText(RegisterActivity.this, url, Toast.LENGTH_SHORT).show();

    }

    public void goToLoginPage(View view) {
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        registerUsernameText = (EditText) findViewById(R.id.registerUsernameText);
        registerPasswordText = (EditText) findViewById(R.id.registerPasswordText);
    }

    public class RegisterUser extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            String result = "";
            InputStream in = null;
            URL url = null;
            int status;

            try {
                url = new URL("http://www.android.com/");
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setDoOutput(true);
                //status = urlConnection.getResponseCode();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }
    }
}
