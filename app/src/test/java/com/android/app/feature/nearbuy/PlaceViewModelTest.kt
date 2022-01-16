package com.android.app.feature.nearbuy

import com.android.app.AndroidTest
import com.android.app.core.extension.empty
import com.android.app.core.functional.Either
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.amshove.kluent.shouldEqualTo
import java.util.*

class PlaceViewModelTest : AndroidTest() {

    private lateinit var placeViewModel: PlaceViewModel

    @MockK
    private lateinit var getPlaces: GetPlaces

    @Before
    fun setUp() {
        placeViewModel = PlaceViewModel(getPlaces)
    }

    @Test
    fun `loading places should update live data`() {
        val placeList = listOf(Place(0, "IronMan Shop","",WhimLocation(0.0,0.0, String.empty()),String.empty(), Collections.emptyList()), Place(1, "Batman Shop","",WhimLocation(0.0,0.0, String.empty()),String.empty(), Collections.emptyList()))
        coEvery { getPlaces.run(any()) } returns Either.Right(placeList)

        placeViewModel.places.observeForever {
            it!!.size shouldEqualTo 2
            it[0].id shouldEqualTo 0
            it[0].name shouldEqualTo "IronMan Shop"
            it[1].id shouldEqualTo 1
            it[1].name shouldEqualTo "Batman Shop"
        }

        runBlocking { placeViewModel.loadPlaces() }
    }
}