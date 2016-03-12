package com.example.jeffrey.travellingturtle;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class AddLocationActivity extends AppCompatActivity {

    TextView tripLocationsView;
    TextView latitudeView;
    TextView longitudeView;
    TextView tripTitleView;
    String username;
    String password;
    String tripId;
    String tripTitle;
    String tripLocations;

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
    }
}
