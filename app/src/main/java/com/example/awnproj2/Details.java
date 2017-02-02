package com.example.awnproj2;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import android.util.Log;
import android.widget.Toast;


public class Details extends Activity {

    String e1;
    Button map;
    Context ctx = this;
    LocTrack loc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.toview);


        TextView txt = (TextView) findViewById(R.id.textView2);
       // final TextView txt2 = (TextView) findViewById(R.id.textView3);

        Button save = (Button) findViewById(R.id.button1);
        Button ret = (Button) findViewById(R.id.ret);

        Intent intent = getIntent();
        e1 = intent.getStringExtra("w");


        final String ssid,deviceid;
        final String timestamp,rssi;
        final double latitude;
        final double longitude;
        double lat=0,lon=0;

        loc = new LocTrack(ctx);

        if(loc.getLoc()){
            lat = loc.getLatitude();
            lon = loc.getLongitude();
        }
        else {
            loc.navtoSettings();
        }

        String Str = e1;
        String[] parts = Str.split(",");

        ssid = parts[0];
        rssi = parts[1];
        timestamp = parts[2];
        deviceid = parts[3];
        latitude = lat;
        longitude = lon;

        txt.setText("SSID: "+ssid+", RSSI: "+rssi+", TIME: "+timestamp+", DEV ID: "+deviceid+", LATITUDE: "+latitude+", LONGITUDE: "+longitude);




        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatabaseHandler dops = new DatabaseHandler(ctx);
                dops.insertDetails(dops,ssid,rssi,timestamp,deviceid,latitude,longitude);
                Toast.makeText(getApplicationContext(),"data inserted",Toast.LENGTH_LONG).show();

            }
        });



        ret.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StringBuffer buffer = new StringBuffer();
                DatabaseHandler db3 = new DatabaseHandler(ctx);
                Cursor cr = db3.getDetails(db3);
                cr.moveToFirst();

                do
                {
                    buffer.append("SSID: "+cr.getString(0)+",");
                    buffer.append("RSSI: "+cr.getString(1)+",");
                    buffer.append("TIME: "+cr.getString(2)+",");
                    buffer.append("DEVICEID: "+cr.getString(3)+",");
                    buffer.append("LATITUDE: "+cr.getString(4)+",");
                    buffer.append("LONGITUDE: "+cr.getString(5));
                }while (cr.moveToNext());

                showMessage("details:",buffer.toString());

                Toast.makeText(getApplicationContext(),"data retreived",Toast.LENGTH_SHORT).show();
            }
        });

        map = (Button)findViewById(R.id.map);
        map.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){

                Intent m= new Intent(Details.this,MapsActivity.class);
                startActivity(m);

            }
        });


    }
    public void showMessage(String title,String Message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(Message);
        builder.show();
    }

}

