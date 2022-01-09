package com.android.app.feature.nearbuy


import com.android.app.core.interactor.UseCase
import javax.inject.Inject

class GetPlaces  @Inject constructor(private val nearByRepository: NearByRepository) : UseCase<List<Place>, UseCase.None>() {

    override suspend fun run(params: None) = nearByRepository.nearByPlaces()
}
