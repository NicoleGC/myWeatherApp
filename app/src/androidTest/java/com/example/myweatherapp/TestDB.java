package com.example.myweatherapp;

import android.app.Instrumentation;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.ContentValues;

import com.example.myweatherapp.database.DbHelper;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class TestDB  extends TestCase {




private SQLiteDatabase db;
private SQLiteOpenHelper dbHelper;
private final Context context = ApplicationProvider.getApplicationContext();
private static final String dbName = "weather";


//DB COLUMNS
/* October 1st, 2016 at midnight, GMT time */
static final long DATE_NORMALIZED = 1475280000000L;
    public static final String COLUMN_DATE="date";
    public static final String COLUMN_MIN_TEMP="min_temp";
    public static final String COLUMN_MAX_TEMP="max_temp";
    public static final String COLUMN_TEMPERATURE ="temperature";
    public static final String COLUMN_HUMIDITY="humidity";
    public static final String COLUMN_PRESSURE="pressure";
    public static final String COLUMN_WIND_SPEED="wind_speed";
    public static final String COLUMN_WEATHER_ID ="weather_code";
    public static final String COLUMN_WIND_DIR="wind_direction";
    public static final String COLUMN_WEATHER_DESC = "description";



@Before
public void before() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {

  dbHelper= (SQLiteOpenHelper) new DbHelper(context);
  db = (SQLiteDatabase) SQLiteOpenHelper.class.getDeclaredMethod("getWritableDatabase").invoke(dbHelper);

}






//test db creation first
    @Test
        public void testTableCreation() {

        //make a set to store the names of all the tables you plan to create (in our case just one)
        final HashSet<String> tableNames = new HashSet<>();
        tableNames.add(dbName);

        //check if the db is open
        assertEquals("Database didn't open", true, db.isOpen());



        Cursor tableCursor = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'",null);

        //check if the db exists
        assertTrue("Database was not created correctly", tableCursor.moveToFirst());

        //check if the name matches what we put in the hasmap
        do{
            tableNames.remove(tableCursor.getString(0));
        }while (tableCursor.moveToNext());

        assertTrue("Error: table are not the tables we expected", tableNames.isEmpty());
        tableCursor.close();

        }

    //reading->writing->validating
    //1. get reference to writable database: happens on the before test
    //2. create contentValues of what we will insert
    //3. insert contentValues and get a row ID back
    //4. query the db for that ID and see if it matches, we will get a cursor back
    //5.close cursor and database when you're done
@Test
    public void testSingleRecordInsertion(){

    //contentValues
    ContentValues weatherVals = new ContentValues();
    weatherVals.put(COLUMN_DATE,DATE_NORMALIZED);
    weatherVals.put(COLUMN_MIN_TEMP,65);
    weatherVals.put(COLUMN_WEATHER_ID,100);
    weatherVals.put(COLUMN_MAX_TEMP,75);
    weatherVals.put(COLUMN_TEMPERATURE,63);
    weatherVals.put(COLUMN_HUMIDITY,1.2);
    weatherVals.put(COLUMN_PRESSURE,1.3);
    weatherVals.put(COLUMN_WIND_SPEED,5.5);
    weatherVals.put(COLUMN_WIND_DIR,4.5);
    weatherVals.put(COLUMN_WEATHER_DESC,"Cloudy");

    long rowId = db.insert(dbName, null, weatherVals);

    assertTrue("Insertion failed",rowId!=-1);

    Cursor weatherCursor = db.query(dbName, null, null,null, null, null, null);

    assertTrue("Error: Table returned nothing", weatherCursor.moveToFirst());


    //finally verify that the information in the table matches what we just inserted
    Set<Map.Entry<String,Object>> valueSet = weatherVals.valueSet();

    for(Map.Entry<String,Object> entry: valueSet){
        String colName = entry.getKey();
        int index = weatherCursor.getColumnIndex(colName);
        //test if column name exists
        assertFalse("Column "+ colName+" not found", index==-1);

        //test if values are the same
        String expectedVal = entry.getValue().toString();
        String actual = weatherCursor.getString(index);

        assertEquals("Error: Values don't match", expectedVal,actual);


    }
    //check that this is our only table
    assertFalse("Error: More than one record returned", weatherCursor.moveToNext());
    weatherCursor.close();

}

}
