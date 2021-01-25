package com.example.myweatherapp.utilities;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.example.myweatherapp.R;

import androidx.preference.PreferenceManager;

public class PreferencesUtility {


    public static void setLocation(Context context, String city){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(context.getString(R.string.editPrefs_key),city);
        editor.apply();
    }
    public static String getLocationFromPreferenceOrDefault(Context context){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String defaultLoc = context.getString(R.string.editPrefs_default);
        String key = context.getString(R.string.editPrefs_key);
        return sharedPreferences.getString(key,defaultLoc);
    }
    public static boolean isMetric(Context context){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String key = context.getString(R.string.listPrefs_key);
        String defaultVal = context.getString(R.string.listPrefs_default_metric_value);
        String valFromPrefs = sharedPreferences.getString(key,defaultVal);
        String metric = context.getString(R.string.listPrefs_default_metric_value);

        boolean metricPreferred=false;

        if(metric.equals(valFromPrefs)){
            metricPreferred=true;
        }
     return metricPreferred;
    }
    public static void resetLocation(Context context){
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPrefs.edit();

        editor.remove(context.getString(R.string.editPrefs_key));
        editor.apply();
    }
}
