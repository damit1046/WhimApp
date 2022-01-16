package com.android.app.feature.nearbuy

import com.android.app.core.extension.empty
import java.util.*

data class Place (val id : Int,val name : String,val description : String,val location : WhimLocation,val infoUrl : String, val images : List<String>){
    companion object {
        val empty = Place(0, String.empty(),String.empty(), WhimLocation(0.0,0.0, String.empty()),String.empty(), Collections.emptyList())
    }
}

data class WhimLocation(val lat : Double,val lon : Double,val address : String)
