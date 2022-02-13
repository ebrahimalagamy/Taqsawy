package com.hema.taqsawy.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.hema.taqsawy.R
import com.hema.taqsawy.data.db.favoritePlacesModel.FavoriteModel
import com.hema.taqsawy.ui.favorite.FavoriteViewModel
import kotlinx.android.synthetic.main.item_favorite.view.*


class FavoriteAdapter(
    private val items: List<FavoriteModel>,
    favoriteViewModel: FavoriteViewModel

) : RecyclerView.Adapter<FavoriteAdapter.ViewHolder>() {

    val favViewModel: FavoriteViewModel = favoriteViewModel

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_favorite, parent, false)
        return ViewHolder(view)
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.placeText.text = items[position].place ?: "non"
        holder.delete.setOnClickListener {
            favViewModel.deleteItem(items[position].lat, items[position].lng)
            notifyItemRemoved(position)
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        var placeText: TextView = itemView.placeText
        var delete: LottieAnimationView = itemView.delete


        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val pos = adapterPosition
            val latClick = items[pos].lat
            val lngClick = items[pos].lng
            favViewModel.onClick("$latClick", "$lngClick")
        }


    }


}