package com.example.socialapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;



public class MainActivity extends AppCompatActivity {

    private LocationManager locationManager;
    private String provider;
    private String currentLocation;
    LocationListener locationListener = new LocationListener() {
        @Override
        public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onProviderEnabled(String arg0) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onProviderDisabled(String arg0) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onLocationChanged(Location arg0) {
            // TODO Auto-generated method stub
            // 更新当前经纬度
            currentLocation = "latitude is: " + String.valueOf(arg0.getLatitude()) + " longitude is: " + String.valueOf(arg0.getLongitude()) + " altitude is: " + String.valueOf(arg0.getAltitude());
            Log.i(getClass().getSimpleName(), currentLocation);
        }
    };

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        LinearLayout lm = findViewById(R.id.linearLayout);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        for (int i=0; i<10; i++)
        {
            View testLayoutView = View.inflate(this, R.layout.test_layout, null);
            lm.addView(testLayoutView);
        }



        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                try  {
                    //Your code goes here
                    String returnString = connectTomcatServer("http://192.168.136.244:8080/socialappservice/servlet/HelloServlet/");
                    Log.i(getClass().getSimpleName(), "received message is: " + returnString);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        List<String> list = locationManager.getProviders(true);

        if (list.contains(LocationManager.GPS_PROVIDER)) {
            provider = LocationManager.GPS_PROVIDER;
        } else if (list.contains(LocationManager.NETWORK_PROVIDER)) {
            provider = LocationManager.NETWORK_PROVIDER;
        } else {
            return;
        }



        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        provider = LocationManager.GPS_PROVIDER;

        locationManager.requestLocationUpdates(provider, 2000, 2, locationListener);
        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        //return "App fetch location information failed";
    }

    public void ClickButton(View view) {
        switch (view.getId()) {
            case R.id.projectButton:
                Log.i(getClass().getSimpleName(), "click something");
                break;
            default:
                break;
        }
    }

    String connectTomcatServer(String addr) {
        try {
            String name = URLEncoder.encode("test", "utf-8");
            URL url = new URL(addr);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setDoInput(true);
            connection.setConnectTimeout(15000);
            connection.setReadTimeout(10000);
            Log.i(getClass().getSimpleName(), "apply connection start");
            connection.connect();
            Log.i(getClass().getSimpleName(), "apply connection done");

            InputStream inputStream = connection.getInputStream();
            BufferedReader bufferReader = new BufferedReader(new InputStreamReader(inputStream, "utf-8"));
            String line;
            StringBuilder stringBuilder = new StringBuilder();
            while ((line = bufferReader.readLine()) != null)
            {
                stringBuilder.append(line);
            }
            return stringBuilder.toString();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String FetchGPSLocation() {
        return currentLocation;
    }
}