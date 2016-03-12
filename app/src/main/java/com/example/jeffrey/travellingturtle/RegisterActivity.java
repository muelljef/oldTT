package com.example.jeffrey.travellingturtle;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterActivity extends AppCompatActivity {

    public void register(View view) {

        EditText registerUsernameText = (EditText) findViewById(R.id.registerUsernameText);
        String username = registerUsernameText.getText().toString().trim();

        EditText registerPasswordText = (EditText) findViewById(R.id.registerPasswordText);
        String password = registerPasswordText.getText().toString().trim();

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

        Toast.makeText(RegisterActivity.this, "Success", Toast.LENGTH_SHORT).show();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
    }
}
