package com.janirefernandez.weatherapp.ui.view

import android.Manifest
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.janirefernandez.weatherapp.R


class MainActivity : AppCompatActivity() {

    lateinit var locationManager: LocationManager
    private var gpsLocationListener: LocationListener? = null
    private var hasGps: Boolean = false
    private var dialogGps: Dialog? = null
    private val launcher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            hasGps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
            if (hasGps) {
                requestLocationUpdate()
            } else {
                showGPSDisabledDialog()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        locationManager = this.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        setContentView(R.layout.activity_main)
    }

    override fun onResume() {
        getLocation()
        super.onResume()
    }

    private fun getLocation() {
        hasGps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        gpsLocationListener = object : LocationListener {
            override fun onLocationChanged(location: Location) {
                dialogGps?.dismiss()
                Toast.makeText(
                    applicationContext,
                    "My current location is: " + "Latitud =" + location.latitude + "Longitud = " + location.longitude,
                    Toast.LENGTH_SHORT
                ).show()
            }
            override fun onProviderEnabled(provider: String) {}
            override fun onProviderDisabled(provider: String) {
                showGPSDisabledDialog()
            }
        }
        requestLocationUpdate()
    }

    private fun requestLocationUpdate() {
        if ((ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED)

        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                ),
                255
            )
            return
        }

        locationManager.requestLocationUpdates(
            LocationManager.NETWORK_PROVIDER,
            0,
            0F,
            gpsLocationListener!!
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        if (!hasGps) {
            launcher.launch(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
        } else {
            requestLocationUpdate()
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private fun showGPSDisabledDialog() {
        dialogGps?.dismiss()
        val builder: android.app.AlertDialog.Builder = android.app.AlertDialog.Builder(this)
        builder.setTitle(R.string.gps_dialog_title)
        builder.setMessage(R.string.gps_dialog_message)
        builder.setPositiveButton(R.string.gps_dialog_positive) { _, _ ->
            launcher.launch(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
            dialogGps!!.dismiss()
        }.setNegativeButton(R.string.gps_dialog_negative) { _, _ -> }
        dialogGps = builder.create()
        dialogGps!!.show()
    }
}