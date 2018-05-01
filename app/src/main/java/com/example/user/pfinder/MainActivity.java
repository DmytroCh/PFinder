package com.example.user.pfinder;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.user.pfinder.Accelerometer.FallDetection;
import com.example.user.pfinder.Location.MyLocation;
import com.google.android.gms.common.api.GoogleApiClient;

public class MainActivity extends AppCompatActivity{

    private TextView latitudeTextView,longitudeTextView, square;

    private final static int REQUEST_CHECK_SETTINGS_GPS=0x1;
    private MyLocation myLocation;
    FallDetection fallDetection;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        declareVariables();
    }

    private void declareVariables(){
        latitudeTextView=(TextView)findViewById(R.id.latitudeTextView);
        longitudeTextView=(TextView)findViewById(R.id.longitudeTextView);
        square=(TextView)findViewById(R.id.square);
        myLocation = new MyLocation(this, latitudeTextView, longitudeTextView);
        myLocation.setUpGClient();
        fallDetection = new FallDetection(this);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CHECK_SETTINGS_GPS:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        myLocation.getMyLocation();
                        break;
                    case Activity.RESULT_CANCELED:
                        finish();
                        break;
                }
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        int permissionLocation = ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        if (permissionLocation == PackageManager.PERMISSION_GRANTED) {
            myLocation.getMyLocation();
        }
    }
}
