package com.example.yonunca_juegoparabeber.base.model

import com.google.firebase.firestore.DocumentSnapshot

data class Phrase(
    val id: String,
    val mensaje: String,
    var likes: Long,
    val collection: String,
    var isLiked: Boolean = false
){
    constructor(document: DocumentSnapshot, collection: String) : this(
        id = document.id,
        mensaje = document.getString("mensaje") ?: "",
        likes = document.getLong("likes") ?: 0L,
        collection = collection
    )
}
