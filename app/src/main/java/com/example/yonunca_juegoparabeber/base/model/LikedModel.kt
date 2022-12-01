package com.example.yonunca_juegoparabeber.base.model

import com.example.yonunca_juegoparabeber.base.BaseApplication

class LikedModel {

    private val dao = AppDatabase.getDatabase(BaseApplication.getApplicationContext()).likedDAO()

    fun insertLiked(liked: Liked){
        dao.insertAll(liked)
    }

    fun getLikes(): List<Liked> = dao.getAll()
}