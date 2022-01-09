package com.android.app.feature.nearbuy

import com.android.app.feature.location.CurrentLocation

interface CurrentLocationProvider {
    fun currentLocationProvider() : CurrentLocation
}
