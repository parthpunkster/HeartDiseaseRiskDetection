/**
 * File: MobileActivity.java
 * Authors: Abhishek Manoj Sharma, Nivetha Vijayaraju, Parth Jayantilal Jain
 * Date Last Modified: 12/04/2017
 */

package com.example.nivet.cloudtest;

import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Timer;
import java.util.TimerTask;

public class MobileActivity extends AppCompatActivity {
    private DatabaseReference mdatabase;
    private String userName = "";
    String s = "";
    ProgressBar simpleProgressBar; // initiate the progress bar
    TextView tv4;
    TextView tv3;

    /**
     *
     * onCreate method is the default method invoked when this intent is invoked by the Android device
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Bundle extras = getIntent().getExtras();
        userName = extras.getString("name");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobile);
        simpleProgressBar =findViewById(R.id.progressBar2);
        tv4 = findViewById(R.id.textView4);
        tv4.setText(userName+"'s heart healt status");
        tv3 = findViewById(R.id.textView3);
        simpleProgressBar.setMax(3);
        mdatabase = FirebaseDatabase.getInstance().getReference().child("User");
        s = mdatabase.child(userName).child("Last_Prediction").toString();
        s+=".json";
        Log.d("CS256 Prediction URL",s);
        mdatabase.child(userName).child("Last_Prediction").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                    String name = dataSnapshot.getValue().toString();
                    if(name.equals("0") || name.equals("1") || name.equals("2")){
                        simpleProgressBar.setProgress(1);
                    }
                    else if(name.equals("3")) {
                        simpleProgressBar.setProgress(2);
                    }
                    else if(name.equals("4")) {
                        simpleProgressBar.setProgress(3);
                    }
                    else if(name.equals("10")) {
                    }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        setRepeatingAsyncTask();
    }

    /**
     * setRepeatingAsyncTask() method is used for updating the current pulse rate and progress bar on the UI.
     */
    private void setRepeatingAsyncTask() {
        final Handler handler = new Handler();
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        try {
                            String s2 = mdatabase.child(userName).child("Resting_pulse_rate").toString();
                            s2 +=".json";
                            String t = new RetrievePrediction().execute(s,s2).get();
                            String temp [] = t.split(",");
                            tv3.setText("Current Pulse Rate: " + temp[1]);
                            float name = Float.parseFloat(temp[0]);
                            if(name==0.0 || name==1.0 || name==2.0){
                                simpleProgressBar.setProgress(1);
                            }
                            else if(name==3.0) {
                                simpleProgressBar.setProgress(2);
                            }
                            else if(name==4.0) {
                                simpleProgressBar.setProgress(3);
                            }
                        } catch (Exception e) {}
                    }
                });
            }
        };
        timer.schedule(task, 0, 5*1000);
    }

}

/**
 * RetrievePrediction class extends an AsyncTask so that it runs on the background.
 * It reads the update Pulse Rate and Predicted value for the user and returns it as a comma separated string.
 */
class RetrievePrediction extends AsyncTask<String, Void, String> {
    protected String doInBackground(String... urls) {
        try {
            URL url = new URL(urls[0]);
            URLConnection yc = url.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
            String string_to_return = "";
            String inputLine;
            inputLine = in.readLine();
            string_to_return = inputLine + ",";
            URL url2 = new URL(urls[1]);
            Log.d("CS256 Pulse Rate URL", urls[1]);
            URLConnection yc2 = url2.openConnection();
            BufferedReader in2 = new BufferedReader(new InputStreamReader(yc2.getInputStream()));
            inputLine = in2.readLine();
            string_to_return+=inputLine;
            return string_to_return;
        } catch (Exception e) {
            Log.d("CS256 Exception", String.valueOf(e));
            return null;
        }
    }
}