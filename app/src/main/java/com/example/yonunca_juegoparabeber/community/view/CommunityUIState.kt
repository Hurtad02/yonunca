package com.example.yonunca_juegoparabeber.community.view

import com.example.yonunca_juegoparabeber.base.model.Phrase

data class CommunityUIState(
    val phrases: List<Phrase>,
    var isSortedByPopularity: Boolean
)
