package com.example.myweatherapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.myweatherapp.AppHTTPClient;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {


    private ProgressBar mProgressBar;
    private TextView mErrorMessageDisplay;
    private RecyclerView mRecyclerView;
    private RecyclerViewAdapter mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mProgressBar = (ProgressBar) findViewById(R.id.pb_loading);
        mErrorMessageDisplay = ( TextView) findViewById(R.id.tv_error_message);
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_weather_display);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        mRecyclerView.setLayoutManager(layoutManager);
        //set all items in the list to have the same size:
        mRecyclerView.setHasFixedSize(true);

        //initialize the adapter
        mAdapter = new RecyclerViewAdapter();
        mRecyclerView.setAdapter(mAdapter);


        loadWeatherData();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.refresh){
            mAdapter.setWeatherData(null);
            loadWeatherData();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void loadWeatherData(){

        showWeatherDataView();

        String location = this.getString(R.string.setLocation);
        new FetchWeatherTask().execute(location);
    }
    private void showErrorMessage(){
        mRecyclerView.setVisibility(View.INVISIBLE);
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
    }
    private void showWeatherDataView(){
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }


    public class FetchWeatherTask extends AsyncTask<String, Void,String[]> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressBar.setVisibility(View.VISIBLE);

        }
        @Override
        protected String[] doInBackground(String... params) {

            //if parms is empty, return null since this means there is nothing to lookup
            if(params.length==0){
                return null;
            }

            String city = params[0];

            //build the URL by calling the appHTTPClient function buildURL
            URL apiUrl = AppHTTPClient.buildURLfromCityName(city);

            //now that we have the URL we can try to do the request, also using the function from appHTTPclient
            try {
                String weatherDataJSON = AppHTTPClient.getWeatherDataJSON(apiUrl);
                //we need to call another function to digest the information and tokenize it to a sting array from the JSON
               String[] parsedWeatherInfo = ParseJSON.tokenizeData(MainActivity.this,weatherDataJSON);


               return parsedWeatherInfo;

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }

        }



        @Override
        protected void onPostExecute(String[] weatherData) {
            mProgressBar.setVisibility(View.INVISIBLE);
            if(weatherData!=null){
                //make sure the right view is displayed
                showWeatherDataView();
                mAdapter.setWeatherData(weatherData);
            }
            else{
                showErrorMessage();
            }
        }
    }
}