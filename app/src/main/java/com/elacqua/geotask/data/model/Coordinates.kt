package com.elacqua.geotask.data.model

import com.google.gson.annotations.SerializedName

data class Coordinates(
    @SerializedName("coordinates")
    val coordinates: List<List<Double>>,
    @SerializedName("radiuses")
    val radius: List<Int> = MutableList(coordinates.size) { -1 }
)