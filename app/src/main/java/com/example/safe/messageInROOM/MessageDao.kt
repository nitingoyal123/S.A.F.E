package com.example.safe.messageInROOM

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.safe.model.MessageTable

@Dao
interface MessageDao {

    @Insert
    suspend fun insert(message: MessageTable)

    @Query("SELECT * FROM messages ORDER BY timeStamp DESC")
    suspend fun getAllMessages(): MutableList<MessageTable>
}
