package com.example.myweatherapp.utilities;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.net.Uri.Builder;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class AppHTTPClient {

    private static String BASE_URL ="https://api.openweathermap.org/data/2.5/forecast";
    private static String APPID = "8e01a7b1fd97e8095aaf54ab1dcac566";
    private static String QUERY_PARAM_CITY = "q";
    private static String API_KEY = "appid";
    private static String UNITS = "units";

    private static final String NUM_DAYS = "cnt";
    private static final int days=14;


  /**
   * Method: buildURLfromCityName
     Description: this method will take a city name string as input, and build a url
    to perform api requests
    **/
  public static URL buildURLfromCityName(String city, String units){

      //units
     ;
        //first build uri
      Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                .appendQueryParameter(QUERY_PARAM_CITY, city)
                .appendQueryParameter(UNITS, units)
                .appendQueryParameter(NUM_DAYS, Integer.toString(days))
                .appendQueryParameter(API_KEY, APPID)
                .build();

        URL url=null;
        String link = builtUri.toString();
        try{

            url = new URL(link);

        }
        catch (MalformedURLException e){

            e.printStackTrace();
        }

        return url;
    }

    /* Method: getWeatherDataJSON
        Description: takes in a build url and fetches data from the api, if url is null
        return null
     */
    public static String getWeatherDataJSON(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try{
            InputStream input = urlConnection.getInputStream();

            Scanner scanner = new Scanner(input);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
                String response = null;
            if(hasInput){
                response=scanner.next();

            }
            scanner.close();
            return  response;
        }

        finally {
            urlConnection.disconnect();
        }


    }
    public static URL getUrl(Context context){
        String city =PreferencesUtility.getLocationFromPreferenceOrDefault(context);
        boolean ismetric = PreferencesUtility.isMetric(context);
        String units = "metric";

        if (!ismetric) {
            units = "imperial";
        }
        //build the URL by calling the appHTTPClient function buildURL
        URL apiUrl =  buildURLfromCityName(city, units);
        return apiUrl;
    }
}
