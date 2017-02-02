package com.example.awnproj2;

import android.provider.BaseColumns;

public class WifiDetails {

    public WifiDetails()
    {

    }

    public static abstract class WifiInfo implements BaseColumns
    {
        public static final String SSID = "ssid";
        public static final String RSSI = "rssi";
        public static final String TIMESTAMP = "timestamp";
        public static final String DEVICEID = "deviceid";
        public static final String LATITUDE = "latitude";
        public static final String LONGITUDE = "longitude";
        public static final String DATABASE_NAME = "wifidb_info";
        public static final String TABLE_NAME = "wifi_details";


    }
}
