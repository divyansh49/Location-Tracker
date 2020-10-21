package com.android.locationtracker.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.android.locationtracker.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions


class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private var lat: Double = 0.0
    private var lng: Double = 0.0
    private var elat: Double = 0.0
    private var elng: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        lat = intent.getDoubleExtra("lat", 0.0)
        lng = intent.getDoubleExtra("lng", 0.0)
        elat = intent.getDoubleExtra("elat", 0.0)
        elng = intent.getDoubleExtra("elng", 0.0)

    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        if (lat != 0.0 && lng != 0.0) {

            val myloc = LatLng(lat, lng)
            mMap.addMarker(MarkerOptions().position(myloc).title("My Location"))
            mMap.moveCamera(CameraUpdateFactory.newLatLng(myloc))

        }

        if (elat != 0.0 && elng != 0.0) {

            val dest = LatLng(elat, elng)
            mMap.addMarker(MarkerOptions().position(dest).title("Destination"))
            mMap.moveCamera(CameraUpdateFactory.newLatLng(dest))
        }
    }
}
