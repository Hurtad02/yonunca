package com.example.yonunca_juegoparabeber.online.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.yonunca_juegoparabeber.base.model.RoomModel
import com.example.yonunca_juegoparabeber.online.model.Room
import com.example.yonunca_juegoparabeber.online.view.OnlineGameUIState
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
            val rooms = roomModel.getRooms()
            uiState.postValue(uiState.value?.copy(roomsList = rooms, isLoading = false))
        }
    }
}