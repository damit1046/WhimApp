package com.android.app.feature.nearbuy


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.android.app.whimapp.R
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker


class PlaceInfoAdapter(val context : Context) : GoogleMap.InfoWindowAdapter {

    private var v = LayoutInflater.from(context).inflate(R.layout.item_info_window, null)

    override fun getInfoContents(p0: Marker): View? {
        val titleView = v?.findViewById<TextView>(R.id.tvTitle)
        val bodyView = v?.findViewById<TextView>(R.id.tvDescription)
        titleView?.text = p0.title
        bodyView?.text = p0.snippet
        return v
    }

    override fun getInfoWindow(p0: Marker): View? {
        return null;
    }
}