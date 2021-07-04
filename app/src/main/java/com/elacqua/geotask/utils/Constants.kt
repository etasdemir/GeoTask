package com.elacqua.geotask.utils

object Constants {

    const val MARKER_POS_KEY = "MARKER_POS_KEY"
    const val PLACE_BUNDLE_KEY = "PLACE_BUNDLE_KEY"
    const val MAPVIEW_BUNDLE_KEY = "MapViewBundleKey"
    const val LOCATION_PERM_CODE = 1
    const val OPEN_ROUTE_SERVICE_URL = "https://api.openrouteservice.org"
    // Api Key should not be hardcoded but for current prototype it is hardcoded
    const val API_KEY = "CgB6e3x9Bh1y7VDC11cz2u1nQazRnvJcsDZUpv7jBHHp/bwOpkt6vbAhHqqae6b0tfAEH4yKU2GwH+k8J8r7FTfs"
    const val ORS_API_KEY = "5b3ce3597851110001cf6248587efc1421f040c6b4455666b806049f"
}

enum class MapMarkerState {
    ADD_MARKER, SAVE_MARKER
}