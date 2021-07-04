package com.elacqua.geotask.data.remote.dao

import com.elacqua.geotask.data.model.Coordinates
import com.elacqua.geotask.data.model.Direction
import retrofit2.http.Body
import retrofit2.http.POST

interface DirectionService {

    @POST("/v2/directions/driving-car/")
    suspend fun getRoute(@Body coordinates: Coordinates): Direction
}