package com.example.safe.messageInROOM

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.safe.model.Contact
import com.example.safe.model.MessageTable

@Dao
interface ContactDao {

    @Insert
    suspend fun insert(contact : Contact)

    @Query("SELECT * FROM contacts WHERE blocked=${true}")
    suspend fun getBlockedContacts() : List<Contact>

    @Query("SELECT * FROM contacts WHERE blocked=${false}")
    suspend fun getUnblockedContacts() : List<Contact>

    

}
