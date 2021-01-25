package com.example.myweatherapp.sync;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;

import com.example.myweatherapp.database.Contract;
import com.example.myweatherapp.utilities.AppHTTPClient;
import com.example.myweatherapp.utilities.ParseJSON;

import java.net.URL;

//this class contains all the tasks that will run in the background
public class SyncTask {


    //this function replaces the previous asyncTask in FetchWeatherTask.java.
    //it parses JSON, and gets new weather info from the network and passes it to the
    //content provider.
    synchronized public static void syncWeather(Context context){

        URL url = AppHTTPClient.getUrl(context);
        try {
            String weatherDataJSON = AppHTTPClient.getWeatherDataJSON(url);
            //we need to call another function to digest the information and tokenize it to a sting array from the JSON
            ContentValues[] parsedWeatherInfo = ParseJSON.tokenizeDataIntoContentValues(context, weatherDataJSON);

            if (parsedWeatherInfo!=null && parsedWeatherInfo.length > 0) {
               ContentResolver contentResolver= context.getContentResolver();
               //delete the old data
                contentResolver.delete(Contract.DataEntry.OFFICIAL_URI,null,null);
                //insert new data;
               contentResolver.bulkInsert(Contract.DataEntry.OFFICIAL_URI, parsedWeatherInfo);
            }

        } catch (Exception e) {
            e.printStackTrace();

        }

    }
}

