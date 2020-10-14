package com.example.myweatherapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class MoreDetailsActivity extends AppCompatActivity {

    TextView mDetails;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_details);

        mDetails = (TextView) findViewById(R.id.tv_more_details);
        Intent intentThatTriggeredMe  = getIntent();
        if(intentThatTriggeredMe.hasExtra(Intent.EXTRA_TEXT)){
            String details = intentThatTriggeredMe.getStringExtra(Intent.EXTRA_TEXT);
            mDetails.setText(details);
        }
    }
}