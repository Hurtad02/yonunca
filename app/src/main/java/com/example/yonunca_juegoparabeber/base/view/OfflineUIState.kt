package com.example.yonunca_juegoparabeber.base.view

import com.example.yonunca_juegoparabeber.base.model.Phrase

data class OfflineUIState(
    val phrase: Phrase,
    val shouldEnableSpeech: Boolean
)
