package com.android.locationtracker.activities

import android.Manifest
import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.androidisland.ezpermission.EzPermission
import com.android.locationtracker.fusedLocation.FusedLocationLiveDataModel
import com.android.locationtracker.fusedLocation.LocationServiceHelper
import com.android.locationtracker.R
import kotlinx.android.synthetic.main.activity_mylocation.*


const val REQUEST_CODE = 123

class MyLocationActivity : AppCompatActivity() {

    var lat : Double = 0.0
    var lng : Double = 0.0

    private lateinit var model: FusedLocationLiveDataModel
    private var gpsEnabled = false

    private val permissions = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
    )

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mylocation)

        model = ViewModelProviders.of(this).get(FusedLocationLiveDataModel::class.java)

        LocationServiceHelper(this).enableGPS(object :
            LocationServiceHelper.OnLocationOnListener {

            override fun locationStatus(isLocationOn: Boolean) {
                this@MyLocationActivity.gpsEnabled = isLocationOn
            }
        })

        btnmap.setOnClickListener {

            val intent = Intent(this, MapsActivity::class.java)
            intent.putExtra("lat", lat)
            intent.putExtra("lng", lng)
            startActivity(intent)

        }

        btnshare.setOnClickListener {

            val whatsappIntent = Intent(Intent.ACTION_SEND)
            whatsappIntent.type = "text/plain"
            whatsappIntent.setPackage("com.whatsapp")
            whatsappIntent.putExtra(Intent.EXTRA_TEXT, "Latitude : $lat and Longitude : $lng")
            try {
                startActivity(whatsappIntent)
            } catch (ex: ActivityNotFoundException) {
                Toast.makeText(this,"WhatsApp has not been installed.",Toast.LENGTH_SHORT).show()
            }

        }

    }

    private fun locationUpdates() {

        model.getLocationData.observe(this, Observer {
            lat = it.latitude
            lng = it.longitude
            longitude.text = it.longitude.toString()
            latitude.text = it.latitude.toString()
        })

    }

    private fun startObservingLocation() {
        when {
            !gpsEnabled -> {
                Toast.makeText(this, "Enable GPS!", Toast.LENGTH_SHORT).show()
            }

            permissionsGranted() -> {
                locationUpdates()
            }
            else -> {
                askPermissions()
            }
        }
    }


    override fun onStart() {
        super.onStart()
        startObservingLocation()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_CODE) {
                gpsEnabled = true
                startObservingLocation()
            }
        }
    }

    private fun askPermissions() {
        EzPermission
                .with(this)
                .permissions(permissions[0], permissions[1])
                .request { granted, denied, permanentlyDenied ->
                    if (granted.contains(permissions[0]) && granted.contains(permissions[1])) {

                        startObservingLocation()

                    } else if (denied.contains(permissions[0]) || denied.contains(permissions[1])) {

                        permissionsDeniedDialogBox()

                    } else if (permanentlyDenied.contains(permissions[0]) || permanentlyDenied.contains(permissions[1])) {

                        permissionsPermanentlyDenied()

                    }

                }
    }

    private fun permissionsGranted(): Boolean {
        return (EzPermission.isGranted(this, permissions[0]) && EzPermission.isGranted(this, permissions[1]))
    }

    private fun permissionsPermanentlyDenied() {

        val dialog = AlertDialog.Builder(this)
        dialog.setTitle("Permissions Permanently Denied")
        dialog.setMessage("Application cannot function without GPS.")
        dialog.setNegativeButton("Not Now") { _, _ -> }
        dialog.setPositiveButton("Settings") { _, _ ->
            startActivity(
                    EzPermission.appDetailSettingsIntent(
                            this
                    )
            )
        }
        dialog.setOnCancelListener { }
        dialog.show()

    }


    private fun permissionsDeniedDialogBox() {

        val dialog = AlertDialog.Builder(this)
        dialog.setTitle("Permissions Denied")
        dialog.setMessage("Application cannot function without GPS.")
        dialog.setNegativeButton("Cancel") { _, _ -> }
        dialog.setPositiveButton("Allow") { _, _ ->
            askPermissions()
        }
        dialog.setOnCancelListener { }
        dialog.show()

    }

}