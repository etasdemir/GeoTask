package com.elacqua.geotask.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.location.Location
import android.location.LocationManager
import android.os.Looper
import android.provider.Settings
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.elacqua.geotask.R
import com.huawei.hms.common.ApiException
import com.huawei.hms.common.ResolvableApiException
import com.huawei.hms.location.*
import timber.log.Timber


object LocationFinder : LocationCallback() {

    @SuppressLint("StaticFieldLeak")
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private val mLocation = MutableLiveData<Location>()
    val location: LiveData<Location> = mLocation

    override fun onLocationResult(location: LocationResult?) {
        if (location != null) {
            val locations: List<Location> = location.locations
            if (locations.isNotEmpty()) {
                for (element in locations) {
                    mLocation.value = element
                }
            }
        }
    }

    fun getLocation(activity: AppCompatActivity) {
        val locationManager =
            activity.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
            locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        ) {
            listenLocationChanges(activity)
        } else {
            turnOnGPS(activity)
        }
    }

    private fun listenLocationChanges(activity: AppCompatActivity) {
        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(activity)
        val settingsClient = LocationServices.getSettingsClient(activity)
        val mLocationRequest = LocationRequest().apply {
            interval = 500
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
        try {
            val builder = LocationSettingsRequest.Builder()
            builder.addLocationRequest(mLocationRequest)
            val locationSettingsRequest = builder.build()
            settingsClient.checkLocationSettings(locationSettingsRequest)
                .addOnSuccessListener {
                    Timber.e("check location settings success")
                    fusedLocationProviderClient.requestLocationUpdates(
                        mLocationRequest,
                        this,
                        Looper.getMainLooper()
                    )
                        .addOnSuccessListener { Timber.e("requestLocationUpdatesWithCallback onSuccess") }
                        .addOnFailureListener { e ->
                            Timber.e("requestLocationUpdatesWithCallback onFailure: ${e.message}")
                        }
                }
                .addOnFailureListener { e ->
                    Timber.e("checkLocationSetting onFailure: ${e.message}")
                    when ((e as ApiException).statusCode) {
                        LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> try {
                            val rae = e as ResolvableApiException
                            rae.startResolutionForResult(
                                activity,
                                0
                            )
                        } catch (sie: IntentSender.SendIntentException) {
                        }
                    }
                }
        } catch (e: Exception) {
            Timber.e("requestLocationUpdatesWithCallback exception: ${e.message}")
        }
    }

    fun resumeLocationListening(locationInterval: Long) {
        if (!LocationFinder::fusedLocationProviderClient.isInitialized) {
            return
        }
        try {
            val mLocationRequest = LocationRequest().apply {
                interval = locationInterval
                priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            }
            fusedLocationProviderClient.requestLocationUpdates(
                mLocationRequest,
                this,
                Looper.getMainLooper()
            )
                .addOnSuccessListener { Timber.e("requestLocationUpdatesWithCallback onSuccess") }
                .addOnFailureListener { e ->
                    Timber.e("requestLocationUpdatesWithCallback onFailure: ${e.message}")
                }
        } catch (e: java.lang.Exception) {
            Timber.e("removeLocationUpdatesWithCallback exception: ${e.message}")
        }
    }

    fun stopLocationListening() {
        if (!LocationFinder::fusedLocationProviderClient.isInitialized) {
            return
        }
        try {
            fusedLocationProviderClient.removeLocationUpdates(this)
                .addOnSuccessListener {
                    Timber.e("removeLocationUpdatesWithCallback onSuccess")
                }
        } catch (e: java.lang.Exception) {
            Timber.e("removeLocationUpdatesWithCallback exception: ${e.message}")
        }
    }

    private fun turnOnGPS(activity: AppCompatActivity) {
        val builder: AlertDialog.Builder = AlertDialog.Builder(activity)
        builder
            .setMessage(activity.getString(R.string.map_request_gps))
            .setCancelable(false)
            .setPositiveButton(activity.getString(R.string.open_settings)) { _, _ ->
                activity.startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
            }
            .setNegativeButton(activity.getString(R.string.cancel)) { dialog, _ ->
                dialog.cancel()
            }
        val alertDialog: AlertDialog = builder.create()
        alertDialog.show()
    }
}