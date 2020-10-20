package com.example.myweatherapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class MoreDetailsActivity extends AppCompatActivity {

    TextView mDetails;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_details);
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mDetails = (TextView) findViewById(R.id.tv_more_details);
        Intent intentThatTriggeredMe  = getIntent();
        if(intentThatTriggeredMe.hasExtra(Intent.EXTRA_TEXT)){
            String details = intentThatTriggeredMe.getStringExtra(Intent.EXTRA_TEXT);
            mDetails.setText(details);
        }
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
}