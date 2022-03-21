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
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.janirefernandez.weatherapp.R
import com.janirefernandez.weatherapp.databinding.ActivityMainBinding
import com.janirefernandez.weatherapp.ui.viewmodel.WeatherViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    lateinit var locationManager: LocationManager
    private var gpsLocationListener: LocationListener? = null
    private var hasGps: Boolean = false
    private var dialogGps: Dialog? = null

    private lateinit var binding: ActivityMainBinding
    private val weatherViewModel: WeatherViewModel by viewModels()

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
        binding = ActivityMainBinding.inflate(layoutInflater)
        binding.weatherView.isVisible = false
        binding.loading.isVisible = true
        setContentView(binding.root)

        Glide.with(binding.cityIcon.context)
            .load("https://cdn.pixabay.com/photo/2020/01/24/21/33/city-4791269_960_720.png")
            .fitCenter()
            .into(binding.cityIcon)

        locationManager = this.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        weatherViewModel.weatherResponse.observe(this) {
            binding.weatherView.isVisible = true
            binding.loading.isVisible = false
            binding.currentDate.text = Calendar.getInstance().time.toString()
            binding.location.text = it.name
            binding.minTempMain.text = String.format(it.main.tempMin.toString() + "°")
            binding.minTempValue.text = String.format(it.main.tempMin.toString() + "°")
            binding.maxTempValue.text = String.format(it.main.tempMax.toString() + "°")
            binding.windSpeedValue.text = String.format(it.wind.speed.toString() + " m/s")
            binding.humidityValue.text = String.format(it.main.humidity.toString() + "%%")
        }
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
                weatherViewModel.onCreate(location.latitude, location.longitude)
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