package com.hema.taqsawy.data.db.pojo.favoritePlacesModel

import androidx.room.Entity

@Entity(primaryKeys = ["lat", "lng"])
data class FavoriteModel(
    var place: String? = null,
    var lat: String,
    var lng: String
)





