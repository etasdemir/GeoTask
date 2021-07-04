package com.elacqua.geotask.ui.savedplace

import com.elacqua.geotask.data.model.Place

interface OnPlaceDeleteListener {
    fun deletePlace(place: Place)
}