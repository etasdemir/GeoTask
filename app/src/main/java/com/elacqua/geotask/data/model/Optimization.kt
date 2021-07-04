package com.elacqua.geotask.data.model

import com.google.gson.annotations.SerializedName

data class Optimization(
    @SerializedName("routes")
    val routes: List<Route> = listOf(),
)