package com.android.app.feature.nearbuy

import android.os.Parcel
import android.os.Parcelable.PARCELABLE_WRITE_RETURN_VALUE
import com.android.app.core.extension.empty
import com.android.app.core.platform.KParcelable
import com.android.app.core.platform.parcelableCreator


data class PlaceView(val id : Int,val name : String,val description : String,val location : LocationView) : KParcelable {
    companion object {
        @JvmField
        val CREATOR = parcelableCreator(::PlaceView)
    }

    constructor(parcel: Parcel) : this(parcel.readInt(),parcel.readString()?:String.empty() ,parcel.readString()?:String.empty(),parcel.readParcelable<LocationView>(LocationView::class.java.classLoader)!!)

    override fun writeToParcel(dest: Parcel, flags: Int) {
        with(dest) {
            writeInt(id)
            writeString(name)
            writeString(description)
            writeParcelable(location,PARCELABLE_WRITE_RETURN_VALUE)
        }
    }
}

data class LocationView(val lat : Double,val long : Double, val address : String) : KParcelable{

    companion object {
        @JvmField
        val CREATOR = parcelableCreator(::LocationView)
    }

    constructor(parcel: Parcel) : this(parcel.readDouble(), parcel.readDouble(),parcel.readString()?:String.empty())

    override fun writeToParcel(dest: Parcel, flags: Int) {
        with(dest) {
            writeDouble(lat)
            writeDouble(long)
            writeString(address)
        }
    }
}
