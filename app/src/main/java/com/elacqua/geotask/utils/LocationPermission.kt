package com.elacqua.geotask.utils

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.view.View
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.requestPermissions
import com.elacqua.geotask.R
import com.google.android.material.snackbar.Snackbar


object LocationPermission {

    private lateinit var successCallback: () -> Unit

    fun requestPermission(view: View, activity: AppCompatActivity, callback: () -> Unit) {
        successCallback = callback
        if (hasLocationPerms(activity)) {
            successCallback()
        } else {
            requestPermissions(
                activity, arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ), Constants.LOCATION_PERM_CODE
            )
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                showSnackBar(view, activity)
            }
        }
    }

    fun hasLocationPerms(activity: Activity): Boolean {
        val foreground = ActivityCompat.checkSelfPermission(
            activity,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(
            activity,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        return if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            foreground
        } else {
            val background = ActivityCompat.checkSelfPermission(
                activity,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
            (background && foreground)
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun showSnackBar(view: View, activity: AppCompatActivity) {
        val snackbar =
            Snackbar.make(view, R.string.location_snack_bar, Snackbar.LENGTH_INDEFINITE)
        snackbar.view.findViewById<TextView>(com.google.android.material.R.id.snackbar_text).apply {
            maxLines = 8
        }
        snackbar.setAction(R.string.open_settings) {
            Intent(
                Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                Uri.parse("package:${activity.packageName}")
            ).apply {
                addCategory(Intent.CATEGORY_DEFAULT)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                activity.startActivity(this)
            }
        }
        snackbar.show()
    }

    fun onRequestPermissionsResult(
        requestCode: Int,
        grantResults: IntArray
    ) {
        if (requestCode == Constants.LOCATION_PERM_CODE && grantResults.isNotEmpty()) {
            var perms = true
            for (res in grantResults) {
                if (res != PackageManager.PERMISSION_GRANTED) {
                    perms = false
                }
            }
            if (perms && LocationPermission::successCallback.isInitialized) {
                successCallback()
            }
        }
    }

}