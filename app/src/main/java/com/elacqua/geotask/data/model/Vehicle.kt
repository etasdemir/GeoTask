package com.elacqua.geotask.data.model

import com.google.gson.annotations.SerializedName

data class Vehicle(
    @SerializedName("start")
    val start: List<Double> = listOf(),
    @SerializedName("end")
    val end: List<Double> = listOf(),
    @SerializedName("id")
    val id: Int = 1,
    @SerializedName("profile")
    val profile: String = "driving-car"
)