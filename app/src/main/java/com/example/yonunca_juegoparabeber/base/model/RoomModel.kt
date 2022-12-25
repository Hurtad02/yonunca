package com.example.yonunca_juegoparabeber.base.model

import androidx.lifecycle.MutableLiveData
import com.example.yonunca_juegoparabeber.online.model.Room
import com.example.yonunca_juegoparabeber.online.view.OnlineGameUIState
import com.example.yonunca_juegoparabeber.utils.ROOMS_COLLECTION
import com.example.yonunca_juegoparabeber.utils.getYesterday
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

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

    suspend fun getRoomByCode(code: String): Room? {
        val querySnapshot = db.collection("salas")
            .whereEqualTo("codigo", code)
            .get().await()
        if (querySnapshot != null && !querySnapshot.isEmpty) {
            val doc = querySnapshot.documents[0]
            return Room(doc)
        }
        return null
    }

    fun joinPlayer(room: Room) {
        val playersMap = mapOf(
            "jugadores" to room.players
        )

        db.collection("salas")
            .document(room.id)
            .update(playersMap)
    }

    suspend fun getPublicRooms(): List<Room> {
        val documents = db.collection("salas")
            .whereGreaterThan("actualizado", getYesterday())
            .whereEqualTo("codigo", "")
            .get().await().documents
        val rooms = mutableListOf<Room>()
        documents.forEach{
            rooms.add(Room(it))
        }
        return rooms
    }

    suspend fun createRoom(room: Room): Room? {
        val alreadyExist = getRoomByCode(room.code) != null

        if (alreadyExist) {
            return null
        }

        val roomMap = hashMapOf(
            "nombre" to room.name,
            "frase" to room.phrase,
            "turno" to room.turn,
            "jugadores" to room.players,
            "actualizado" to room.date,
            "codigo" to room.code
        )
        val reference = db.collection(ROOMS_COLLECTION)
            .add(roomMap).await()
        val document = reference.get().await()
        return Room(document)
    }

    fun updateRoom(room: Room) {
        val map = hashMapOf(
            "nombre" to room.name,
            "frase" to room.phrase,
            "turno" to room.turn,
            "jugadores" to room.players,
            "actualizado" to room.date,
            "codigo" to room.code
        )
        db.collection(ROOMS_COLLECTION)
            .document(room.id)
            .update(map)
    }

}