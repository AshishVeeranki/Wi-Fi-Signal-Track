package com.example.awnproj2;


        import android.app.Activity;
        import android.app.AlertDialog;
        import android.content.BroadcastReceiver;
        import android.content.Context;
        import android.content.Intent;
        import android.content.IntentFilter;
        import android.database.Cursor;
        import android.net.wifi.ScanResult;
        import android.net.wifi.WifiManager;
        import android.os.Bundle;
        import android.support.design.widget.FloatingActionButton;
        import android.support.design.widget.Snackbar;
        import android.support.v7.app.AppCompatActivity;
        import android.support.v7.widget.Toolbar;
        import android.view.View;
        import android.view.Menu;
        import android.view.MenuItem;
        import android.widget.AdapterView;
        import android.widget.ArrayAdapter;
        import android.widget.Button;
        import android.widget.ListView;
        import android.widget.Toast;

        import java.util.List;
        import java.util.Timer;
        import java.util.TimerTask;

public class MainActivity extends Activity {
    ListView lv;
    WifiManager wifi;
    WifiScanReceiver wifiReciever;
    String wifis[];
    String sw;
    int time = 1000;
    Context ct=this;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lv=(ListView)findViewById(R.id.listView);
        wifi=(WifiManager)getSystemService(Context.WIFI_SERVICE);
        wifiReciever = new WifiScanReceiver();
        wifi.startScan();


        Timer myTimer = new Timer();
        myTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                ServerDetails sd = new ServerDetails(ct);
                String sleepTime = Integer.toString(time);
                sd.execute(sleepTime);
            }
        }, 0, 60000);


    }


    public void showMessage(String title,String Message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(Message);
        builder.show();
    }


    protected void onPause() {
        unregisterReceiver(wifiReciever);
        super.onPause();
    }

    protected void onResume() {
        registerReceiver(wifiReciever, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        super.onResume();
    }


    private class WifiScanReceiver extends BroadcastReceiver {

        public void onReceive(Context c, Intent intent) {

            List<ScanResult> wifiScanList = wifi.getScanResults();
            wifis = new String[wifiScanList.size()];

            for (int i = 0; i < wifiScanList.size(); i++) {
                // wifis[i] = ((wifiScanList.get(i)).toString());

                wifis[i] = ((wifiScanList.get(i)).SSID.toString()
                        +","+Integer.toString((wifiScanList.get(i)).level)
                        +","+Long.toString((wifiScanList.get(i)).timestamp)
                        +","+(wifiScanList.get(i)).BSSID.toString());




            }

            lv.setAdapter(new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, wifis));
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view,
                                        int position, long id) {
                    sw= (adapterView.getItemAtPosition(position)).toString();
                    Intent x = new Intent(view.getContext(), Details.class);
                    x.putExtra("w",sw);
                    startActivity(x);

                }
            });





        }
    }

}
