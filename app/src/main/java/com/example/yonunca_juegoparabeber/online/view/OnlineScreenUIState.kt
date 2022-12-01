package com.example.yonunca_juegoparabeber.online.view

import com.google.firebase.auth.FirebaseUser

data class OnlineScreenUIState(
    val user: FirebaseUser? = null,
    val messageToShow: String? = null,
    val isOnline: Boolean = false,
    val userName: String = ""
)



