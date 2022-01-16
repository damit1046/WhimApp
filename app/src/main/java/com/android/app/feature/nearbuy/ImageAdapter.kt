package com.android.app.feature.nearbuy

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.app.core.extension.inflate
import com.android.app.core.extension.loadFromUrl
import com.android.app.whimapp.R
import kotlinx.android.synthetic.main.row_place_image.view.*

class ImageAdapter (private val collection: List<String>): RecyclerView.Adapter<ImageAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(parent.inflate(R.layout.row_place_image))

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) =
        viewHolder.bind(collection[position])

    override fun getItemCount() = collection.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(url : String) {
            itemView.placePoster.loadFromUrl(url)
        }
    }
}
