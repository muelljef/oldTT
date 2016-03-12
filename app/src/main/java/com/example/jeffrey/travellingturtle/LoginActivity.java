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

public class LoginActivity extends AppCompatActivity {

    EditText loginUsernameText;
    EditText loginPasswordText;
    String username;
    String userId;
    String password;

    public void login(View view) {

        username = loginUsernameText.getText().toString().trim();
        password = loginPasswordText.getText().toString().trim();

        LoginUserTask loginTask = new LoginUserTask();
        String url = null;
        try {
            url = "http://54.206.28.148/api/v1/users/" + URLEncoder.encode(username, "UTF-8");
            url += "?password=" + URLEncoder.encode(password, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        loginTask.execute(url);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginUsernameText = (EditText) findViewById(R.id.loginUsernameText);
        loginPasswordText = (EditText) findViewById(R.id.loginPasswordText);

    }

    public class LoginUserTask extends AsyncTask<String, Void, String> {
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

            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(result);

                //FAILURE
                if(jsonObject.has("error")) {
                    Toast.makeText(LoginActivity.this, "Sorry an error occurred", Toast.LENGTH_SHORT).show();
                    return;
                }

                //SUCCESS
                Toast.makeText(LoginActivity.this, "Login Success", Toast.LENGTH_SHORT).show();
                JSONObject dataObject = jsonObject.getJSONObject("data");
                userId = dataObject.getString("id");
                Log.i("id: ", userId);

                Intent intent = new Intent(getApplicationContext(), ListTrips.class);
                intent.putExtra("userId", userId);
                intent.putExtra("username", username);
                intent.putExtra("password", password);
                startActivity(intent);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

}
