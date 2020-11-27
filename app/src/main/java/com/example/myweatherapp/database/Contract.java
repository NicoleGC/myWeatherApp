package com.example.myweatherapp.database;

import android.net.Uri;
import android.provider.BaseColumns;

import com.example.myweatherapp.utilities.DateFormatter;

public class Contract {


    public static final String CONTENT_AUTHORITY="com.example.myweatherapp";

    public static final Uri BASE_URI = Uri.parse("content://"+CONTENT_AUTHORITY);

    public static final String PATH_DB_NAME ="weather";

    //table:weather contract DataEntry
    public static final class DataEntry implements BaseColumns{


        //This uri will be used to query from the Weather db (from content provider)
        public static final Uri OFFICIAL_URI=BASE_URI.buildUpon()
                .appendPath(PATH_DB_NAME)
                .build();


        public static final String TABLE_NAME= "weather";
        public static final String COLUMN_WEATHER_ID ="weather_code";
        public static final String COLUMN_DATE="date";
        public static final String COLUMN_MIN_TEMP="min_temp";
        public static final String COLUMN_MAX_TEMP="max_temp";
        public static final String COLUMN_TEMPERATURE ="temperature";
        public static final String COLUMN_HUMIDITY="humidity";
        public static final String COLUMN_PRESSURE="pressure";
        public static final String COLUMN_WIND_SPEED="wind_speed";
        public static final String COLUMN_WIND_DIR="wind_direction";
        public static final String COLUMN_WEATHER_DESC = "description";

        public static Uri buildUriWithDate(long date){
            return OFFICIAL_URI.buildUpon()
                    .appendPath(Long.toString(date))
                    .build();
        }
        public static String getSqlForTodayOn(){
            long normalizedUtc = DateFormatter.normalizeDate(System.currentTimeMillis());
            return DataEntry.COLUMN_DATE+" >= " + normalizedUtc;
        }

    }
}
