package com.example.safe.messageInROOM

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.safe.model.Contact
import com.example.safe.model.MessageTable

@Database(entities = [MessageTable::class, Contact::class], version = 5)
abstract class MDatabase : RoomDatabase() {

    abstract fun messageDao(): MessageDao

    abstract fun contactDao() : ContactDao

    companion object {
        @Volatile
        private var INSTANCE: MDatabase? = null

        fun getDatabase(context: Context): MDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MDatabase::class.java,
                    "message_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
