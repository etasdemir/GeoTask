package com.elacqua.geotask.ui

import android.app.Application
import com.elacqua.geotask.BuildConfig
import com.elacqua.geotask.utils.Constants
import com.huawei.hms.maps.MapsInitializer
import timber.log.Timber

class GeoTaskApp : Application() {
    override fun onCreate() {
        super.onCreate()

        MapsInitializer.setApiKey(Constants.API_KEY)
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}