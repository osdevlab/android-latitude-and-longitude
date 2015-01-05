package com.example.osdevlab.simpletutorial;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Toast;

/**
 * Created by osdevlab on 1/1/15.
 */

//we need to first implements LocationListener
//Four functions will be provided by IDE when implements LocationListener and we have to override those functions.
//For now, we don't need to override those functions yet.

public class GpsHelperClass implements LocationListener {

    protected LocationManager locationManager;
    Context context;
    Location location;

    /*At constructor, context is passed from FragmentOne.java and
    context will be used by GpsHelperClass throughout.*/
    public GpsHelperClass(Context context) {
        this.context = context;
    }
    /*Pass context from FragmentOne; cannot find any neat way to pass*/
    public void setContext(Context context) {
        this.context = context;
    }

    /*To removes all location updates.Save battery*/
    public void removeGpsUpdate() {
        locationManager.removeUpdates(GpsHelperClass.this);
    }

    /*If GPS is not turn on yet, then ask user to turn on for us. If GPS is turn on, */

    //context is passed from FragmentOne.java and context will be used by this function
    public boolean isGpsReadyToUse() {
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

      /* If there is no gps provider, ask user to activate
        We cannot enable GPS via program.
        Still need user input to enable for good reason.*/
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            getUserInputToEnableGPS();
        } else {

            //500 ==  minTime	minimum time interval between location updates, in milliseconds
            //1 ==  minDistance	minimum distance between location updates, in meters
            locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    500,
                    1, this);
            if (locationManager != null) {
                location = locationManager
                        .getLastKnownLocation(LocationManager.GPS_PROVIDER);
                if (location != null) {

                    String latitude = String.valueOf(location.getLatitude());
                    String longitude = String.valueOf(location.getLongitude());
                    //small pop up for Lat and Long
                    Toast.makeText(context, "Lat: " + latitude + "\nLong: " + longitude,
                            Toast.LENGTH_SHORT).show();
                }
            }
        }
        //return the status of GPS; whether it is ON or OFF.
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    /*To get user to enable GPS through dialog*/

    private void getUserInputToEnableGPS() {
        //Ref from http://www.androidhive.info/2012/07/android-gps-location-manager-tutorial
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        alertDialog.setTitle("GPS is settings");

        // Setting Dialog Message
        alertDialog.setMessage("GPS is not enabled. Do you want to go to settings menu?");

        // When user presses 'Settings' button
        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                //Settings.ACTION_LOCATION_SOURCE_SETTINGS == Activity Action: Show settings to allow configuration of current location sources.
                // The Settings provider contains global system-level device preferences.
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);

                //Every Activity is invoked by an Intent. Therefore, we are going to start activity which allow to set location
                context.startActivity(intent);
            }
        });

        // when user presses 'Cancel' button
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        // Showing Alert Message
        alertDialog.show();
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
