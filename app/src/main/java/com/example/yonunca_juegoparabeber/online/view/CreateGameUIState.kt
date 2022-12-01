package com.example.yonunca_juegoparabeber.online.view

import com.example.yonunca_juegoparabeber.online.model.Room

data class CreateGameUIState (
    var isLoading: Boolean = false,
    var createdRoom: Room? = null,
    var errorMessage: String? = null
)