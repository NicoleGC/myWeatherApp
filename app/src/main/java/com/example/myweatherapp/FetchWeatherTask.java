package com.example.myweatherapp;

import android.content.ContentValues;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.myweatherapp.database.Contract;
import com.example.myweatherapp.utilities.AppHTTPClient;
import com.example.myweatherapp.utilities.ParseJSON;
import com.example.myweatherapp.utilities.PreferencesUtility;

import java.net.URL;

public class FetchWeatherTask extends AsyncTask<String, Void, Void> {
    Context mContext;

    public FetchWeatherTask(Context context) {
        mContext = context;

    }

    @Override
    protected Void doInBackground(String... strings) {

        //call parse JSON
        String city = strings[0];
        boolean ismetric = PreferencesUtility.isMetric(mContext);
        String units = "metric";

        if (!ismetric) {
            units = "imperial";
        }
        //build the URL by calling the appHTTPClient function buildURL
        URL apiUrl = AppHTTPClient.buildURLfromCityName(city, units);

        //now that we have the URL we can try to do the request, also using the function from appHTTPclient
        try {
            String weatherDataJSON = AppHTTPClient.getWeatherDataJSON(apiUrl);

            //we need to call another function to digest the information and tokenize it to a sting array from the JSON
            ContentValues[] parsedWeatherInfo = ParseJSON.tokenizeDataIntoContentValues(mContext, weatherDataJSON);

            if (parsedWeatherInfo.length > 0) {

                mContext.getContentResolver().bulkInsert(Contract.DataEntry.OFFICIAL_URI, parsedWeatherInfo);
            }

        } catch (Exception e) {
            e.printStackTrace();


        }
        return null;
    }
}