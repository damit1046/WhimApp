package com.android.app.feature.placedetail

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Toast
import com.android.app.core.extension.makeHyperLink
import com.android.app.feature.nearbuy.ImageAdapter
import com.android.app.feature.nearbuy.PlaceView
import com.android.app.whimapp.R
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.bottom_sheet_place_detail_dialog.*


class PlaceDetailBottomSheetDialog(context: Context) : BottomSheetDialog(context) {

    private lateinit var placeView: PlaceView


    companion object{
        fun show(context : Context,placeView : PlaceView){
            val bottomSheet = PlaceDetailBottomSheetDialog(context)
            bottomSheet.placeView = placeView
            bottomSheet.show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.bottom_sheet_place_detail_dialog)
        titleView.text = placeView.name
        imagesView.visibility = if(placeView.images.isEmpty()) GONE else VISIBLE
        descriptionView.text = placeView.description
        imagesView.adapter= ImageAdapter(placeView.images)
        infoView.makeHyperLink(placeView.infoUrl)
        directionView.setOnClickListener {
            val intent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("geo:0,0?q=${placeView.location.lat},${placeView.location.long}(${placeView.name})")
            )
            try {
                context.startActivity(intent)    
            }catch (e : Exception){
                Toast.makeText(context,context.getString(R.string.err_no_app_found),Toast.LENGTH_LONG).show()
            }
            
        }
    }

}