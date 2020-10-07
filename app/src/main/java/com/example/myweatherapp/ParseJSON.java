package com.example.myweatherapp;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.content.Context;
import android.util.Log;

import java.util.TimeZone;
import com.example.myweatherapp.DateFormatter;
public class ParseJSON {

    static String[] tokenizeData(Context context, String jsonData) throws JSONException {

        //keys according to API to info we need
        final String LIST = "list";
        final String CONDITION_CODE = "cod";
        final String TEMPERATURE = "temp";
        final String MAX_TEMP ="temp_max";
        final String MIN_TEMP = "temp_min";
        final String WEATHER ="weather";
        final String DESCRIPTION="main";
        final String COORDS = "coord";



        JSONObject jsonObj = new JSONObject (jsonData);





        //EXTRACTING INFO
        JSONArray weatherArray = jsonObj.getJSONArray(LIST);
        String[] dataPerDay = new String[weatherArray.length()];



        //
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
