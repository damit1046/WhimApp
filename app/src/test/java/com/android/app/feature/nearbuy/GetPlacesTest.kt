package com.android.app.feature.nearbuy


import com.android.app.UnitTest
import com.android.app.core.functional.Either
import com.android.app.core.interactor.UseCase
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class GetPlacesTest : UnitTest() {

    private lateinit var getPlaces: GetPlaces

    @MockK private lateinit var nearByRepository: NearByRepository

    @Before fun setUp() {
        getPlaces = GetPlaces(nearByRepository)
        every { nearByRepository.nearByPlaces() } returns Either.Right(listOf(Place.empty))
    }

    @Test fun `should get data from repository`() {
        runBlocking { getPlaces.run(UseCase.None()) }

        verify(exactly = 1) { nearByRepository.nearByPlaces() }
    }
}
