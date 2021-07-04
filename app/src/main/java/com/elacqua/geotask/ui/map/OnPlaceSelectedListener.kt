package com.elacqua.geotask.ui.map

import com.elacqua.geotask.data.model.Place

interface OnPlaceSelectedListener {
    fun onDetailClicked(place: Place)
    fun onLocationClicked(place: Place)
}