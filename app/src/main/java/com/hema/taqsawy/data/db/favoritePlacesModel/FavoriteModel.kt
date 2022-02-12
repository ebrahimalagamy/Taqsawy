package com.hema.taqsawy.data.db.favoritePlacesModel

import androidx.room.Entity

@Entity(primaryKeys = ["lat", "lng"])
data class FavoriteModel(
    var place: String? = null,
    var lat: String,
    var lng: String
)





