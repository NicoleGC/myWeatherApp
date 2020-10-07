package com.example.myweatherapp;

import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import com.example.myweatherapp.AppHTTPClient;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    TextView mweatherDisplay;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mweatherDisplay = (TextView) findViewById(R.id.tv_weather_display);
        loadWeatherData();

    }
    private void loadWeatherData(){
        String location = "London";
        new FetchWeatherTask().execute(location);
    }


    public class FetchWeatherTask extends AsyncTask<String, Void,String[]> {

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
            if(weatherData!=null){
                for(String day: weatherData){
                    mweatherDisplay.append((day)+ "\n\n\n");
                }
            }
            else{
                mweatherDisplay.setText("bad");
            }
        }
    }
}