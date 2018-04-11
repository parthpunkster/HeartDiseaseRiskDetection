/**
 * File: WearActivity.java
 * Authors: Abhishek Manoj Sharma, Nivetha Vijayaraju, Parth Jayantilal Jain
 * Date Last Modified: 12/04/2017
 */

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.wearable.activity.WearableActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

public class WearActivity extends WearableActivity {
    Handler mHandler = new Handler();
    String user_name = "";
    int gender = 0;
    int age = 0;
    int cp = 0;
    int dm = 0;
    int famhist = 0;
    int cigs_per_day = 0;
    int slope = 0;
    float rest_bps = 0;
    float cholestrol = 0;
    float fbs = 0;
    float tpeakbps = 0;
    float tpeakbpd = 0;
    float trestbpd = 0;
    float oldpeak = 0;
    int rest_ecg = 0;
    int exang = 0;
    int ca = 0;
    int smoking_years = 0;
    int pulse_rate = 0;
    int pulse_rate_max = 0;
    int pulse_rate_rest = 0;
    int DEFAULT_FLAG_BIT = 1;
    String b = "";
    /**
     *
     * onCreate method is the default method invoked when this intent is invoked by the Android device
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_form);
        setAmbientEnabled();
    }

    /**
     *
     * readValues() method reads all the attributes for the current user and stores them in an object
     * to send to Firebase.
     */
    public void readValues(View v)throws Exception
    {
        try
        {
            user_name = ((EditText)findViewById(R.id.name)).getText().toString();
            age = Integer.parseInt(((EditText)findViewById(R.id.age)).getText().toString());
            rest_bps = Float.parseFloat(((EditText)findViewById(R.id.rest_bps)).getText().toString());
            cholestrol = Float.parseFloat(((EditText)findViewById(R.id.chol)).getText().toString());
            smoking_years = Integer.parseInt(((EditText)findViewById(R.id.years_as_smoker)).getText().toString());
            cigs_per_day = Integer.parseInt(((EditText)findViewById(R.id.cigs_per_day)).getText().toString());
            fbs = Float.parseFloat(((EditText)findViewById(R.id.fbs)).getText().toString());
            tpeakbps = Float.parseFloat(((EditText)findViewById(R.id.peak_exercise_bp1)).getText().toString());
            tpeakbpd = Float.parseFloat(((EditText)findViewById(R.id.peak_exercise_bp2)).getText().toString());
            trestbpd = Float.parseFloat(((EditText)findViewById(R.id.Resting_bp)).getText().toString());
            oldpeak = Float.parseFloat(((EditText)findViewById(R.id.oldpeak)).getText().toString());
            String selectedtext = ((RadioButton)findViewById(((RadioGroup)findViewById(R.id.sex)).
                    getCheckedRadioButtonId())).getText().toString();
        }
        catch (Exception e)
        {
            Toast.makeText(WearActivity.this,"ABC",Toast.LENGTH_LONG);
            TextView status_text = findViewById(R.id.status_text);
            status_text.setTextColor(Color.parseColor("#FF0000"));
            status_text.setText("One or more values left blank");
            Log.d("Toast Generated","ABC");
            return;
        }
        TextView status_text = findViewById(R.id.status_text);
        status_text.setTextColor(Color.parseColor("#00FF00"));
        status_text.setText("Connected and sending values to server");

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(3000);
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                user_name = ((EditText)findViewById(R.id.name)).getText().toString();
                                age = Integer.parseInt(((EditText)findViewById(R.id.age)).getText().toString());
                                rest_bps = Float.parseFloat(((EditText)findViewById(R.id.rest_bps)).getText().toString());
                                cholestrol = Float.parseFloat(((EditText)findViewById(R.id.chol)).getText().toString());
                                smoking_years = Integer.parseInt(((EditText)findViewById(R.id.years_as_smoker)).getText().toString());
                                cigs_per_day = Integer.parseInt(((EditText)findViewById(R.id.cigs_per_day)).getText().toString());
                                fbs = Float.parseFloat(((EditText)findViewById(R.id.fbs)).getText().toString());
                                tpeakbps = Float.parseFloat(((EditText)findViewById(R.id.peak_exercise_bp1)).getText().toString());
                                tpeakbpd = Float.parseFloat(((EditText)findViewById(R.id.peak_exercise_bp2)).getText().toString());
                                trestbpd = Float.parseFloat(((EditText)findViewById(R.id.Resting_bp)).getText().toString());
                                oldpeak = Float.parseFloat(((EditText)findViewById(R.id.oldpeak)).getText().toString());
                                String selectedtext = ((RadioButton)findViewById(((RadioGroup)findViewById(R.id.sex)).
                                        getCheckedRadioButtonId())).getText().toString();
                                if(selectedtext.equals("Male"))
                                {
                                    gender = 1;
                                }
                                else if(selectedtext.equals("Female"))
                                {
                                    gender = 0;
                                }
                                String catext = ((RadioButton)findViewById(((RadioGroup)findViewById(R.id.no_vessels_coloured)).
                                        getCheckedRadioButtonId())).getText().toString();
                                ca = Integer.parseInt(catext);
                                String slopetext = ((RadioButton)findViewById(((RadioGroup)findViewById(R.id.st_seg_slope)).
                                        getCheckedRadioButtonId())).getText().toString();
                                if(slopetext.equals("Upslope"))
                                {
                                    slope = 1;
                                }
                                else if(slopetext.equals("Flat"))
                                {
                                    slope = 2;
                                }
                                else if(slopetext.equals("Downslope"))
                                {
                                    slope = 3;
                                }
                                String dmtext = ((RadioButton)findViewById(((RadioGroup)findViewById(R.id.diabetes_fam_history)).
                                        getCheckedRadioButtonId())).getText().toString();
                                if(dmtext.equals("Yes"))
                                {
                                    dm = 1;
                                }
                                else if(dmtext.equals("No"))
                                {
                                    dm = 0;
                                }
                                String exang_text = ((RadioButton)findViewById(((RadioGroup)findViewById(R.id.exang)).
                                        getCheckedRadioButtonId())).getText().toString();
                                if(exang_text.equals("Yes"))
                                {
                                    exang = 1;
                                }
                                else if(dmtext.equals("No"))
                                {
                                    exang = 0;
                                }
                                String restecg_text = ((RadioButton)findViewById(((RadioGroup)findViewById(R.id.rest_ecg)).
                                        getCheckedRadioButtonId())).getText().toString();
                                if(restecg_text.equals("Normal"))
                                {
                                    rest_ecg = 0;
                                }
                                else if(restecg_text.equals("ST-T wave is abnormal"))
                                {
                                    rest_ecg = 1;
                                }
                                else if(restecg_text.equals("Probable hypertrophy"))
                                {
                                    rest_ecg = 2;
                                }
                                String famhist_text = ((RadioButton)findViewById(((RadioGroup)findViewById(R.id.cad_fam_history)).
                                        getCheckedRadioButtonId())).getText().toString();
                                if(famhist_text.equals("Yes"))
                                {
                                    famhist = 1;
                                }
                                else if(famhist_text.equals("No"))
                                {
                                    famhist = 0;
                                }
                                String cptxt = ((RadioButton)findViewById(((RadioGroup)findViewById(R.id.cp_type)).
                                        getCheckedRadioButtonId())).getText().toString();
                                if(cptxt.equals("Typical Angina"))
                                {
                                    cp = 1;
                                }
                                else if(cptxt.equals("Atypical Angina"))
                                {
                                    cp = 2;
                                }
                                else if(cptxt.equals("Non Anginal pain"))
                                {
                                    cp = 3;
                                }
                                else if(cptxt.equals("Asymptotic"))
                                {
                                    cp = 4;
                                }
                                Random r = new Random();
                                pulse_rate_max = r.nextInt((135 - 80) + 1) + 80;
                                Random r2 = new Random();
                                pulse_rate_rest = r2.nextInt((90 - 65) + 1) + 63;
                                int[] pulse_values = new int[3];
                                pulse_values[0] = 3;
                                pulse_values[1] = 6;
                                pulse_values[2] = 7;
                                int pulse_index = r.nextInt(pulse_values.length);
                                pulse_rate = pulse_values[pulse_index];
                                String formatted_string = "User: " + user_name + "\n";
                                formatted_string+="Age: " + age + "\n";
                                formatted_string+="Gender: " + gender + "\n";
                                formatted_string+="Pulse Rate Category: " + pulse_rate + "\n";
                                formatted_string+="Pulse Rate Max: " + pulse_rate_max;
                                send_values_to_firebase();
                                //Log.d("My App Value", formatted_string);
                            }
                        });
                    } catch (Exception e) {
                        Log.d("My App Exception",e.toString());
                    }
                }
            }
        }).start();

    }

    /**
     * send_values_to_firebase() method is used to write the values entered by the user on Firebase
     */
    public void send_values_to_firebase ()
    {
        final HashMap<String, Object> userMap= new HashMap<String, Object>();
        final DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("User");
        mDatabase.child(user_name).child("Age").setValue(age);
        mDatabase.child(user_name).child("Gender").setValue(gender);
        mDatabase.child(user_name).child("Chest_Pain_Type").setValue(cp);
        mDatabase.child(user_name).child("Resting_blood_pressure").setValue(rest_bps);
        mDatabase.child(user_name).child("Serum_cholestrol").setValue(cholestrol);
        mDatabase.child(user_name).child("No_of_years_as_smoker").setValue(smoking_years);
        mDatabase.child(user_name).child("Cigs_per_day").setValue(cigs_per_day);
        mDatabase.child(user_name).child("Fasting_blood_sugar").setValue(fbs);
        mDatabase.child(user_name).child("Diabetes_family_history").setValue(dm);
        mDatabase.child(user_name).child("Coronary_Artery_Disease_Family_History").setValue(famhist);
        mDatabase.child(user_name).child("Resting_ECG_result").setValue(rest_ecg);
        mDatabase.child(user_name).child("Pulse_Rate").setValue(pulse_rate);
        mDatabase.child(user_name).child("Pulse_Rate_Max").setValue(pulse_rate_max);
        Log.d("Writing pulse rate",String.valueOf(pulse_rate_rest));
        mDatabase.child(user_name).child("Resting_pulse_rate").setValue(pulse_rate_rest);
        mDatabase.child(user_name).child("Peak_exercise_BP_High").setValue(tpeakbps);
        mDatabase.child(user_name).child("Peak_exercise_BP_Low").setValue(tpeakbpd);
        mDatabase.child(user_name).child("Resting_blood_pressure_low").setValue(trestbpd);
        mDatabase.child(user_name).child("Exercise_induced_angina").setValue(exang);
        mDatabase.child(user_name).child("Depression_induced_exercise").setValue(oldpeak);
        mDatabase.child(user_name).child("Slope_peak_exercise_ST").setValue(slope);
        mDatabase.child(user_name).child("No_of_major_vessels").setValue(ca);
        mDatabase.child(user_name).child("To_Check").setValue(DEFAULT_FLAG_BIT);
    }
}

