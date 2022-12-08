package com.example.yonunca_juegoparabeber.online.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.yonunca_juegoparabeber.base.model.RoomModel
import com.example.yonunca_juegoparabeber.online.view.SearchGameUIState
import kotlinx.coroutines.launch

class SearchGameViewModel: ViewModel() {

    private val roomModel = RoomModel()

    private var uiState: MutableLiveData<SearchGameUIState> =
        MutableLiveData<SearchGameUIState>().also {
            it.value = SearchGameUIState(listOf())
        }

    fun getUIState(): MutableLiveData<SearchGameUIState>{
        return uiState
    }

    fun getRooms(){
        viewModelScope.launch {
            uiState.postValue(uiState.value?.copy(isLoading = true))
            val rooms = roomModel.getPublicRooms()
            uiState.postValue(uiState.value?.copy(roomsList = rooms, isLoading = false))
        }
    }

    fun getRoomByCode(code: String){
        viewModelScope.launch {
            val room = roomModel.getRoomByCode(code)
            if (room == null){
                uiState.postValue(uiState.value?.copy(roomError = true))
            } else {
                uiState.postValue(uiState.value?.copy(privateRoom = room))
            }
        }
    }

    fun clearError() {
        uiState.postValue(uiState.value?.copy(roomError = false))
    }

    fun clearRoom() {
        uiState.postValue(uiState.value?.copy(privateRoom = null))
    }
}