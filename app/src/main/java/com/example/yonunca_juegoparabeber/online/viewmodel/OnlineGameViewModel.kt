package com.example.yonunca_juegoparabeber.online.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.yonunca_juegoparabeber.base.model.PhraseModel
import com.example.yonunca_juegoparabeber.base.model.RoomModel
import com.example.yonunca_juegoparabeber.online.model.Room
import com.example.yonunca_juegoparabeber.online.view.OnlineGameUIState
import com.example.yonunca_juegoparabeber.utils.firebase.FirebaseManager
import kotlinx.coroutines.launch

class OnlineGameViewModel : ViewModel() {

    private val roomModel = RoomModel()
    private val firebaseManager = FirebaseManager()
    private val phraseModel = PhraseModel()

    private var uiState: MutableLiveData<OnlineGameUIState> =
        MutableLiveData<OnlineGameUIState>().also {
            it.value = OnlineGameUIState()
        }

    fun getUIState(): MutableLiveData<OnlineGameUIState> {
        return uiState
    }

    fun getGameData(gameRoom: Room) {
        roomModel.getRoomData(gameRoom.id, uiState)
    }

    fun joinCurrentPlayerToGameIfRequired() {
        with(uiState.value?.room){
            if (this != null && !players.contains(firebaseManager.getUserEmail())) {
                joinPlayer()
            }
        }
    }

    fun getRandomPhrase() {
        viewModelScope.launch {
            if (uiState.value?.room != null){
                val room = uiState.value?.room?.copy(phrase = phraseModel.getRandomPhrase().mensaje).also {
                    it?.changeTurn()
                }
                roomModel.updateRoom(room!!)
            }
        }
    }

    private fun joinPlayer(){
        uiState.value?.room?.apply {
            val newList = players.toMutableList()
            newList.add(firebaseManager.getUserEmail())
            players = newList
        }?.also {
            roomModel.joinPlayer(it)
        }
    }

    fun isYourTurn(): Boolean {
         uiState.value?.room?.let { room ->
             val currentPlayer = room.getCurrentPlayerIndex()
             return currentPlayer == room.turn.toInt()
        }
        return false
    }

    fun getCurrentRoom(): Room? {
        return uiState.value?.room
    }

    fun removePlayer() {
        val players = getCurrentRoom()?.players?.toMutableList()
        if (players != null){
            players.remove(firebaseManager.getUserEmail())
            val room = getCurrentRoom()?.apply {
                this.turn = getTurnAfterPlayerRemoved().toDouble()
                this.players = players.toList()
            }
            if (room != null) {
                roomModel.updateRoom(room)
            }
        }
    }

    private fun Room.getTurnAfterPlayerRemoved(): Int {
        val currentPlayer = getCurrentPlayerIndex()
        return if (currentPlayer <= turn.toInt() && turn.toInt() > 0) {
            turn.toInt() - 1
        } else {
            turn.toInt()
        }
    }

    private fun Room.getCurrentPlayerIndex(): Int {
        return players.indexOf(firebaseManager.getUserEmail())
    }

}