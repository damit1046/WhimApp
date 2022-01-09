package com.android.app.feature.nearbuy

import retrofit2.Call
import retrofit2.Retrofit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NearByService @Inject constructor(retrofit: Retrofit) : NearByApi {
    private val nearbyApi by lazy { retrofit.create(NearByApi::class.java) }
    override fun nearByPlaces(): Call<PlaceResponse>  = nearbyApi.nearByPlaces()
}
