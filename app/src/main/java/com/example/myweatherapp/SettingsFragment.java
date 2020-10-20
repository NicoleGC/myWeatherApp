package com.example.myweatherapp;

import android.content.SharedPreferences;
import android.os.Bundle;

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

            //set pref summary on each pref
        PreferenceScreen prefScreen = getPreferenceScreen();
        int count = prefScreen.getPreferenceCount();

        SharedPreferences sharedPrefs=getPreferenceScreen().getSharedPreferences();
        for(int i=0;i<count;i++){
            Preference currPref = prefScreen.getPreference(i);
            if(!(currPref instanceof CheckBoxPreference)){
                String val = sharedPrefs.getString(currPref.getKey(),"");
                setPreferenceSummary(currPref,val);
            }
        }
    }

    private void setPreferenceSummary(Preference pref, Object val){
        //it can either be an edit text pref or a list pref
        if(pref instanceof EditTextPreference){
                pref.setSummary(val.toString());
        }
        else if(pref instanceof ListPreference){
            ListPreference listPref = (ListPreference) pref;
            int indexPref= listPref.findIndexOfValue(val.toString());
            if(indexPref>=0){
                pref.setSummary(listPref.getEntries()[indexPref]);
            }
            else {
                pref.setSummary(val.toString());
            }

        }

    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Preference pref = findPreference(key);
        if(pref!=null){
            if(!(pref instanceof CheckBoxPreference)){
                String val = sharedPreferences.getString(pref.getKey(),"");
                setPreferenceSummary(pref,val);
            }
        }
    }

    //register and unregister the listener


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }
}
