package com.elacqua.geotask.data.model

import com.google.gson.annotations.SerializedName

data class Step(
    @SerializedName("location")
    val location: List<Double> = listOf()
)