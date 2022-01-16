package com.android.app.feature.nearbuy

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.android.app.core.interactor.UseCase
import com.android.app.core.platform.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PlaceViewModel @Inject constructor(private val getPlaces : GetPlaces) : BaseViewModel() {

    private val _places: MutableLiveData<List<PlaceView>> = MutableLiveData()
    val places: LiveData<List<PlaceView>> = _places

    fun loadPlaces() =
        getPlaces(UseCase.None(), viewModelScope) { it.fold(::handleFailure, ::handlePlaceList) }

    private fun handlePlaceList(places: List<Place>) {
        _places.value = places.map { PlaceView(it.id, it.name,it.description,LocationView(it.location.lat,it.location.lon,it.location.address),it.infoUrl,it.images) }
    }
}
