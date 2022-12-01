package com.example.yonunca_juegoparabeber.base.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Liked::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun likedDAO(): LikedDAO

    companion object {
        private var INSTANCE: AppDatabase? = null
        fun getDatabase(context: Context): AppDatabase {
            if (INSTANCE == null) {
                synchronized(this) {
                    INSTANCE =
                        Room.databaseBuilder(context,AppDatabase::class.java, "database")
                            .build()
                }
            }
            return INSTANCE!!
        }
    }
}