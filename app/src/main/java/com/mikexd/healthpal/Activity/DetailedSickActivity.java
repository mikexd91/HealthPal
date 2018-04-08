package com.mikexd.healthpal.Activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mikexd.healthpal.R;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class DetailedSickActivity extends AppCompatActivity {

    private String sickID;
    private String description;
    private String medicalCondition;
    private String name;
    private String possibleSymptoms;
    private String TreatmentDescription;

    private TextView nameView;
    private TextView descriptionView;
    private TextView medicalConditionView;
    private TextView symptomsView;
    private TextView treatmentView;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_sick);
        Intent i = getIntent();
        String id = i.getStringExtra("id");
        String sick = i.getStringExtra("sick");
        sickID = id;
        new DetailedSickActivity.DownloadFilesTask().execute("as");
    }

    class DownloadFilesTask extends AsyncTask<String, Void, String> {
        protected String doInBackground(String... api) {
            String result ="";
            try {
                String first ="https://sandbox-healthservice.priaid.ch/issues/";
                String second="/info?token=eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJlbWFpbCI6Im1pa2UueHVuZGFAZ21haWwuY29tIiwicm9sZSI6IlVzZXIiLCJodHRwOi8vc2NoZW1hcy54bWxzb2FwLm9yZy93cy8yMDA1LzA1L2lkZW50aXR5L2NsYWltcy9zaWQiOiIzMTU2IiwiaHR0cDovL3NjaGVtYXMubWljcm9zb2Z0LmNvbS93cy8yMDA4LzA2L2lkZW50aXR5L2NsYWltcy92ZXJzaW9uIjoiMjAwIiwiaHR0cDovL2V4YW1wbGUub3JnL2NsYWltcy9saW1pdCI6Ijk5OTk5OTk5OSIsImh0dHA6Ly9leGFtcGxlLm9yZy9jbGFpbXMvbWVtYmVyc2hpcCI6IlByZW1pdW0iLCJodHRwOi8vZXhhbXBsZS5vcmcvY2xhaW1zL2xhbmd1YWdlIjoiZW4tZ2IiLCJodHRwOi8vc2NoZW1hcy5taWNyb3NvZnQuY29tL3dzLzIwMDgvMDYvaWRlbnRpdHkvY2xhaW1zL2V4cGlyYXRpb24iOiIyMDk5LTEyLTMxIiwiaHR0cDovL2V4YW1wbGUub3JnL2NsYWltcy9tZW1iZXJzaGlwc3RhcnQiOiIyMDE4LTA0LTA2IiwiaXNzIjoiaHR0cHM6Ly9zYW5kYm94LWF1dGhzZXJ2aWNlLnByaWFpZC5jaCIsImF1ZCI6Imh0dHBzOi8vaGVhbHRoc2VydmljZS5wcmlhaWQuY2giLCJleHAiOjE1MjMxMjUxMDIsIm5iZiI6MTUyMzExNzkwMn0.c-qi3pMV1U6HLumErWXo8GoxdHrd1JxWKazdbOQWMbA&language=en-gb&format=json";
                String resultz = first + sickID + second;
                URL url = new URL(resultz);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                try {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder stringBuilder = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuilder.append(line).append("\n");
                    }
                    bufferedReader.close();
                    result = stringBuilder.toString();
//                Log.d("result medicalll", "onComplete" + stringBuilder.toString());
                }
                finally{
                    urlConnection.disconnect();
                }
            }
            catch(Exception e) {
                Log.e("ERROR", e.getMessage(), e);

            }
            return result;
        }

        protected void onProgressUpdate(Integer... progress) {

        }

        protected void onPostExecute(String a) {
            try{
                JSONObject json = new JSONObject(a);
                description = json.getString("Description");
                medicalCondition = json.getString("MedicalCondition");
                name = json.getString("Name");
                possibleSymptoms = json.getString("PossibleSymptoms");
                TreatmentDescription = json.getString("TreatmentDescription");

                Log.d("Sickness", ": " + name);

                nameView = findViewById(R.id.name);
                descriptionView = findViewById(R.id.descriotion);
                medicalConditionView = findViewById(R.id.condition);
                symptomsView = findViewById(R.id.symptoms);
                treatmentView = findViewById(R.id.treatment);

                nameView.setText(name);
                descriptionView.setText(description);
                medicalConditionView.setText(medicalCondition);
                symptomsView.setText(possibleSymptoms);
                treatmentView.setText(TreatmentDescription);

                progressBar = findViewById(R.id.progress);
                progressBar.setVisibility(View.INVISIBLE);
            }catch(Exception e){

            }

        }
    }
}
