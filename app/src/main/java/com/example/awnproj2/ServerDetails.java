package com.example.awnproj2;



import android.content.Context;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import static android.content.Context.CONNECTIVITY_SERVICE;


public class ServerDetails extends AsyncTask<String,Void,String> {

    Context ctx;

    ServerDetails(Context ctx){
        this.ctx = ctx;
    }

    @Override
    protected void onPreExecute(){
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... params) {

        String result= "";

        ConnectivityManager CM = (ConnectivityManager)ctx.getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo ninfo = CM.getActiveNetworkInfo();

        if(ninfo != null && ninfo.isConnected()) {

            try {
                int time = Integer.parseInt(params[0]);


                DatabaseHandler dopsm = new DatabaseHandler(ctx);
                Cursor cr = dopsm.getDetails(dopsm);

                cr.moveToFirst();
                do {
                    String ssid = cr.getString(0);
                    int rssi = Integer.parseInt(cr.getString(1));
                    long timestamp = Long.parseLong(cr.getString(2));
                    String deviceid = cr.getString(3);
                    Double latitude = (cr.getDouble(4));
                    Double longitude = (cr.getDouble(5));

                    String link = "http://project2website.000webhostapp.com/insert_data.php?ssid="+ssid+"&rssi="+rssi+"&lat="+latitude+"&long="+longitude+"&timestamp="+timestamp+"&deviceid="+deviceid;

                    URL url = new URL(link);
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("GET");

                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                    StringBuffer sb = new StringBuffer("");

                    String line = "";

                    while ((line = bufferedReader.readLine()) != null) {
                        sb.append(line);
                        break;
                    }

                    bufferedReader.close();

                } while (cr.moveToNext());



                result = "Data Uploaded";


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        else {

            result = "No Internet";
        }
        return result;
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(String result) {
        Toast.makeText(ctx,result,Toast.LENGTH_SHORT).show();
    }


}
