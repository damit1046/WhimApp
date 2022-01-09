package com.android.app.feature.nearbuy


import com.android.app.core.exception.Failure
import com.android.app.core.functional.Either
import com.android.app.core.platform.NetworkHandler
import retrofit2.Call
import javax.inject.Inject

interface NearByRepository {
    fun nearByPlaces(): Either<Failure, List<Place>>

    class Network @Inject constructor(private val networkHandler: NetworkHandler, private val service: NearByService) : NearByRepository {


        override fun nearByPlaces(): Either<Failure, List<Place>> {
            return when (networkHandler.isNetworkAvailable()) {
                true -> request(
                    service.nearByPlaces(),
                    { it.data.map { placeEntity -> placeEntity.toPlace() } },
                    PlaceResponse(emptyList())
                )
                false -> Either.Left(Failure.NetworkConnection)
            }
        }

        private fun <T, R> request(call: Call<T>, transform: (T) -> R, default: T): Either<Failure, R> {
            return try {
                val response = call.execute()
                when (response.isSuccessful) {
                    true -> Either.Right(transform((response.body() ?: default)))
                    false -> Either.Left(Failure.ServerError)
                }
            } catch (exception: Throwable) {
                Either.Left(Failure.ServerError)
            }
        }

    }
}
