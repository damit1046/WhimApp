package com.android.app.feature.nearbuy

import com.google.gson.annotations.SerializedName

data class PlaceResponse (@SerializedName("data")val data : List<PlaceEntity>)