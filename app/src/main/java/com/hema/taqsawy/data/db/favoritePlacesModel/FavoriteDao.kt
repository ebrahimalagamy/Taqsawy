package com.hema.taqsawy.data.db.favoritePlacesModel

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface FavoriteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFavoritePlaces(favoriteModel: FavoriteModel?)

    @Query("SELECT * From FavoriteModel")
    fun getFavoritePlaces(): LiveData<List<FavoriteModel>>

    @Query("Delete from FavoriteModel WHERE lat=:lat AND lng=:lng  ")
    suspend fun deleteAll(lat: String, lng: String)
}