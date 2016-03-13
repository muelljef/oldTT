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

public class RegisterActivity extends AppCompatActivity {

    EditText registerUsernameText;
    EditText registerPasswordText;
    String username;
    String password;

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

        Log.i("URL", url);

        RegisterUserTask registerTask = new RegisterUserTask();
        registerTask.execute(url);

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

    public class RegisterUserTask extends AsyncTask<String, Void, String> {

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


            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(result);

                //FAILURE
                if(jsonObject.has("error")) {
                    Toast.makeText(RegisterActivity.this, "Sorry an error occurred", Toast.LENGTH_SHORT).show();
                    return;
                }

                //SUCCESS
                JSONObject dataObject = jsonObject.getJSONObject("data");
                if(dataObject.has("id")) {
                    Toast.makeText(RegisterActivity.this, "Registration was successful, please proceed to the Login Page", Toast.LENGTH_SHORT).show();
                    registerUsernameText.setText("");
                    registerPasswordText.setText("");
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
