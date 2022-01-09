package com.android.app.feature.nearbuy

import retrofit2.Call
import retrofit2.http.GET

internal interface NearByApi {
    companion object {
        private const val NEAR_BY_PLACES = "/v2/places/"
    }

    @GET(NEAR_BY_PLACES)
    fun nearByPlaces(): Call<PlaceResponse>
}
