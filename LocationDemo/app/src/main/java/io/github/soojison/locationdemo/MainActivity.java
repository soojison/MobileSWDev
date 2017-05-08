package io.github.soojison.locationdemo;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity
        implements AITLocationManager.OnNewLocationAvailible {

    private TextView tvData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvData = (TextView) findViewById(R.id.tvData);
    }

    @Override
    public void onNewLocation(Location location) {
        tvData.setText("Lat: " + location.getLatitude() + "\n" +
                "Long: " + location.getLongitude());
    }

    // GROSS!!
    private void requestNeededPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // request for permission
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                // Toast or dialog or whatever tell
                Toast.makeText(this, "u fucked up", Toast.LENGTH_SHORT).show();
            }
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    101);

        } else {
            // start doing our thang
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        // called if user has answered the permission question
        if (requestCode == 101) {
            if(grantResults.length > 0 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission granted jupeee!", Toast.LENGTH_SHORT).show();
                // do our job
            } else {
                Toast.makeText(this, "Permission was not granted. Go to hell :)", Toast.LENGTH_SHORT).show();
            }

        }

    }
}
