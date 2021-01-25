package com.example.myweatherapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.myweatherapp.database.Contract;
import com.example.myweatherapp.utilities.DateFormatter;

public class MoreDetailsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {


    TextView mDate;
    TextView mDescription;
    TextView mHighTemperature;
    TextView mLowTemperature;
    TextView mHumidity;
    TextView mPressure;
    TextView mWindSpeed;
    TextView mWindDirection;

    private static final int LOADER_ID =23;

    Uri mUri;

    /*projections*/
    /*column names to display*/
    public static final String[] COLUMNS_TO_EXTRACT = {
            Contract.DataEntry.COLUMN_DATE,
            Contract.DataEntry.COLUMN_WEATHER_DESC,
            Contract.DataEntry.COLUMN_MAX_TEMP,
            Contract.DataEntry.COLUMN_MIN_TEMP,
            Contract.DataEntry.COLUMN_HUMIDITY,
            Contract.DataEntry.COLUMN_PRESSURE,
            Contract.DataEntry.COLUMN_WIND_SPEED,
            Contract.DataEntry.COLUMN_WIND_DIR

    };
    //indices to match positions in array above:
    public static final int INDEX_DATE =0;
    public static final int INDEX_DESCRIPTION=1;
    public static final int INDEX_MAX_TEMP = 2;
    public static final int INDEX_MIN_TEMP =3;
    public static final int INDEX_HUMIDITY =4;
    public static final int INDEX_PRESSURE = 5;
    public static final int INDEX_WIND_SPEED= 6;
    public static final int INDEX_WIND_DIR = 7;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_details);

        //this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mDate = (TextView) findViewById(R.id.tv_date);
        mDescription = (TextView) findViewById(R.id.tv_description);
        mHighTemperature = (TextView) findViewById(R.id.tv_high_temperature);
        mLowTemperature = (TextView) findViewById(R.id.tv_low_temperature);
        mHumidity = (TextView) findViewById(R.id.tv_humidity);
        mPressure=( TextView) findViewById(R.id.tv_pressure);
        mWindSpeed = (TextView) findViewById(R.id.tv_wind_speed);
        mWindDirection = (TextView) findViewById(R.id.tv_wind_dir);

        mUri = getIntent().getData();
        if(mUri==null){
            throw new NullPointerException("Uri for detail activity is null");
        }
        //loader init

        LoaderManager loaderManager = getSupportLoaderManager();
        //if a loader already exists, it will re-use it and not re create it
        loaderManager.initLoader(LOADER_ID,null,this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.more_details_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id==R.id.settings){
           openSettingsWithIntent();
            return true;
        }
        if(id==android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
    public void openSettingsWithIntent(){
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        switch (id){
            case LOADER_ID:
                return new CursorLoader(this, mUri, COLUMNS_TO_EXTRACT,null,null,null);

            default:
                throw new RuntimeException("Loader not implemented: "+ id);
        }
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        boolean hasValidData=false;
        if(data!=null && data.moveToFirst()){
            hasValidData=true;
        }

        if(!hasValidData){
            return;
        }

        //read date from cursor
        long gmtDate = data.getLong(INDEX_DATE);
        String date= DateFormatter.getFriendlyDateString(this, gmtDate, true);
        mDate.setText(date);

        //get weather description
        String description = data.getString(INDEX_DESCRIPTION);
        mDescription.setText(description);

        //get  MAX temperature
        double maxTemp = data.getDouble(INDEX_MAX_TEMP);
        mHighTemperature.setText(""+maxTemp);

        //GET MIN TEMP
        double minTemp = data.getDouble(INDEX_MIN_TEMP);
        mLowTemperature.setText(""+minTemp);

        //get humidity
        double humidity = data.getDouble(INDEX_HUMIDITY);
        mHumidity.setText(""+humidity);

        //get wind speed and dir
        double windSpeed = data.getDouble(INDEX_WIND_SPEED);
        mWindSpeed.setText(""+windSpeed);
        double windDir = data.getDouble(INDEX_WIND_DIR);
        mWindDirection.setText(""+windDir);

        //get pressure
        double pressure= data.getDouble(INDEX_PRESSURE);
        mPressure.setText(""+pressure);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {

    }
}