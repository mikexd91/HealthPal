package com.mikexd.healthpal.Activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.mikexd.healthpal.Data.Sickness;
import com.mikexd.healthpal.R;
import com.mikexd.healthpal.Utilities.MasonryAdapter;
import com.mikexd.healthpal.Utilities.SpacesItemDecoration;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class ExplorerActivity extends AppCompatActivity {

    RecyclerView mRecyclerView;
    ProgressBar mProgressBar;
    ArrayList<Sickness> sickness;
    String api = R.string.baseURL+ "issues?"+ R.string.token +"&language=en-gb&format=json";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explorer);

        new DownloadFilesTask().execute(api);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));

    }

    class DownloadFilesTask extends AsyncTask<String, Void, String> {
        protected String doInBackground(String... api) {
            String result ="";
            try {
                String apis = getString(R.string.baseURL)+ "issues?token="+ getString(R.string.token) +"&language=en-gb&format=json";
//                URL url = new URL("https://sandbox-healthservice.priaid.ch/issues?token=eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJlbWFpbCI6Im1pa2UueHVuZGFAZ21haWwuY29tIiwicm9sZSI6IlVzZXIiLCJodHRwOi8vc2NoZW1hcy54bWxzb2FwLm9yZy93cy8yMDA1LzA1L2lkZW50aXR5L2NsYWltcy9zaWQiOiIzMTU2IiwiaHR0cDovL3NjaGVtYXMubWljcm9zb2Z0LmNvbS93cy8yMDA4LzA2L2lkZW50aXR5L2NsYWltcy92ZXJzaW9uIjoiMjAwIiwiaHR0cDovL2V4YW1wbGUub3JnL2NsYWltcy9saW1pdCI6Ijk5OTk5OTk5OSIsImh0dHA6Ly9leGFtcGxlLm9yZy9jbGFpbXMvbWVtYmVyc2hpcCI6IlByZW1pdW0iLCJodHRwOi8vZXhhbXBsZS5vcmcvY2xhaW1zL2xhbmd1YWdlIjoiZW4tZ2IiLCJodHRwOi8vc2NoZW1hcy5taWNyb3NvZnQuY29tL3dzLzIwMDgvMDYvaWRlbnRpdHkvY2xhaW1zL2V4cGlyYXRpb24iOiIyMDk5LTEyLTMxIiwiaHR0cDovL2V4YW1wbGUub3JnL2NsYWltcy9tZW1iZXJzaGlwc3RhcnQiOiIyMDE4LTA0LTA2IiwiaXNzIjoiaHR0cHM6Ly9zYW5kYm94LWF1dGhzZXJ2aWNlLnByaWFpZC5jaCIsImF1ZCI6Imh0dHBzOi8vaGVhbHRoc2VydmljZS5wcmlhaWQuY2giLCJleHAiOjE1MjMxMzI2NDcsIm5iZiI6MTUyMzEyNTQ0N30.FtBaIgdCJCixTZSK0vcW28nnry8w-QLrBe3dU-0aTCY&language=en-gb&format=json");
                URL url = new URL(apis);
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
                JSONArray json = new JSONArray(a);
                sickness = new ArrayList<>();
                for(int i=0;i<json.length();i++){
                    JSONObject e = json.getJSONObject(i);
                    sickness.add(new Sickness(e.getString("ID"),e.getString("Name")));
//                    Log.d("Sickness", ": " + e.getString("Name"));
                }

                MasonryAdapter adapter = new MasonryAdapter(getApplicationContext(), sickness);
                mRecyclerView.setAdapter(adapter);
                SpacesItemDecoration decoration = new SpacesItemDecoration(5);
                mRecyclerView.addItemDecoration(decoration);
                mProgressBar = findViewById(R.id.progress);
                mProgressBar.setVisibility(View.INVISIBLE);
//                Log.d("result medicalll", "onComplete" + a);
            }catch(Exception e){

            }
        }
    }
}


