package com.example.yonunca_juegoparabeber.online.model

import android.os.Parcel
import android.os.Parcelable
import com.google.firebase.firestore.DocumentSnapshot
import java.util.Date

data class Room(
    val id: String,
    val name: String,
    var phrase: String,
    var turn: Double,
    var players: List<String>,
    var date: Date,
    var code: String
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readDouble(),
        parcel.createStringArrayList()?.toList() ?: listOf(),
        Date(parcel.readLong()),
        parcel.readString() ?: ""
    ) {
    }
    constructor(document: DocumentSnapshot) : this(
        id = document.id,
        name = document.getString("nombre") ?: "",
        phrase = document.getString("frase") ?: "",
        turn = document.getDouble("turno") ?: 0.0,
        players = document.get("jugadores") as List<String>,
        date = document.getDate("actualizado") ?: Date(),
        code = document.getString("codigo") ?: ""
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(name)
        parcel.writeString(phrase)
        parcel.writeDouble(turn)
        parcel.writeStringList(players)
        parcel.writeLong(date.time)
        parcel.writeString(code)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Room> {
        override fun createFromParcel(parcel: Parcel): Room {
            return Room(parcel)
        }

        override fun newArray(size: Int): Array<Room?> {
            return arrayOfNulls(size)
        }
    }

    fun isFull(): Boolean {
        return players.size == 5
    }

    fun changeTurn() {
        var newTurn = turn.toInt() + 1
        if (newTurn == players.size) {
            newTurn = 0
        }
        turn = newTurn.toDouble()
    }

}