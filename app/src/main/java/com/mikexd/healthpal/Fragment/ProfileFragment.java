package com.mikexd.healthpal.Fragment;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mikexd.healthpal.Activity.LoginActivity;
import com.mikexd.healthpal.R;

import java.util.Calendar;
import java.util.Date;

import static android.content.ContentValues.TAG;
import static android.content.Context.MODE_PRIVATE;
import static com.mikexd.healthpal.Activity.SignUpActivity.MY_SHAREDPREF_NAME1;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, SensorEventListener {

    private Button pressme;
    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private FirebaseUser user;
    private GoogleApiClient mGoogleApiClient;
    private SensorManager sensorManager;
    private boolean running = false;
    private TextView nameText;
    private TextView emailText;
    private TextView dayStep;
    private TextView weekStep;
    private TextView monthStep;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();

        //step counter
        sensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);

        // Write a message to the database
        database = FirebaseDatabase.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();

        writeToFirebase("day", 1, "dayStep");
        writeToFirebase("week", 7, "weekStep");
        writeToFirebase("month", 30, "monthStep");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_profile, container, false);
        Button signOut = v.findViewById(R.id.buttonSignOut);
        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                LoginManager.getInstance().logOut();
                Intent i = new Intent(getActivity(), LoginActivity.class);
                startActivity(i);
                getActivity().overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
            }
        });
        dayStep = v.findViewById(R.id.dayStep);
        weekStep = v.findViewById(R.id.weekStep);
        monthStep = v.findViewById(R.id.monthStep);
        nameText = v.findViewById(R.id.nameText);
        emailText = v.findViewById(R.id.emailText);

        SharedPreferences editors = getActivity().getSharedPreferences(MY_SHAREDPREF_NAME1,MODE_PRIVATE);
        int daySteps = Integer.valueOf(editors.getString("dayStep","0"));
        int weekSteps = Integer.valueOf(editors.getString("weekStep","0"));
        int monthSteps = Integer.valueOf(editors.getString("monthStep","0"));
        dayStep.setText(Integer.toString(daySteps));
        weekStep.setText(Integer.toString(weekSteps));
        monthStep.setText(Integer.toString(monthSteps));

        getUserInfo();
        return v;
    }

    public void getUserInfo(){
        if (user != null) {
            final DatabaseReference myRef = database.getReference("users").child(user.getUid());

            // Read from the database
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // This method is called once with the initial value and again
                    // whenever data at this location is updated.
                   if(dataSnapshot.exists()){
                       nameText.setText(String.valueOf(dataSnapshot.child("name").getValue()));
                       emailText.setText(String.valueOf(dataSnapshot.child("userEmail").getValue()));
                   }
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    // Failed to read value
                    Log.w(TAG, "Failed to read value.", error.toException());
                }
            });
        }
    }

    public void writeToFirebase(String date, final int dateNumeric, final String steps){
        if (user != null) {
            final DatabaseReference myRef = database.getReference("users").child(user.getUid()).child(date);

            // Read from the database
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // This method is called once with the initial value and again
                    // whenever data at this location is updated.
                    if(dataSnapshot.exists()){
//                        Toast.makeText(getContext(),dataSnapshot.getValue().toString(), Toast.LENGTH_SHORT).show();
                        int day = calculateDay(dataSnapshot.child("duration").getValue(Long.class));
//                        Toast.makeText(getContext(),String.valueOf(day), Toast.LENGTH_SHORT).show();
                        if(day >= dateNumeric){
                            long timeNow = setTimeNow();
                            myRef.child("duration").setValue(timeNow);

                            SharedPreferences editor = getActivity().getSharedPreferences(MY_SHAREDPREF_NAME1,MODE_PRIVATE);
                            String step = editor.getString(steps,"0");
                            myRef.child("steps").setValue(step);

                            SharedPreferences.Editor editors = getActivity().getSharedPreferences(MY_SHAREDPREF_NAME1,MODE_PRIVATE).edit();
                            editors.putString(steps,"0");
                        }
                    }else{
                        long timeNow = setTimeNow();
                        myRef.child("duration").setValue(timeNow);

                        SharedPreferences editor = getActivity().getSharedPreferences(MY_SHAREDPREF_NAME1,MODE_PRIVATE);
                        String step = editor.getString(steps,"0");
                        myRef.child("steps").setValue(step);

                        SharedPreferences.Editor editors = getActivity().getSharedPreferences(MY_SHAREDPREF_NAME1,MODE_PRIVATE).edit();
                        editors.putString(steps,"0");
                    }
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    // Failed to read value
                    Log.w(TAG, "Failed to read value.", error.toException());
                }
            });
        }
    }


    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser == null){
            updateUI();
        }
    }

    public void updateUI() {
        Intent i = new Intent(getActivity(), LoginActivity.class);
        startActivity(i);
        getActivity().overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
    }

    @Override
    public void onResume() {
        super.onResume();
        running = true;
        Sensor countSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        if(countSensor != null){
            sensorManager.registerListener(this,countSensor, SensorManager.SENSOR_DELAY_UI);
        }else {
            Toast.makeText(getContext(),"Sensor not found", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        running = false;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if(running){
            int stepToAdd = Integer.valueOf((int)event.values[0]);
            // get shared pref steps
            SharedPreferences editors = getActivity().getSharedPreferences(MY_SHAREDPREF_NAME1,MODE_PRIVATE);
            int daySteps = Integer.valueOf(editors.getString("dayStep","0"));
            int weekSteps = Integer.valueOf(editors.getString("weekStep","0"));
            int monthSteps = Integer.valueOf(editors.getString("monthStep","0"));
            // add it in
            daySteps+= stepToAdd;
            weekSteps+= stepToAdd;
            monthSteps+= stepToAdd;
            // update shared pref
            SharedPreferences.Editor editor = getActivity().getSharedPreferences(MY_SHAREDPREF_NAME1,MODE_PRIVATE).edit();
            editor.putString("dayStep", String.valueOf(daySteps));
            editor.putString("weekStep", String.valueOf(weekSteps));
            editor.putString("monthStep", String.valueOf(monthSteps));
            editor.commit();

            dayStep.setText(Integer.toString(daySteps));
            weekStep.setText(Integer.toString(weekSteps));
            monthStep.setText(Integer.toString(monthSteps));
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
    public long setTimeNow(){
        Calendar cal = Calendar.getInstance();
        Date now = new Date();
        cal.setTime(now);
        return cal.getTimeInMillis();
    }

    public int calculateDay(long milliseconds){
        return (int) ((setTimeNow() - milliseconds)  / (1000*60*60*24));
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}


