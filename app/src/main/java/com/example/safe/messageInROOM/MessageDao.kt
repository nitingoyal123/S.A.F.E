package com.example.safe.messageInROOM

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.safe.model.MessageTable

@Dao
interface MessageDao {

    @Insert
    suspend fun insert(message: MessageTable)

    @Query("SELECT * FROM messages WHERE isBlocked = ${false} ORDER BY timeStamp DESC")
    suspend fun getAllMessages(): MutableList<MessageTable>

    @Query("UPDATE messages SET isBlocked = ${true} WHERE phoneNumber = :phoneNumber")
    suspend fun blockMessagesUsingPhoneNumber(phoneNumber : String)

    @Query("UPDATE messages SET isBlocked = ${false} WHERE phoneNumber = :phoneNumber")
    suspend fun unBlockMessagesUsingPhoneNumber(phoneNumber : String)

    @Query("SELECT DISTINCT phoneNumber FROM messages WHERE isBlocked=${true} ORDER BY phoneNumber")
    fun getBlockedPhoneNumbers() : List<String>
}
