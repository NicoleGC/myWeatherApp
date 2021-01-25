package com.example.myweatherapp.sync;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import com.example.myweatherapp.database.ContentProviderWeather;
import com.example.myweatherapp.database.Contract;

import androidx.annotation.NonNull;

public class SyncUtils {
    private static boolean sInitialized;

   synchronized public static void initialize(@NonNull final Context context){
        if(sInitialized){
            return;
        }

        sInitialized=true;
        //check if content provider is empty, if it is sync weather.
       new AsyncTask<Void, Void, Void>(){

           @Override
           protected Void doInBackground(Void... voids) {


               Uri uriToTable = Contract.DataEntry.OFFICIAL_URI;
               String [] projections ={Contract.DataEntry._ID};
               String selection = Contract.DataEntry.getSqlForTodayOn();
               Cursor cursor = context.getContentResolver().query(uriToTable,
                       projections,
                       selection,
                       null,
                       null);
               if(null==cursor || cursor.getCount()==0){
                   startImmediateSync(context);
               }
               cursor.close();
            return null;
           }
       }.execute();

    }
    public static void startImmediateSync(@NonNull final Context context){
        Intent syncIntent = new Intent(context, SyncIntentService.class);
        context.startService(syncIntent);
    }
}
