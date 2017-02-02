package com.example.awnproj2;

import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;

public class LocTrack extends Service implements LocationListener{

    private final Context c;

    boolean locenabled = false;
    boolean nwenabled = false;
    boolean getloc = false;

    Location location;

    double latitude;
    double longitude;

    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 5;
    private static final long MIN_TIME_BW_UPDATES = 500 * 60 * 1;

    protected LocationManager locmanager;

    public LocTrack(Context c) {
        this.c = c;
        getLocation();
    }



    public Location getLocation() {
        try {
            locmanager = (LocationManager) c.getSystemService(LOCATION_SERVICE);
            locenabled = locmanager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            nwenabled = locmanager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if(!locenabled && !nwenabled) {
            } else {                 this.getloc = true;

                if (nwenabled) {

                    locmanager.requestLocationUpdates(
                            LocationManager.NETWORK_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

                    if (locmanager != null) {
                        location = locmanager
                                .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

                        if (location != null) {

                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                        }
                    }

                }

                if(locenabled) {
                    if(location == null) {
                        locmanager.requestLocationUpdates(
                                LocationManager.GPS_PROVIDER,
                                MIN_TIME_BW_UPDATES,
                                MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

                        if(locmanager != null) {
                            location = locmanager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                            if(location != null) {
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();
                            }
                        }
                    }
                }
            }

        } catch (SecurityException e) {
            e.printStackTrace();
        }

        return location;
    }


    public void stopUsingGPS() {

        try {
            if (locmanager != null) {
                locmanager.removeUpdates(LocTrack.this);
            }
        }catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    public double getLatitude() {
        if(location != null) {
            latitude = location.getLatitude();
        }
        return latitude;
    }

    public double getLongitude() {
        if(location != null) {
            longitude = location.getLongitude();
        }

        return longitude;
    }

    public boolean getLoc() {
        return this.getloc;
    }

    public void navtoSettings() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(c);

        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                c.startActivity(intent);
            }
        });

        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        alertDialog.show();
    }

    @Override
    public void onLocationChanged(Location arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onProviderDisabled(String arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onProviderEnabled(String arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
        // TODO Auto-generated method stub

    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }
}
