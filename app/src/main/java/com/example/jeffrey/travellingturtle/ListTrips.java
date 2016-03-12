package com.example.jeffrey.travellingturtle;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class ListTrips extends AppCompatActivity {

    String userId;
    String password;
    String username;
//    ListView tripsView;
//    ArrayAdapter arrayAdapter;
//    ArrayList<String> trips;
//    ArrayList<String> ids;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_trips);

        Intent intent = getIntent();
        userId = intent.getStringExtra("userId");
        password = intent.getStringExtra("password");
        Log.i("User", userId);
        Log.i("password", password);

        TextView usernameView = (TextView) findViewById(R.id.usernameView);
        usernameView.setText(username);

//        tripsView = (ListView) findViewById(R.id.tripsView);
//        trips = new ArrayList<String>();
//        ids = new ArrayList<String>();
//        arrayAdapter = new ArrayAdapter(ListTrips.this, android.R.layout.simple_list_item_1, trips);
//        tripsView.setAdapter(arrayAdapter);
//        tripsView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
//                Intent i = new Intent(getApplicationContext(), TripActivity.class);
//                i.putExtra("tripName", trips.get(position));
//                i.putExtra("id", ids.get(position));
//                startActivity(i);
//            }
//        });
    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//
//        //trips.clear();
//
//        GetTripsTask task = new GetTripsTask();
//        String url = "http://54.206.28.148/api/v1/trips";
//        try {
//            url += "?password=" + URLEncoder.encode(password, "UTF-8");
//            url += "?username=" + URLEncoder.encode(username, "UTF-8");
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
//        task.execute(url);
//    }

    public class GetTripsTask extends AsyncTask<String, Void, String> {

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

        //This gets passed the result from doInBackground method
        //The do in background method cannot interact with the UI so we do it here
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            Log.i("result", result);
//            try {
//                JSONArray jsonTrips = new JSONArray(result);
//                if (jsonTrips.isNull(0)) {
//                    Toast.makeText(ListTrips.this, "Sorry there was an error", Toast.LENGTH_LONG).show();
//                } else {
//
//                    for (int i = 0; i < jsonTrips.length(); i++) {
//                        JSONObject trip = jsonTrips.getJSONObject(i);
//                        trips.add(trip.getString("name"));
//                        ids.add(trip.getString("_id"));
//                    }
//
//                    Log.i("name array", trips.toString());
//                    Log.i("id array", ids.toString());
//
//                    arrayAdapter.notifyDataSetChanged();
//                }
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }

        }
    }

}
