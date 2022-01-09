package com.android.app.feature.nearbuy

import com.android.app.core.extension.empty
import com.google.gson.annotations.SerializedName

data class PlaceEntity (@SerializedName ("id")val id : Int,
                        @SerializedName ("name")val name: Name? = null,
                        @SerializedName ("description")val description: Description,
                        @SerializedName ("location")val location: WhimLocationEntity){
    fun toPlace() = Place(id,name?.en?:String.empty(),description.body?: String.empty(),location.toLocation())
}

data class Name(@SerializedName ("en") val en : String? = null)

data class Description(@SerializedName("body") val body : String? = null)

data class WhimLocationEntity(@SerializedName ("lat") val lat : Double,
                              @SerializedName ("lon") val lon : Double,
                              @SerializedName ("address") val address: Address){
    fun toLocation() = WhimLocation(lat,lon,address.toString())
}

data class Address(@SerializedName("street_address") val streetAddress : String? = null,
                   @SerializedName("postal_code") val postalCode : String? = null,
                   @SerializedName("locality") val locality : String? = null,
                   @SerializedName("neighbourhood") val neighbourhood : String? = null){
    override fun toString(): String = "$streetAddress, $neighbourhood,\n$locality\n$postalCode"
}
