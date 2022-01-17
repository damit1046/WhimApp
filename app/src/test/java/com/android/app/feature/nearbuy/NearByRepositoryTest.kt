package com.android.app.feature.nearbuy

import com.android.app.UnitTest
import com.android.app.core.exception.Failure
import com.android.app.core.extension.empty
import com.android.app.core.functional.Either
import com.android.app.core.platform.NetworkHandler
import io.mockk.Called
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import org.amshove.kluent.shouldBeInstanceOf
import org.amshove.kluent.shouldEqual
import org.junit.Before
import org.junit.Test
import retrofit2.Call
import retrofit2.Response
import java.util.*

class NearByRepositoryTest : UnitTest() {

    private lateinit var networkRepository: NearByRepository.Network

    @MockK
    private lateinit var networkHandler: NetworkHandler
    @MockK
    private lateinit var service: NearByService

    @MockK
    private lateinit var placeCall: Call<PlaceResponse>
    @MockK
    private lateinit var placeResponse: Response<PlaceResponse>



    @Before
    fun setUp() {
        networkRepository = NearByRepository.Network(networkHandler, service)
    }

    @Test
    fun `should return empty list by default`() {
        every { networkHandler.isNetworkAvailable() } returns true
        every { placeResponse.body() } returns null
        every { placeResponse.isSuccessful } returns true
        every { placeCall.execute() } returns placeResponse
        every { service.nearByPlaces() } returns placeCall

        val places = networkRepository.nearByPlaces()

        places shouldEqual Either.Right(emptyList<Place>())
        verify(exactly = 1) { service.nearByPlaces() }
    }

    @Test
    fun `should get place list from service`() {
        every { networkHandler.isNetworkAvailable() } returns true
        every { placeResponse.body() } returns PlaceResponse(listOf(PlaceEntity(0, Name("IronMan Shop"),Description(""),"", WhimLocationEntity(0.0,0.0, Address("","","","")))))
        every { placeResponse.isSuccessful } returns true
        every { placeCall.execute() } returns placeResponse
        every { service.nearByPlaces() } returns placeCall

        val places = networkRepository.nearByPlaces()

        places shouldEqual Either.Right(listOf(Place(0, "IronMan Shop","",WhimLocation(0.0,0.0, Address("","","","").toString()),String.empty(), Collections.emptyList())))
        verify(exactly = 1) { service.nearByPlaces()}
    }

    @Test
    fun `places service should return network failure when no connection`() {
        every { networkHandler.isNetworkAvailable() } returns false
        val places = networkRepository.nearByPlaces()
        places shouldBeInstanceOf Either::class.java
        places.isLeft shouldEqual true
        places.fold({ failure -> failure shouldBeInstanceOf Failure.NetworkConnection::class.java }, {})
        verify { service wasNot Called }
    }

    @Test
    fun `places service should return server error if no successful response`() {
        every { networkHandler.isNetworkAvailable() } returns true
        every { placeResponse.isSuccessful } returns false
        every { placeCall.execute() } returns placeResponse
        every { service.nearByPlaces() } returns placeCall

        val places = networkRepository.nearByPlaces()

        places shouldBeInstanceOf Either::class.java
        places.isLeft shouldEqual true
        places.fold({ failure -> failure shouldBeInstanceOf Failure.ServerError::class.java }, {})
    }

    @Test
    fun `places request should catch exceptions`() {
        every { networkHandler.isNetworkAvailable() } returns true
        every { placeCall.execute() } returns placeResponse
        every { service.nearByPlaces() } returns placeCall

        val places = networkRepository.nearByPlaces()

        places shouldBeInstanceOf Either::class.java
        places.isLeft shouldEqual true
        places.fold({ failure -> failure shouldBeInstanceOf Failure.ServerError::class.java }, {})
    }

}