package com.android.app.feature.location

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng


class CurrentLocation constructor(private var activity : Activity){

    private var fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(activity)
    private var locationPermissionGranted: Boolean = false
    private var location  : LatLng = LatLng(60.192059, 24.945831)
    private var currentLocationListener : CurrentLocationUpdate? = null
    private var destroy = false

    companion object{
        private const val PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 20210
        const val DEFAULT_ZOOM = 15
    }

    init {
        getLocationPermission()
    }

    /**
     * Gets the current location of the device, and positions the map's camera.
     */
    fun getDeviceLocation(listener : CurrentLocationUpdate) {
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */
        this.currentLocationListener = listener
        try {
            getLocationPermission()
            if (locationPermissionGranted) {
                val locationResult = fusedLocationProviderClient.lastLocation
                locationResult.addOnCompleteListener(activity) { task ->
                    if (task.isSuccessful) {
                        // Set the map's camera position to the current location of the device.
                        if (task.result != null) {
                            location = LatLng(task.result.latitude,task.result.longitude)
                        }
                    }
                    onCurrentLocationUpdate()
                }
            }else{
                getLocationPermission()
            }
        } catch (e: SecurityException) {
            onCurrentLocationUpdate()
        }
    }

    private fun onCurrentLocationUpdate() {
        currentLocationListener?.currentLocationUpdate(location)
    }

    /**
     * Prompts the user for permission to use the device location.
     */
    private fun getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(activity.applicationContext,
                Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED) {
            locationPermissionGranted = true
        } else {
            ActivityCompat.requestPermissions(activity, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION)
        }
    }

    /**
     * Handles the result of the request for location permissions.
     */
    fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        locationPermissionGranted = false
        when (requestCode) {
            PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION -> {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.isNotEmpty() &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    locationPermissionGranted = true
                    currentLocationListener?.let {
                        getDeviceLocation(it)
                    }
                }
            }
        }
    }


    fun destroy(){
        destroy = true
        currentLocationListener = null
    }

    interface CurrentLocationUpdate{
        fun currentLocationUpdate(location  : LatLng)
    }

}