package com.example.myweatherapp.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DbHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME="weather.db";
    private static final int DATABASE_VERSION=3;


    public DbHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
       final String SQL_CREATE_TABLE = "CREATE TABLE "+ Contract.DataEntry.TABLE_NAME + " (" +
                Contract.DataEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                Contract.DataEntry.COLUMN_DATE + " INTEGER NOT NULL, " +
                Contract.DataEntry.COLUMN_WEATHER_ID+" INTEGER NOT NULL, " +
                Contract.DataEntry.COLUMN_WEATHER_DESC + " TEXT NOT NULL," +
                Contract.DataEntry.COLUMN_TEMPERATURE + " REAL NOT NULL, " +
                Contract.DataEntry.COLUMN_MAX_TEMP + " REAL NOT NULL, "+
                Contract.DataEntry.COLUMN_MIN_TEMP +" REAL NOT NULL, "+
                Contract.DataEntry.COLUMN_WIND_DIR +" REAL NOT NULL, " +
                Contract.DataEntry.COLUMN_HUMIDITY+" REAL NOT NULL, " +
                Contract.DataEntry.COLUMN_PRESSURE+ " REAL NOT NULL, "+
                Contract.DataEntry.COLUMN_WIND_SPEED+" REAL NOT NULL," +

               " UNIQUE (" + Contract.DataEntry.COLUMN_DATE + ") ON CONFLICT REPLACE);";


       db.execSQL(SQL_CREATE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
                db.execSQL("DROP TABLE IF EXISTS "+ Contract.DataEntry.TABLE_NAME);
                onCreate(db);
    }
}
