package com.elacqua.geotask.data.model

import com.google.gson.annotations.SerializedName

data class Polyline(
    @SerializedName("geometry")
    val geometry: String = ""
)