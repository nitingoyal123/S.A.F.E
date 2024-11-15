package com.example.safe.messageInROOM

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.safe.model.Contact
import com.example.safe.model.MessageTable

@Dao
interface ContactDao {
    @Insert
    suspend fun insert(contact : Contact)

    @Update
    suspend fun update(contact: Contact)

    @Query("SELECT * FROM contacts WHERE blocked=${true}")
    suspend fun getBlockedContacts() : MutableList<Contact>

    @Query("SELECT * FROM contacts WHERE blocked=${false}")
    suspend fun getUnblockedContacts() : List<Contact>

    @Query("SELECT * FROM contacts ORDER BY name")
    suspend fun getAllContacts() : List<Contact>

    @Delete
    suspend fun delete(contact : Contact)

}
