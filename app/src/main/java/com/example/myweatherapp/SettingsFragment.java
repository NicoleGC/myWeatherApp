package com.example.myweatherapp;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.example.myweatherapp.database.Contract;
import com.example.myweatherapp.sync.SyncUtils;
import com.example.myweatherapp.utilities.PreferencesUtility;

import androidx.preference.CheckBoxPreference;
import androidx.preference.EditTextPreference;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceScreen;

public class SettingsFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            addPreferencesFromResource(R.xml.preferences);

        SharedPreferences sharedPrefs=getPreferenceScreen().getSharedPreferences();
            //set pref summary on each pref
        PreferenceScreen prefScreen = getPreferenceScreen();
        int count = prefScreen.getPreferenceCount();
        for(int i=0;i<count;i++){
            Preference currPref = prefScreen.getPreference(i);
            if(!(currPref instanceof CheckBoxPreference)){
                String val = sharedPrefs.getString(currPref.getKey(),"");
                setPreferenceSummary(currPref,val);
            }
        }
    }
    private void setPreferenceSummary(Preference pref, Object val){
            String stringVal = val.toString();

        //it can either be an edit text pref or a list pref
        if(pref instanceof ListPreference) {
            ListPreference listPref = (ListPreference) pref;
            int indexPref = listPref.findIndexOfValue(stringVal);
            if (indexPref >= 0) {
                pref.setSummary(listPref.getEntries()[indexPref]);
            }
        }
        else {
            pref.setSummary(stringVal);
        }

    }
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
      Activity activity = getActivity();

       if(key.equals(getString(R.string.editPrefs_key))){
           Log.v("CHANGEd","location changed");
          // PreferencesUtility.resetLocation(activity);
           SyncUtils.startImmediateSync(activity);
       }
       else if(key.equals(getString(R.string.listPrefs_key))){
           activity.getContentResolver().notifyChange(Contract.DataEntry.OFFICIAL_URI,null);
           SyncUtils.startImmediateSync(activity);
       }
        Preference pref = findPreference(key);
        if(null!=pref){
            if(!(pref instanceof CheckBoxPreference)){
                setPreferenceSummary(pref,sharedPreferences.getString(key,""));
            }
        }
    }
    @Override
    public void onStart() {
        super.onStart();
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }
    @Override
    public void onStop() {
        super.onStop();
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }
}
