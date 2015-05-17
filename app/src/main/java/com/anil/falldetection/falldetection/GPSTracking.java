package com.anil.falldetection.falldetection;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

public class GPSTracking extends Service implements LocationListener {
    
                private final Context mContext;
                boolean isGPSEnabled = false;
                boolean isNetworkEnabled = false;
                boolean canGetLocation = false;
                String locationOrigin = "N/A";
                Location location; // location
                double latitude; // latitude
                double longitude; // longitude
                double locationAccuracy;
     
                private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 1; // 1 meter
                private static final long MIN_TIME_BW_UPDATES = 1000 * 30 * 1; // 1 minute
     
                protected LocationManager locationManager;
     
                public GPSTracking(Context context) {
                this.mContext = context;
                getLocation();
            }
     
                public Location getLocation() {
                try {
                        locationManager = (LocationManager) mContext
                                .getSystemService(LOCATION_SERVICE);
             
                        isGPSEnabled = locationManager
                                .isProviderEnabled(LocationManager.GPS_PROVIDER);
             
                        isNetworkEnabled = locationManager
                                .isProviderEnabled(LocationManager.NETWORK_PROVIDER);
             
                        if (!isGPSEnabled && !isNetworkEnabled) {
                            } else {
                                this.canGetLocation = true;
                                location = null;

                                if (isNetworkEnabled) {
                                        locationManager.requestLocationUpdates(
                                                        LocationManager.NETWORK_PROVIDER,
                                                        MIN_TIME_BW_UPDATES,
                                                        MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                                        Log.d("Network", "Network");
                                        if (locationManager != null) {
                                                location = locationManager
                                                        .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                                                if (location != null) {
                                                        latitude = location.getLatitude();
                                                        longitude = location.getLongitude();
                                                        locationOrigin = "Network";
                                                        locationAccuracy = location.getAccuracy();
                                                    }
                                            }
                                    }

                                if (isGPSEnabled) {
                                        if (location == null) {
                                                locationManager.requestLocationUpdates(
                                                                LocationManager.GPS_PROVIDER,
                                                                MIN_TIME_BW_UPDATES,
                                                                MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                                                Log.d("GPS Enabled", "GPS Enabled");
                                                if (locationManager != null) {
                                                        location = locationManager
                                                                .getLastKnownLocation(LocationManager.GPS_PROVIDER);
                                                        if (location != null) {
                                                                latitude = location.getLatitude();
                                                                longitude = location.getLongitude();
                                                                locationOrigin = "GPS";
                                                                locationAccuracy = location.getAccuracy();
                                                            }
                                                    }
                                            }
                                    }
                            }
             
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
         
                return location;
            }

                public double getLatitude(){
                if(location != null){
                        latitude = location.getLatitude();
                    }
                 
                // return latitude
                return latitude;
            }
         
                public double getLongitude(){
                if(location != null){
                        longitude = location.getLongitude();
                    }
                 
                // return longitude
                return longitude;
            }
         
       public boolean canGetLocation() {
                return this.canGetLocation;
            }
         

                @Override
        public void onLocationChanged(Location location) {
            }
     
                @Override
        public void onProviderDisabled(String provider) {
            }
     
                @Override
        public void onProviderEnabled(String provider) {
            }
     
                @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            }
     
                @Override
        public IBinder onBind(Intent arg0) {
                return null;
            }
     
}