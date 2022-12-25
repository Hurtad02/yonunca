package com.example.yonunca_juegoparabeber.online.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.yonunca_juegoparabeber.base.model.RoomModel
import com.example.yonunca_juegoparabeber.online.model.Room
import com.example.yonunca_juegoparabeber.online.view.CreateGameUIState
import com.example.yonunca_juegoparabeber.utils.firebase.FirebaseManager
import kotlinx.coroutines.launch
import java.util.*

class CreateGameViewModel: ViewModel() {

    private val model = RoomModel()
    private val firebaseManager = FirebaseManager()

    private var uiState: MutableLiveData<CreateGameUIState> =
        MutableLiveData<CreateGameUIState>().also {
            it.value = CreateGameUIState()
        }

    fun getUIState(): MutableLiveData<CreateGameUIState>{
        return uiState
    }

    fun createRoom(name: String, code: String) {
        viewModelScope.launch {
            uiState.postValue(uiState.value?.copy(isLoading = true))
            val roomToCreate = Room(
                id = "0",
                name = name,
                phrase = "",
                turn = 0.0,
                date = Date(),
                players = listOf(firebaseManager.getUserEmail()),
                code = code
            )
            val createdRoom = model.createRoom(roomToCreate)
            if (createdRoom != null) {
                uiState.postValue(uiState.value?.copy(createdRoom = createdRoom, isLoading = false))
            } else {
                uiState.postValue(uiState.value?.copy(errorMessage = "El código de partida ya existe", isLoading = false))
            }
        }
    }

    fun clearRoom() {
        uiState.postValue(uiState.value?.copy(createdRoom = null))
    }

    fun clearError() {
        uiState.postValue(uiState.value?.copy(errorMessage = null))
    }
}