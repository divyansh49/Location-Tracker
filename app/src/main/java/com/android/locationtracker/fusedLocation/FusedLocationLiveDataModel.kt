package com.android.locationtracker.fusedLocation

import android.app.Application
import android.location.Location
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData


class FusedLocationLiveDataModel(application: Application) : AndroidViewModel(application) {

    private val liveData =
        FusedLocationLiveData(
            application
        )

    val getLocationData: LiveData<Location> = liveData

}