package com.hema.taqsawy.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hema.taqsawy.data.db.favoritePlacesModel.FavoriteModel
import com.hema.taqsawy.databinding.ItemFavoriteBinding
import com.hema.taqsawy.ui.favorite.FavoriteViewModel


class FavoriteAdapter(
    private val items: List<FavoriteModel>, favoriteViewModel: FavoriteViewModel

) : RecyclerView.Adapter<FavoriteAdapter.ViewHolder>() {

    private val favViewModel: FavoriteViewModel = favoriteViewModel
    private lateinit var mListener: OnItemClickListener
    private lateinit var binding: ItemFavoriteBinding

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        mListener = listener
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = ItemFavoriteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding, mListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.placeText.text = items[position].place ?: "non"
        holder.binding.delete.setOnClickListener {
            favViewModel.deleteItem(items[position].lat, items[position].lng)
            notifyItemRemoved(position)
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class ViewHolder(val binding: ItemFavoriteBinding, listener: OnItemClickListener) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            itemView.setOnClickListener {
                listener.onItemClick(adapterPosition)
                val latClick = items[adapterPosition].lat
                val lngClick = items[adapterPosition].lng
                favViewModel.onClick("$latClick", "$lngClick")
            }
        }
    }
}