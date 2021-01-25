package com.example.myweatherapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.example.myweatherapp.database.Contract;
import com.example.myweatherapp.sync.SyncUtils;
import com.example.myweatherapp.utilities.PreferencesUtility;

public class MainActivity extends AppCompatActivity implements RecyclerViewAdapter.AdapterOnClickHandler, LoaderManager.LoaderCallbacks<Cursor> {


    private static final String TAG = MainActivity.class.getSimpleName();

    /*column names to display*/
    public static final String[] COLUMNS_TO_EXTRACT = {
            Contract.DataEntry.COLUMN_DATE,
            Contract.DataEntry.COLUMN_MAX_TEMP,
            Contract.DataEntry.COLUMN_MIN_TEMP,
            Contract.DataEntry.COLUMN_WEATHER_ID,
           Contract.DataEntry.COLUMN_WEATHER_DESC
    };
    //indices to match positions in array above:
    public static final int INDEX_DATE =0;
    public static final int INDEX_MAX_TEMP = 1;
    public static final int INDEX_MIN_TEMP =2;
    public static final int INDEX_WEATHER_ID =3;
   public static final int INDEX_DESCRIPTION=4;

    private ProgressBar mProgressBar;
    private RecyclerView mRecyclerView;
    private RecyclerViewAdapter mAdapter;
    private static final int LOADER_ID =22;
    private int mPosition = RecyclerView.NO_POSITION;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setElevation(0f);

        //initialize views
        mProgressBar = (ProgressBar) findViewById(R.id.pb_loading);
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_weather_display);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        mRecyclerView.setLayoutManager(layoutManager);
        //set all items in the list to have the same size:
        mRecyclerView.setHasFixedSize(true);

        //initialize the adapter
        mAdapter = new RecyclerViewAdapter(this,this);
        mRecyclerView.setAdapter(mAdapter);


        showLoading();


        //loader init
        //if a loader already exists, it will re-use it and not re create it
        getSupportLoaderManager().initLoader(LOADER_ID,null,this);
        //populate our db

//updateWeather();

      SyncUtils.initialize(this);

    }


    /****MENU FUNCTIONS***/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

       if(id==R.id.mapOpen){
            openMapWithIntent();
            return true;
        }
        else if(id==R.id.settings){
           openSettingsWithIntent();
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

  /* public void updateWeather(){
        //populate our db
        FetchWeatherTask weatherTask = new FetchWeatherTask(this);
        weatherTask.execute(PreferencesUtility.getLocationFromPreferenceOrDefault(this));

    }*/
    public void openSettingsWithIntent(){
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }
    public void openMapWithIntent(){
        //build uri
        String userlocation = PreferencesUtility.getLocationFromPreferenceOrDefault(this);

        Uri geoLocation = Uri.parse("geo:0,0?q=" + userlocation);

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(geoLocation);

        //check for apps
        if(intent.resolveActivity(getPackageManager())!=null){
            startActivity(intent);
        }
        else{
            Log.d(TAG, "No apps to service this request");
        }
    }


   /************************************************************************/

    private void showWeatherDataView(){
        mProgressBar.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }
private void showLoading(){
        mRecyclerView.setVisibility(View.INVISIBLE);
        mProgressBar.setVisibility(View.VISIBLE);


}


    @Override
    public void onClick(long date) {
        Context context=this;
        Uri uriData = Contract.DataEntry.buildUriWithDate(date);
        Intent intent = new Intent(context,MoreDetailsActivity.class);
        intent.setData(uriData);
        startActivity(intent);

    }


    /***LOADER FUNCTIONS **/
    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        //check that we are responding to the right loader ( in our case we know there's only one)
        switch(id){
            case LOADER_ID:

                Uri uriToTable = Contract.DataEntry.OFFICIAL_URI;

                //the projection argument is the names of the columns we are interested in, we have grouped them in a string array in main
                String [] projections = COLUMNS_TO_EXTRACT;

                //for selection, we only care about rows whose date >= today
                String selection = Contract.DataEntry.getSqlForTodayOn();

                //we want the information sorted in asc order of date
                String sortedOrder = Contract.DataEntry.COLUMN_DATE+" ASC";
                return new CursorLoader(this,uriToTable,COLUMNS_TO_EXTRACT,selection,null,sortedOrder);

            default:
                throw new RuntimeException("Loader not implemented: "+ id);
        }


    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        //swaps the current cursor with our new loaded one.
        mAdapter.swapCursor(data);
        if(mPosition==RecyclerView.NO_POSITION){
            mPosition=0;
        }
        mRecyclerView.smoothScrollToPosition(mPosition);
       // Log.v("getcount",String.valueOf(data.getCount()));
        if(data.getCount()!=0){
            showWeatherDataView();
        }

    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        //release resources.
        mAdapter.swapCursor(null);

    }



}