package com.example.jeffrey.travellingturtle;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class LoginActivity extends AppCompatActivity {

    EditText loginUsernameText;
    EditText loginPasswordText;

    public void login(View view) {
        String url = "http://54.206.28.148/api/v1/users?";

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginUsernameText = (EditText) findViewById(R.id.loginUsernameText);
        loginPasswordText = (EditText) findViewById(R.id.loginPasswordText);
    }
    
}
