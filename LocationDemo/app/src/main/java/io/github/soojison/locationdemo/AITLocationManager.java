package io.github.soojison.locationdemo;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

public class AITLocationManager implements LocationListener {


    public interface OnNewLocationAvailible {
        public void onNewLocation(Location location);
    }

    public void startLocationMonitoring(Context context) {
        LocationManager locationManager = (LocationManager) context.getSystemService(
                Context.LOCATION_SERVICE);

        // two types of permission!
        // user privacy-wise, your current location is a dangerous permission
        // you must ask for permissions at runtime as well in newer android version
        // or you could just put throws SecurityException, but the app will not work because
        // we didn't ask for the permission
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
    }

    public void stopLocationMonitoring() {

    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

}
