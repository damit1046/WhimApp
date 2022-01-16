package com.android.app.feature.nearbuy

import android.os.Bundle
import android.view.View
import androidx.annotation.StringRes
import androidx.fragment.app.viewModels
import com.android.app.core.exception.Failure
import com.android.app.core.extension.failure
import com.android.app.core.extension.observe
import com.android.app.core.navigation.Navigator
import com.android.app.core.platform.BaseFragment
import com.android.app.feature.location.CurrentLocation
import com.android.app.feature.placedetail.PlaceDetailBottomSheetDialog
import com.android.app.whimapp.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class NearByFragment : BaseFragment(), OnMapReadyCallback, CurrentLocation.CurrentLocationUpdate {
    @Inject
    lateinit var navigator: Navigator

    private val placeViewModel: PlaceViewModel by viewModels()

    private lateinit var map : GoogleMap
    private var initMap = false
    private var currentLocation : LatLng? = null


    override fun layoutId() = R.layout.fragment_places



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        with(placeViewModel) {
            observe(places, ::renderPlacesList)
            failure(failure, ::handleFailure)
        }
    }

    private fun renderPlacesList(places: List<PlaceView>?) {
        if(initMap){
            places?.forEachIndexed { index, place ->
                val location  = LatLng((place.location.lat), place.location.long)

                map.addMarker(
                    MarkerOptions()
                        .position(location)
                        .title(place.name)
                        .snippet("\n${place.description}\n\n${place.location.address}")
                ).apply {
                    this?.tag = index
                }
            }
            updateCurrentLocation()
        }
        hideProgress()
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment: SupportMapFragment? = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)
        loadPlaces()
        fetchCurrentLocation()
    }

    private fun fetchCurrentLocation() {
        val currentActivity = activity
        if (currentActivity is CurrentLocationProvider){
            currentActivity.currentLocationProvider().getDeviceLocation(this)
        }
    }


    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        initMap = true
        renderPlacesList(placeViewModel.places.value)
        map.setOnMarkerClickListener {
           if(it.tag is Int){
               val position = it.tag as Int
               placeViewModel.places.value?.get(position)?.let { placeView ->
                   PlaceDetailBottomSheetDialog.show(requireContext(),
                       placeView
                   )
               }
           }
            true
        }
    }

    private fun handleFailure(failure: Failure?) {
        when (failure) {
            is Failure.NetworkConnection -> renderFailure(R.string.failure_network_connection)
            is Failure.ServerError -> renderFailure(R.string.failure_server_error)
            is PlaceFailure.ListNotAvailable -> renderFailure(R.string.failure_place_list_unavailable)
            else -> renderFailure(R.string.failure_server_error)
        }
    }

    private fun loadPlaces() {
        showProgress()
        placeViewModel.loadPlaces()
    }

    private fun renderFailure(@StringRes message: Int) {
        hideProgress()
        notifyWithAction(message, R.string.action_refresh, ::loadPlaces)
    }

    override fun currentLocationUpdate(location: LatLng) {
        currentLocation = location
        updateCurrentLocation()
    }

    private fun updateCurrentLocation() {
        if(initMap && currentLocation != null){
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation!!,CurrentLocation.DEFAULT_ZOOM.toFloat()))
        }
    }

}