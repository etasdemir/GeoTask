package com.elacqua.geotask.data.remote

import com.elacqua.geotask.data.model.*
import com.elacqua.geotask.utils.UIState
import com.huawei.hms.maps.model.LatLng
import timber.log.Timber

object RemoteRepository {

    suspend fun getRoutes(coordinates: Coordinates): Direction? {
        try {
            return NetworkHandler.getDirectionService().getRoute(coordinates)
        } catch (e: Exception) {
            Timber.e(e.stackTraceToString())
            UIState.state.postValue(UIState.Error("Failed to find route"))
        }
        return null
    }

    suspend fun optimizeEndPoints(
        jobs: ArrayList<Job>,
        location: LatLng
    ): Optimization? {
        try {
            val startEndPoint = listOf(location.longitude, location.latitude)
            val vehicle = Vehicle(startEndPoint, startEndPoint)
            val optimizationBody = OptimizationBody(jobs, listOf(vehicle))
            return NetworkHandler.getOptimizationService().optimizeEndPoints(optimizationBody)
        } catch (e: Exception) {
            Timber.e(e.stackTraceToString())
            UIState.state.postValue(
                UIState.Error("Unfound route(s) from location [${location.latitude}, ${location.longitude}]")
            )
        }
        return null
    }
}