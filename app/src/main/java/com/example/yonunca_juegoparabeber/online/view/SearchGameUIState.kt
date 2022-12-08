package com.example.yonunca_juegoparabeber.online.view

import com.example.yonunca_juegoparabeber.online.model.Room

data class SearchGameUIState(
    val roomsList: List<Room>,
    val isLoading: Boolean = false,
    val roomError: Boolean = false,
    val privateRoom: Room? = null
)