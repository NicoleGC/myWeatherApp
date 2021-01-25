package com.example.myweatherapp.utilities;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentValues;
import android.content.Context;
import android.util.Log;

import com.example.myweatherapp.utilities.DateFormatter;
import com.example.myweatherapp.database.Contract;

public class ParseJSON {


    //keys according to API to info we need
    final static String LIST = "list";
    final static String TEMPERATURE = "temp";
    final static String MAX_TEMP ="temp_max";
    final static String MIN_TEMP = "temp_min";
    final static String WEATHER ="weather";
    final static String DESCRIPTION="main";
    final static String WEATHER_DESCRIPTION = "description";
    final static String PRESSURE = "pressure";
    final static String HUMIDITY = "humidity";
    final static String WIND = "wind";
    final static String WIND_SPEED="speed";
    final static String WIND_DIR ="deg";
    final static String ID = "id";


   /* static String[] tokenizeData(Context context, String jsonData) throws JSONException {
        JSONObject jsonObj = new JSONObject (jsonData);

        //EXTRACTING INFO
        JSONArray weatherArray = jsonObj.getJSONArray(LIST);
        String[] dataPerDay = new String[weatherArray.length()];

        //get the start day normalized
        long localDate = System.currentTimeMillis();
        long utcDate = DateFormatter.getUTCDateFromLocal(localDate);
        long startDay = DateFormatter.normalizeDate(utcDate);


        for(int i = 0; i<weatherArray.length(); i++){
                //get Date
            JSONObject dateObj = weatherArray.getJSONObject(i);

          long dateTimeMillis = startDay + DateFormatter.DAY_IN_MILLIS*i;
          String date = DateFormatter.getFriendlyDateString(context,dateTimeMillis,false);

            //get weather: DESCRIPTION
            JSONObject weatherObj = dateObj.getJSONArray(WEATHER).getJSONObject(0);
                //From weather, get description
            String description = weatherObj.getString(DESCRIPTION);

            //get main: temp
            JSONObject mainTemp = dateObj.getJSONObject("main");
            double temp = mainTemp.getDouble(TEMPERATURE);

            double high = mainTemp.getDouble(MAX_TEMP);

            double low = mainTemp.getDouble(MIN_TEMP);



            dataPerDay[i]=date+ " - " +description + " - " + Math.round(high)+ " / " + Math.round(low);
    Log.v("DAY "+ Integer.toString(i), dataPerDay[i]);


        }







            return dataPerDay;



    }*/

    public static ContentValues[] tokenizeDataIntoContentValues (Context context, String jsonData) throws JSONException {
        JSONObject jsonObj = new JSONObject (jsonData);

        //EXTRACTING INFO
        JSONArray weatherArray = jsonObj.getJSONArray(LIST);

        ContentValues[] dataPerDay = new ContentValues[weatherArray.length()]; //this array will hold weather data where each slot represents a different day

        //get the start day normalized
        long localDate = System.currentTimeMillis();
        long utcDate = DateFormatter.getUTCDateFromLocal(localDate);
        long startDay = DateFormatter.normalizeDate(utcDate);

        for(int i = 0; i<weatherArray.length(); i++){
            //variables we will extract from JSON
            long dateTimeMillis;
            double pressure;
            int humidity;
            double windSpeed;
            double windDirection;
            double high;
            double low;
            double temperature;
            int weather_ID;
            String description;


            //get Date
            JSONObject dateObj = weatherArray.getJSONObject(i);
            dateTimeMillis = startDay + DateFormatter.DAY_IN_MILLIS*i;

            //get pressure, humidity,high, low, temperature
                    //get main: temp
            JSONObject mainTemp = dateObj.getJSONObject("main");
            pressure=mainTemp.getDouble(PRESSURE);
            high = mainTemp.getDouble(MAX_TEMP);
            low = mainTemp.getDouble(MIN_TEMP);
            humidity=mainTemp.getInt(HUMIDITY);
            temperature=mainTemp.getDouble(TEMPERATURE);

            //get windDireciton, and speed
            JSONObject windStats = dateObj.getJSONObject(WIND);
            windSpeed=windStats.getDouble(WIND_SPEED);
            windDirection=windStats.getDouble(WIND_DIR);

            //get weather id
            JSONArray descArray = dateObj.getJSONArray(WEATHER);
            JSONObject idAndDescription = descArray.getJSONObject(0);
            weather_ID=idAndDescription.getInt(ID);
            description=idAndDescription.getString(WEATHER_DESCRIPTION);

            ContentValues currValues = new ContentValues();
            currValues.put(Contract.DataEntry.COLUMN_DATE, dateTimeMillis);
            currValues.put(Contract.DataEntry.COLUMN_MIN_TEMP,low);
            currValues.put(Contract.DataEntry.COLUMN_MAX_TEMP,high);
            currValues.put(Contract.DataEntry.COLUMN_TEMPERATURE,temperature);
            currValues.put(Contract.DataEntry.COLUMN_HUMIDITY,humidity);
            currValues.put(Contract.DataEntry.COLUMN_PRESSURE,pressure);
            currValues.put(Contract.DataEntry.COLUMN_WIND_SPEED,windSpeed);
            currValues.put(Contract.DataEntry.COLUMN_WEATHER_ID,weather_ID);
            currValues.put(Contract.DataEntry.COLUMN_WIND_DIR,windDirection);
            currValues.put(Contract.DataEntry.COLUMN_WEATHER_DESC,description);

            dataPerDay[i]=currValues;
        }
        return dataPerDay;

    }





    //METHODS to help extract data from json
    private static JSONObject getObject(String tag, JSONObject jobj) throws JSONException {
        JSONObject tempObj = jobj.getJSONObject(tag);
        return tempObj;
    }
    private static String getString(String tag, JSONObject jobj) throws JSONException {
        return jobj.getString(tag);
    }
    private static float getFloat(String tag, JSONObject jobj) throws JSONException {
        return (float)jobj.getDouble(tag);
    }
    private static int getInt(String tag, JSONObject jobj) throws JSONException {
        return jobj.getInt(tag);
    }
}
