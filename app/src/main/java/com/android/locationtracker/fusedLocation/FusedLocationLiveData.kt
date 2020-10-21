package com.android.locationtracker.fusedLocation

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices

class FusedLocationLiveData(context: Context) : MutableLiveData<Location>() {


    private var locationClient = LocationServices.getFusedLocationProviderClient(context)

    companion object {
        val locationRequest: LocationRequest = LocationRequest.create()
            .apply {
                interval = 2000L
                fastestInterval = 1000L
                priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            }
    }


    @SuppressLint("MissingPermission")
    override fun onActive() {
        super.onActive()
        locationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                location?.also {
                    locationData(it)
                }
            }
        updateLocation()
    }

    private fun locationData(location: Location) {
        value = location
    }

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult?) {
            locationResult ?: return
            for (location in locationResult.locations) {
                locationData(location)
            }
        }
    }


    @SuppressLint("MissingPermission")
    private fun updateLocation() {
        locationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            null
        )
    }

    override fun onInactive() {
        super.onInactive()
        locationClient.removeLocationUpdates(locationCallback)
    }

}