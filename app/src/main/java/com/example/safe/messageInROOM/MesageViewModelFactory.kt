//package com.example.safe.messageInROOM
//
//import android.app.Application
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.ViewModelProvider
//
//class MessageViewModelFactory(
//    private val app: Application,
//    private val repository: MessageRepo
//) : ViewModelProvider.Factory {
//    override fun <T : ViewModel> create(modelClass: Class<T>): T {
//        if (modelClass.isAssignableFrom(MessageViewModel::class.java)) {
//            @Suppress("UNCHECKED_CAST")
//            return MessageViewModel(app, repository) as T
//        }
//        throw IllegalArgumentException("Unknown ViewModel class")
//    }
//}
