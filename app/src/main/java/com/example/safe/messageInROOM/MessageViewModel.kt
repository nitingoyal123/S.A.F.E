//package com.example.safe.messageInROOM
//
//import android.app.Application
//import androidx.lifecycle.AndroidViewModel
//import androidx.lifecycle.LiveData
//import androidx.lifecycle.viewModelScope
//import kotlinx.coroutines.launch
//
//class MessageViewModel(application: Application, repository: MessageRepo) : AndroidViewModel(application) {
//
//
//    private var repo = MessageRepo(application )
//
//
//    fun insertMessage(message: MessageTable) = viewModelScope.launch {
//        repo.insertMessage(message)
//    }
//
//    fun getAllMessages(): LiveData<MutableList<MessageTable>> {
//        return repo.getAllMessages()
//    }
//}
