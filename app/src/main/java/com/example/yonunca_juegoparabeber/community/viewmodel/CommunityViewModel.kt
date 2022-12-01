package com.example.yonunca_juegoparabeber.community.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.yonunca_juegoparabeber.base.model.Liked
import com.example.yonunca_juegoparabeber.base.model.LikedModel
import com.example.yonunca_juegoparabeber.base.model.Phrase
import com.example.yonunca_juegoparabeber.base.model.PhraseModel
import com.example.yonunca_juegoparabeber.community.view.CommunityUIState
import com.example.yonunca_juegoparabeber.community.view.OnLikePressed
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CommunityViewModel: ViewModel(), OnLikePressed {

    private val model: PhraseModel = PhraseModel()
    private val likedModel = LikedModel()
    private val uiState: MutableLiveData<CommunityUIState> =
        MutableLiveData<CommunityUIState>().also {
            it.value = CommunityUIState(listOf(), isSortedByPopularity = false)
        }
    private lateinit var unsortedPhrases: List<Phrase>

    fun getUiState(): LiveData<CommunityUIState> = uiState

    fun getPhrases(){
        viewModelScope.launch(Dispatchers.IO) {
            val likes = likedModel.getLikes()
            val phrases = model.getCommunityPhrases(likes)
            unsortedPhrases = phrases
            uiState.postValue(
                uiState.value?.copy(phrases = phrases, isSortedByPopularity = false)
            )
        }
    }

    override fun onLikeAdded(phrase: Phrase) {
        viewModelScope.launch(Dispatchers.IO) {
            phrase.likes++
            model.putLikes(phrase)
            likedModel.insertLiked(Liked(id = phrase.id))
        }
    }

    fun shouldOrderByPopularity() {
        uiState.value?.isSortedByPopularity?.let{ isSorted ->
            if (isSorted) {
                disablePopularity()
            } else {
                orderByPopularity()
            }
        }
    }

    private fun orderByPopularity() {
        viewModelScope.launch(Dispatchers.IO) {
            val phrases = uiState.value?.phrases
            if (phrases != null) {
                val sorted = phrases.sortedByDescending { it.likes }
                uiState.postValue(uiState.value?.copy(phrases = sorted, isSortedByPopularity = true))
            }
        }
    }

    private fun disablePopularity() {
        uiState.postValue(uiState.value?.copy(phrases = unsortedPhrases, isSortedByPopularity = false))
    }

}