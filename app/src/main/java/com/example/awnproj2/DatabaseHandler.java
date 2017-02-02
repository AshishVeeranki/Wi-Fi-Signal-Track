package com.example.awnproj2;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import static com.example.awnproj2.WifiDetails.WifiInfo.TABLE_NAME;


public class DatabaseHandler extends SQLiteOpenHelper {

    public static final int database_version = 1;
    public String CREATE_QUERY = "CREATE TABLE "+ TABLE_NAME+"("+WifiDetails.WifiInfo.SSID+" TEXT,"
            +WifiDetails.WifiInfo.RSSI+" INTEGER,"+WifiDetails.WifiInfo.TIMESTAMP+" INTEGER,"
            +WifiDetails.WifiInfo.DEVICEID+" TEXT,"+WifiDetails.WifiInfo.LATITUDE+" REAL,"
            +WifiDetails.WifiInfo.LONGITUDE+" REAL);";

    public DatabaseHandler(Context context){

        super(context, WifiDetails.WifiInfo.DATABASE_NAME, null, database_version);


    }

    @Override
    public void onCreate(SQLiteDatabase sdb){

        sdb.execSQL(CREATE_QUERY);


    }

    @Override
    public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2){

    }

    public void insertDetails(DatabaseHandler dop, String ssid, String rssi, String timestamp, String deviceid, double latitude, double longitude){

        SQLiteDatabase sq = dop.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(WifiDetails.WifiInfo.SSID, ssid);
        cv.put(WifiDetails.WifiInfo.RSSI, rssi);
        cv.put(WifiDetails.WifiInfo.TIMESTAMP, timestamp);
        cv.put(WifiDetails.WifiInfo.DEVICEID, deviceid);
        cv.put(WifiDetails.WifiInfo.LATITUDE, latitude);
        cv.put(WifiDetails.WifiInfo.LONGITUDE, longitude);
        long k = sq.insert(TABLE_NAME, null, cv);


    }

    public Cursor getDetails(DatabaseHandler dops){

        SQLiteDatabase sq = dops.getReadableDatabase();
        String[] columns = {WifiDetails.WifiInfo.SSID, WifiDetails.WifiInfo.RSSI, WifiDetails.WifiInfo.TIMESTAMP,
                WifiDetails.WifiInfo.DEVICEID,WifiDetails.WifiInfo.LATITUDE, WifiDetails.WifiInfo.LONGITUDE};
        Cursor cr = sq.query(WifiDetails.WifiInfo.TABLE_NAME,columns,null,null,null,null,null);
        return cr;

    }


}
