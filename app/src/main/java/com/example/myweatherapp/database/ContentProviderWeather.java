package com.example.myweatherapp.database;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import com.example.myweatherapp.utilities.DateFormatter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class ContentProviderWeather extends ContentProvider {

    //handles two types of URIs
public static final int CODE_WEATHER=100;
public static final int CODE_WEATHER_N_DATE=101;

    private DbHelper mDbHelper;
    private UriMatcher  mMatcher= buildUriMatcher();
    @Override
    public boolean onCreate() {
        //initialize the dbhelper
       mDbHelper= new DbHelper(getContext());
       return true;
    }


    @Override
    public Cursor query(@NonNull Uri uri, String[] projection,  String selection, String[] selectionArgs,  String sortOrder) {
                Cursor cursor;
                switch(mMatcher.match(uri)){
                    case CODE_WEATHER_N_DATE:
                        String dateNormalized = uri.getLastPathSegment();
                        String [] selectionArgument = new String [1];
                        selectionArgument[0]=dateNormalized;
                        cursor= mDbHelper.getReadableDatabase().query(Contract.DataEntry.TABLE_NAME,projection,Contract.DataEntry.COLUMN_DATE +" = ? ",selectionArgument,null,null,sortOrder);
                        break;
                    case CODE_WEATHER:
                        cursor=mDbHelper.getReadableDatabase().query(Contract.DataEntry.TABLE_NAME,projection,selection, selectionArgs,null,null,sortOrder);
                        break;
                    default:
                        throw new UnsupportedOperationException("Unknown Uri: "+uri);
                }
                cursor.setNotificationUri(getContext().getContentResolver(),uri);
                return cursor;
    }

    @Nullable
    @Override
    //function not used
    public String getType(@NonNull Uri uri) {
       return null;
    }

    @Nullable
    @Override

    //function not used: replaced with bulkinsert
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        return null;
    }
    @Override
    public int bulkInsert(@NonNull Uri uri,@NonNull ContentValues[] values){
        final SQLiteDatabase db = mDbHelper.getWritableDatabase();
        int code = mMatcher.match(uri);

        switch(code){
            case CODE_WEATHER:
                db.beginTransaction();
                int rowsInserted=0;
                try{
                    for(ContentValues value: values){
                       long date=value.getAsLong(Contract.DataEntry.COLUMN_DATE);
                       if(!DateFormatter.isDateNormalize(date)){
                           throw new IllegalArgumentException("Date must be normalized to isnert properly");
                       }

                       long id = db.insert(Contract.DataEntry.TABLE_NAME,null, value);
                        if(id!=-1){
                            rowsInserted++;
                        }
                    }
                    db.setTransactionSuccessful();
                }finally
                {
                    db.endTransaction();
                }

                if(rowsInserted>0){
                    getContext().getContentResolver().notifyChange(uri,null);
                }
                return rowsInserted;
            default:
               return super.bulkInsert(uri, values);

        }
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        final SQLiteDatabase db= mDbHelper.getWritableDatabase();

        int code = mMatcher.match(uri);
        int rowsDeleted;
        if(selection==null){
            selection="1";// makes the delete function return the number of rows deleted even when everything was selected
        }
        switch(code){
            case CODE_WEATHER:
                rowsDeleted=db.delete(Contract.DataEntry.TABLE_NAME,selection,selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown Uri: "+uri);

        }
        //if you deleted some rows, notify of the change
        if(rowsDeleted!=0){
            getContext().getContentResolver().notifyChange(uri,null);
        }
        return rowsDeleted;




    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        final SQLiteDatabase db = mDbHelper.getWritableDatabase();
        int code = mMatcher.match(uri);

        int numRows;
        switch(code){
            case CODE_WEATHER:
                numRows=db.update(Contract.DataEntry.TABLE_NAME,values, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: "+uri);

        }
    if(numRows!=0){
        getContext().getContentResolver().notifyChange(uri,null);
    }
        return numRows;
    }

    public static UriMatcher buildUriMatcher(){

       final UriMatcher  uriMatcher= new UriMatcher(UriMatcher.NO_MATCH);
      final  String authority = Contract.CONTENT_AUTHORITY;
      final  String path = Contract.PATH_DB_NAME;

        uriMatcher.addURI(authority,path, CODE_WEATHER);
        uriMatcher.addURI(authority,path + "/#", CODE_WEATHER_N_DATE);

        return uriMatcher;
    }

}
