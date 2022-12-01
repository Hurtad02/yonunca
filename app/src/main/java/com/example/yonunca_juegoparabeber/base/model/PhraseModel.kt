package com.example.yonunca_juegoparabeber.base.model

import com.example.yonunca_juegoparabeber.online.model.Room
import com.example.yonunca_juegoparabeber.utils.HOT_COLLECTION
import com.example.yonunca_juegoparabeber.utils.NAUGHTY_COLLECTION
import com.example.yonunca_juegoparabeber.utils.ROOMS_COLLECTION
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import kotlin.random.Random

class PhraseModel {

    private val db = Firebase.firestore

    suspend fun getHotPhrases(): List<Phrase> {
        val phrases = mutableListOf<Phrase>()

        val result = getHotQuery(true).get().await()
        for (document in result.documents) {
            phrases.add(Phrase(document, HOT_COLLECTION))
        }
        return phrases
    }

    private fun getHotQuery(approved: Boolean): Query =
        db.collection(HOT_COLLECTION)
            .whereEqualTo("aprobado", approved)

    suspend fun getNaughtyPhrases(): List<Phrase> {
        val phrases = mutableListOf<Phrase>()

        val result = getNaughtyQuery(true).get().await()
        for (document in result.documents) {
            phrases.add(Phrase(document, NAUGHTY_COLLECTION))
        }
        return phrases
    }

    private fun getNaughtyQuery(approved: Boolean): Query =
        db.collection(NAUGHTY_COLLECTION)
            .whereEqualTo("aprobado", approved)


    suspend fun getCommunityPhrases(likes: List<Liked>): List<Phrase> {
        val phrases = mutableListOf<Phrase>()
//        Adding hot phrases
        val hotDocuments = getHotQuery(false)
            .whereEqualTo("comunidad", true)
            .get().await().documents
        hotDocuments.forEach {
            phrases.add(Phrase(it, HOT_COLLECTION))
        }
//        Adding naughty phrases
        val naughtyDocuments = getNaughtyQuery(false)
            .whereEqualTo("comunidad", true)
            .get().await().documents
        naughtyDocuments.forEach {
            phrases.add(Phrase(it, NAUGHTY_COLLECTION))
        }
        return getPhrasesWithLikes(phrases, likes)
    }

    private fun getPhrasesWithLikes(phrases: List<Phrase>, likes: List<Liked>): List<Phrase> {
        val list = mutableListOf<Phrase>()
        phrases.forEach { phrase ->
            if (likes.any { it.id == phrase.id }){
                phrase.isLiked = true
            }
            list.add(phrase)
        }
        return list
    }

    fun putLikes(phrase: Phrase) {
        val map = mapOf(
            "likes" to phrase.likes
        )
        db.collection(phrase.collection)
            .document(phrase.id)
            .update(map)
    }

    fun createPhrase(phrase: Phrase, onSuccess: () -> Unit, onFailure: () -> Unit) {
        val phraseMap = hashMapOf(
            "aprobado" to false,
            "comunidad" to true,
            "likes" to 0,
            "mensaje" to phrase.mensaje
        )

        db.collection(phrase.collection)
            .add(phraseMap)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onFailure() }
    }

    suspend fun getRandomPhrase(): Phrase {
        val type = Random.nextInt(0, 1)
        val result = mutableListOf<Phrase>()
        if (type == 0) {
            result.addAll(getNaughtyPhrases())
        } else {
            result.addAll(getHotPhrases())
        }
        return result.random()
    }

}