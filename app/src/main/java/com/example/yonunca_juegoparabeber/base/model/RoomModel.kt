package com.example.yonunca_juegoparabeber.base.model

import android.os.Parcelable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.yonunca_juegoparabeber.online.model.Room
import com.example.yonunca_juegoparabeber.online.view.OnlineGameUIState
import com.example.yonunca_juegoparabeber.utils.ROOMS_COLLECTION
import com.example.yonunca_juegoparabeber.utils.getYesterday
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import java.util.*

class RoomModel {

    private val db = Firebase.firestore

    fun getRoomData(id: String, liveData: MutableLiveData<OnlineGameUIState>) {
        db.collection("salas")
            .document(id).addSnapshotListener{ snapshot, e ->
                if (e != null){
                    return@addSnapshotListener
                }
                if (snapshot != null){
                    val room = Room(snapshot)
                    liveData.postValue(liveData.value?.copy(room = room))
                }
            }
    }

    fun joinPlayer(room: Room) {
        val playersMap = mapOf(
            "jugadores" to room.players
        )

        db.collection("salas")
            .document(room.id)
            .update(playersMap)
    }

    suspend fun getRooms(): List<Room> {
        val documents = db.collection("salas")
            .whereGreaterThan("actualizado", getYesterday())
            .get().await().documents
        val rooms = mutableListOf<Room>()
        documents.forEach{
            rooms.add(Room(it))
        }
        return rooms
    }

    suspend fun createRoom(room: Room): Room {
        val roomMap = hashMapOf(
            "codigo" to room.code,
            "frase" to room.phrase,
            "turno" to room.turn,
            "jugadores" to room.players,
            "actualizado" to room.date
        )

        val reference = db.collection(ROOMS_COLLECTION)
            .add(roomMap).await()

        val document = reference.get().await()
        return Room(document)
    }

    fun updateRoom(room: Room) {
        val map = hashMapOf(
            "codigo" to room.code,
            "frase" to room.phrase,
            "turno" to room.turn,
            "jugadores" to room.players,
            "actualizado" to room.date
        )
        db.collection(ROOMS_COLLECTION)
            .document(room.id)
            .update(map)
    }

}