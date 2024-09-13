package com.example.safe.messageInROOM

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface MessageDao {

    @Insert
    suspend fun insert(message: MessageTable)

    @Query("SELECT * FROM messages")
    suspend fun getAllMessages(): MutableList<MessageTable>
}
