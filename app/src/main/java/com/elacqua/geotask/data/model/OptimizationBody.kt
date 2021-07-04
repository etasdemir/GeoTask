package com.elacqua.geotask.data.model

import com.google.gson.annotations.SerializedName

data class OptimizationBody(
    @SerializedName("jobs")
    val jobs: List<Job> = listOf(),
    @SerializedName("vehicles")
    val vehicles: List<Vehicle> = listOf()
)