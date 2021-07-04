package com.elacqua.geotask.data.remote.dao

import com.elacqua.geotask.data.model.Optimization
import com.elacqua.geotask.data.model.OptimizationBody
import retrofit2.http.Body
import retrofit2.http.POST

interface OptimizationService {

    @POST("/optimization")
    suspend fun optimizeEndPoints(@Body optimizationBody: OptimizationBody): Optimization
}