package com.example.safe.messageInROOM

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData

class MessageRepo(context : Context) {

    private var messageDao : MessageDao

    init {
        var db = MDatabase.getDatabase(context)
        messageDao = db.messageDao()
    }

    suspend fun getAllMessages(): MutableList<MessageTable> {
        return messageDao.getAllMessages()
    }

    suspend fun insertMessage(message: MessageTable) {
        messageDao.insert(message)
    }
}
