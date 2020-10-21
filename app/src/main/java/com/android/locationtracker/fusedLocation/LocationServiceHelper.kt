package com.android.locationtracker.fusedLocation

import android.app.Activity
import android.content.Context
import android.content.IntentSender
import android.location.LocationManager
import android.widget.Toast
import com.android.locationtracker.activities.REQUEST_CODE
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsStatusCodes
import com.google.android.gms.location.SettingsClient

class LocationServiceHelper(private val context: Context) {

    private val client: SettingsClient = LocationServices.getSettingsClient(context)
    private val request: LocationSettingsRequest?
    private val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager


    fun enableGPS(OnGpsListener: OnLocationOnListener?) {

        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            OnGpsListener?.locationStatus(true)
        } else {
            client
                .checkLocationSettings(request)
                .addOnSuccessListener(context as Activity) {
                    OnGpsListener?.locationStatus(true)
                }
                .addOnFailureListener(context) { exception ->
                    when ((exception as ApiException).statusCode) {
                        LocationSettingsStatusCodes.RESOLUTION_REQUIRED ->

                            try {
                                val resolvableApiException = exception as ResolvableApiException
                                resolvableApiException.startResolutionForResult(
                                    context,
                                    REQUEST_CODE
                                )
                            } catch (sendIntentException: IntentSender.SendIntentException) {
                                Toast.makeText(context, sendIntentException.message, Toast.LENGTH_SHORT).show()
                            }

                        LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {
                            Toast.makeText(context, "Settings change unavailable. Try enabling location service from settings.", Toast.LENGTH_LONG).show()
                        }
                    }
                }

        }

    }

    init {
        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(FusedLocationLiveData.locationRequest)
        request = builder.build()
        builder.setAlwaysShow(true)
    }

    interface OnLocationOnListener {
        fun locationStatus(isLocationOn: Boolean)
    }
}