package com.example.yonunca_juegoparabeber.online.view

data class SignUpUIState(
    val result: Boolean = false,
    val isLoading: Boolean = false,
    val signUpError: String? = null
)