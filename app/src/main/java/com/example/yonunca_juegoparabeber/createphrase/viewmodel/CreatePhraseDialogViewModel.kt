package com.example.yonunca_juegoparabeber.createphrase.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.yonunca_juegoparabeber.base.model.Phrase
import com.example.yonunca_juegoparabeber.base.model.PhraseModel
import com.example.yonunca_juegoparabeber.base.model.RoomModel
import com.example.yonunca_juegoparabeber.createphrase.view.CreatePhraseDialogUIState
import com.example.yonunca_juegoparabeber.online.model.Room
import java.util.*

class CreatePhraseDialogViewModel: ViewModel() {
    private val model = PhraseModel()
    private var room: Room? = null
    private val roomModel = RoomModel()

    private val uiState: MutableLiveData<CreatePhraseDialogUIState> =
        MutableLiveData<CreatePhraseDialogUIState>().also {
            it.value = CreatePhraseDialogUIState(isLoading = false, shouldDismiss = false)
        }

    fun getUiState(): LiveData<CreatePhraseDialogUIState> = uiState

    fun createPhrase(phraseStr: String, collection: String) {
        val phrase = Phrase("0", phraseStr, 0, collection)
        model.createPhrase(phrase, {
            uiState.postValue(uiState.value?.copy(shouldDismiss = true))
        },
            {
                uiState.postValue(uiState.value?.copy(isLoading = false))
            }
        )
    }

    fun updateRoomPhrase(phrase: String) {
        if (room != null){
            room?.phrase = phrase
            room?.changeTurn()
            roomModel.updateRoom(room!!)
            uiState.postValue(uiState.value?.copy(shouldDismiss = true))
        }
    }

    fun setRoom(room: Room) {
        this.room = room
    }

}