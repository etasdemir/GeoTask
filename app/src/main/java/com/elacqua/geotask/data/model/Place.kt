package com.elacqua.geotask.data.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity
@Parcelize
data class Place(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    val latitude: Double,
    val longitude: Double,
    val title: String = "",
    val description: String = "",
    val date: String = "",
) : Parcelable