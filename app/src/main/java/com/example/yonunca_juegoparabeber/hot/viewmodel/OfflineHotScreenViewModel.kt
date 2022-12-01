package com.example.yonunca_juegoparabeber.hot.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.yonunca_juegoparabeber.base.model.Phrase
import com.example.yonunca_juegoparabeber.base.model.PhraseModel
import com.example.yonunca_juegoparabeber.base.view.OfflineUIState
import com.example.yonunca_juegoparabeber.utils.HOT_COLLECTION
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class OfflineHotScreenViewModel: ViewModel() {

    private val model: PhraseModel = PhraseModel()
    private var phrases: List<Phrase> = listOf()
    private val previous: MutableList<Phrase> = mutableListOf()
    private val defaultPhrase = Phrase(
        "0",
        mensaje =  "Obteniendo datos...",
        likes = 0,
        collection = HOT_COLLECTION
    )

    private val uiState: MutableLiveData<OfflineUIState> =
        MutableLiveData<OfflineUIState>().also {
            it.value = OfflineUIState(defaultPhrase, false)
        }

    fun getUiState(): LiveData<OfflineUIState> = uiState

    fun getPhrases(){
        viewModelScope.launch(Dispatchers.IO) {
            val fetchedPhrases = model.getHotPhrases()
            phrases = fetchedPhrases
            getNextWord()
        }
    }

    private fun getRandomPhrase(): Phrase {
        return if (phrases.isEmpty())
            defaultPhrase
        else phrases.random()
    }

    fun getNextWord(){
        if (uiState.value!!.phrase != defaultPhrase) {
            addToPrevious(uiState.value!!.phrase)
        }
        uiState.postValue(
            uiState.value?.copy(phrase = getRandomPhrase(), shouldEnableSpeech = true)
        )
    }

    private fun addToPrevious(phrase: Phrase) {
        previous.add(phrase)
    }

    fun getPreviousWord() {
        if (previous.size >= 1){
            val phrase = removeFromPrevious()
            uiState.postValue(
                uiState.value?.copy(phrase = phrase)
            )
        }
    }

    private fun removeFromPrevious(): Phrase {
        return previous.removeLast()
    }

}