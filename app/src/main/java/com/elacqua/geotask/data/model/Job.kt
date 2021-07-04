package com.elacqua.geotask.data.model

import com.google.gson.annotations.SerializedName

data class Job(
    @SerializedName("id")
    val id: Int = 0,
    @SerializedName("location")
    val location: List<Double> = listOf()
)