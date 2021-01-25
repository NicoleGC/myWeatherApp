package com.example.myweatherapp.sync;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.Nullable;

public class SyncIntentService extends IntentService {
    public SyncIntentService(){
        super("SyncIntentService");

    }
    @Override
    protected void onHandleIntent(Intent intent) {

        SyncTask.syncWeather(this);
    }
}
