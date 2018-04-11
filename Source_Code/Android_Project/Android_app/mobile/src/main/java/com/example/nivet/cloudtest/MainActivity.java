/**
 * File: MainActivity.java
 * Authors: Abhishek Manoj Sharma, Nivetha Vijayaraju, Parth Jayantilal Jain
 * Date Last Modified: 12/04/2017
 */

package com.example.nivet.cloudtest;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {
    EditText et;
    /**
     *
     * onCreate method is the default method invoked when this intent is invoked by the Android device
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        et = findViewById(R.id.editText);
    }

    /**
     *
     * onClickMethod calls the MobileActivity.class for the name provided in the textbox before
     * pressing the Submit button.
     */
    public void onClickButton(View v)
    {
        Intent launchResult = new Intent(MainActivity.this, MobileActivity.class);
        launchResult.putExtra("name", String.valueOf(et.getText()));
        startActivity(launchResult);
    }
}
