package com.example.safe.messageInROOM

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [MessageTable::class], version = 1)
abstract class MDatabase : RoomDatabase() {

    abstract fun messageDao(): MessageDao

    companion object {
        @Volatile
        private var INSTANCE: MDatabase? = null

        fun getDatabase(context: Context): MDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MDatabase::class.java,
                    "message_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
