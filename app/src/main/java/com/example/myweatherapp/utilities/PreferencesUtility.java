package com.example.myweatherapp.utilities;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.myweatherapp.R;

import androidx.preference.PreferenceManager;

public class PreferencesUtility {

    public static String getLocationFromPreferenceOrDefault(Context context){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String key = context.getString(R.string.editPrefs_key);
        String defaultLoc = context.getString(R.string.editPrefs_default);
        return sharedPreferences.getString(key,defaultLoc);
    }
    public static boolean isMetric(Context context){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String key = context.getString(R.string.listPrefs_key);
        String defaultVal = context.getString(R.string.listPrefs_default_metric_value);

        String valFromPrefs = sharedPreferences.getString(key,defaultVal);
        if(valFromPrefs.equals(defaultVal)){
            return true;
        }
        else{
            return false;
        }
    }
}
