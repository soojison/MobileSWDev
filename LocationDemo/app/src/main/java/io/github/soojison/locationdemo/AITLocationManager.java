package io.github.soojison.locationdemo;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import java.security.Security;

public class AITLocationManager implements LocationListener {


    public interface OnNewLocationAvailible {
        public void onNewLocation(Location location);
    }

    private LocationManager locationManager;
    private OnNewLocationAvailible onNewLocationAvailible;

    public AITLocationManager(OnNewLocationAvailible onNewLocationAvailible) {
        this.onNewLocationAvailible = onNewLocationAvailible;
    }

    public void startLocationMonitoring(Context context) throws SecurityException {
        locationManager = (LocationManager) context.getSystemService(
                Context.LOCATION_SERVICE);

        // some emulators don't like the network provider
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);

        // two types of permission!
        // user privacy-wise, your current location is a dangerous permission
        // you must ask for permissions at runtime as well in newer android version
        // or you could just put throws SecurityException, but the app will not work because
        // we didn't ask for the permission
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
    }

    public void stopLocationMonitoring() {
        locationManager.removeUpdates(this);
    }

    @Override
    public void onLocationChanged(Location location) {
        onNewLocationAvailible.onNewLocation(location);
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
