package com.hema.taqsawy.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.amulyakhare.textdrawable.util.ColorGenerator
import com.hema.taqsawy.R
import com.hema.taqsawy.data.db.pojo.favoritePlacesModel.FavoriteModel
import com.hema.taqsawy.ui.favorite.FavoriteViewModel
import kotlinx.android.synthetic.main.list_item_favorite.view.*


class FavoriteAdapter(
    private val items: List<FavoriteModel>,
    favoriteViewModel: FavoriteViewModel

) : RecyclerView.Adapter<FavoriteAdapter.ViewHolder>() {

    val favViewModel: FavoriteViewModel = favoriteViewModel

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.list_item_favorite, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val generator = ColorGenerator.MATERIAL

        // generate random color
        val color = generator.randomColor
        holder.cvFavorite.setCardBackgroundColor(color)
        holder.placeText.text = items[position].place ?: "non"
        holder.latText.text = items[position].lat
        holder.lngText.text = items[position].lng
        holder.delete.setOnClickListener(View.OnClickListener {
            favViewModel.deleteItem(items[position].lat, items[position].lng)
//            notifyDataSetChanged()
            notifyItemRemoved(position)
        })

        //holder.iconTemp.setAnimation(R.raw.broken_clouds)

    }

    override fun getItemCount(): Int {
        return items.size

        Log.d("itemSizeeeeeeeeeeeee", items.size.toString())
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        var cvFavorite: CardView = itemView.cvFavorite
        var placeText: TextView = itemView.placeText
        var latText: TextView = itemView.latText
        var lngText: TextView = itemView.lngText
        var place: LottieAnimationView = itemView.place
        var delete: LottieAnimationView = itemView.delete


        init {
            itemView.setOnClickListener(this)

        }

        override fun onClick(v: View?) {
            val pos = adapterPosition
            val latClick = items.get(pos).lat
            val lngClick = items.get(pos).lng
            favViewModel.onClick("$latClick", "$lngClick")
        }


    }


}