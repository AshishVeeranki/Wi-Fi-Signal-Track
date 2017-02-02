package com.example.awnproj2;


import android.app.AlertDialog;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    Context ctx2 = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;

        mMap.getUiSettings().setCompassEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);

        ConnectivityManager connectivitymanager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivitymanager.getActiveNetworkInfo();


        if(networkInfo != null && networkInfo.isConnected())
        {
            try {

                String link = "http://project2website.000webhostapp.com/select_data.php?user=1&ssid=wsu-secure";


                URL url = new URL(link);

                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);

                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
                String rsp = "";
                String line = "";
                while((line = br.readLine()) != null)
                {
                    rsp+= line;
                }

                br.close();
                inputStream.close();
                httpURLConnection.disconnect();

                String s1[] = rsp.split("\\[");
                String s2[] = s1[1].split("\\]");
                String s3[] = s2[0].split(",");
                ArrayList l1 = new ArrayList();
                ArrayList l2 = new ArrayList();

                String k = "";
                String v = "";
                int temp = 1;



                for(int i=0; i<s3.length; i++)
                {
                    String s4[] = s3[i].split(":");
                    String tempStr = s4[1].replaceAll("\"","").trim();

                    if( s4[0].contains("rssi"))
                    {
                        k =  tempStr;
                    }

                    if( s4[0].contains("latitude") || s4[0].contains("longitude") )
                    {
                        if(temp == 1)
                        {
                            v +=  tempStr + ",";
                        }
                        else
                        {
                            v +=  tempStr;
                        }
                        temp++;
                    }

                    if(temp==3)
                    {
                        l1.add(k);
                        l2.add(v.replaceAll("\\}", "" ));
                        k="";
                        v="";
                        temp=1;
                    }

                }

                for(int i=0; i<l1.size(); i++)
                {
                    System.out.println(l1.get(i) +"=" + l2.get(i) );

                    String ssid = "wsu-secure";
                    int rssi = Integer.parseInt(l1.get(i).toString());

                    String Str = l2.get(i).toString();
                    String[] parts = Str.split(",");

                    Double lat = Double.parseDouble(parts[0]);
                    Double lon = Double.parseDouble(parts[1]);

                    LatLng latlng = new LatLng(lat,lon);

                    // Drawing marker on the map
                    drawMarker(latlng, ssid, rssi);

                }


            }catch (IOException e){
                e.printStackTrace();
            }



        }

        else {
            DatabaseHandler dopsm = new DatabaseHandler(ctx2);
            Cursor cr = dopsm.getDetails(dopsm);

            int rowCount = cr.getCount();

            if(rowCount != 0) {
                cr.moveToFirst();
                do {
                    String ssid = cr.getString(0);
                    int rssi = Integer.parseInt(cr.getString(1));

                    LatLng point = new LatLng(Double.parseDouble(cr.getString(4)), Double.parseDouble(cr.getString(5)));

                    // Drawing marker on the map
                    drawMarker(point, ssid, rssi);

                } while (cr.moveToNext());
            }

            else {
                Toast.makeText(getApplicationContext(),"No data in the database",Toast.LENGTH_SHORT).show();
            }
        }

    }

    public void showMessage(String title,String Message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(Message);
        builder.show();
    }

    public BitmapDescriptor getMarkerIcon(String color) {
        float[] hsv = new float[3];
        Color.colorToHSV(Color.parseColor(color), hsv);
        return BitmapDescriptorFactory.defaultMarker(hsv[0]);
    }



    private void drawMarker(LatLng point,String ssid,int rssi){

        String d1 = Integer.toString(rssi);

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(point);
        markerOptions.title(ssid);

        /*if(rssi >= -35){
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
        }
        else if (rssi >= -60 && rssi < -35){
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
        }
        else if (rssi >= -70 && rssi < -60){
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET));
        }
        else if (rssi >= -90 && rssi < -70){
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE));
        }
        else if (rssi < -90){
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
        }*/

        if(rssi >= -35){
            markerOptions.icon(getMarkerIcon("#6bb120"));
        }
        else if (rssi >= -60 && rssi < -35){
            markerOptions.icon(getMarkerIcon("#9afe2e"));
        }
        else if (rssi >= -70 && rssi < -60){
            markerOptions.icon(getMarkerIcon("#aefe57"));
        }
        else if (rssi >= -90 && rssi < -70){
            markerOptions.icon(getMarkerIcon("#fe5757"));
        }
        else if (rssi < -90){
            markerOptions.icon(getMarkerIcon("#cb2424"));
        }

        mMap.addMarker(markerOptions);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(point,15));

    }
}
