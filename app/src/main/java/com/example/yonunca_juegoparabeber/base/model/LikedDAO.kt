package com.example.yonunca_juegoparabeber.base.model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface LikedDAO {
    @Query("SELECT * FROM liked")
    fun getAll(): List<Liked>

    @Insert
    fun insertAll(vararg likes: Liked)
}