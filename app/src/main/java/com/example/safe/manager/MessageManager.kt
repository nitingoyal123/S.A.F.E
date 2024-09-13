//package com.example.safe.manager
//
//import android.annotation.SuppressLint
//import android.content.Context
//import android.content.SharedPreferences
//import android.database.Cursor
//import android.provider.Telephony
//import android.util.Log
//import com.example.safe.messageInROOM.MDatabase
//import com.example.safe.messageInROOM.MessageRepo
//import com.example.safe.messageInROOM.MessageTable
//import com.google.firebase.auth.ktx.auth
//import com.google.firebase.database.FirebaseDatabase
//import com.google.firebase.ktx.Firebase
//import kotlinx.coroutines.CoroutineScope
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.coroutineScope
//import kotlinx.coroutines.launch
//import kotlinx.coroutines.withContext
//import java.text.SimpleDateFormat
//import java.util.Locale
//
//object MessageManager {
//
//    private lateinit var repo: MessageRepo
//    private lateinit var sharedPreferences: SharedPreferences
//    private lateinit var editor: SharedPreferences.Editor
//    private var database = FirebaseDatabase.getInstance()
//    private var auth = Firebase.auth
//    var email = auth.currentUser?.email.toString().replace('.', '_')
//    var reference = database.getReference(email)
//    var DATA_STORED = "data"
//
//    var listOfMessageTables = mutableListOf<MessageTable>()
//        private set
//
//    suspend fun readMessages(context: Context) {
//        repo = MessageRepo(context)
//        sharedPreferences = context.getSharedPreferences("MESSAGES", Context.MODE_PRIVATE)
//        editor = sharedPreferences.edit()
//
//        if (sharedPreferences.getInt(DATA_STORED, -1) == 1) {
//            coroutineScope {
//                listOfMessageTables = MDatabase.getDatabase(context).messageDao().getAllMessages()
//            }
//            Log.d("MessageManager", "Messages loaded from database: ${listOfMessageTables.size}")
//        } else {
//            editor.putInt(DATA_STORED, 1)
//            editor.apply()
//            Log.d("MessageManager", "Messages loaded from Content resolver: ${listOfMessageTables.size}")
//            loadData(context)
//        }
//    }
//
//    @SuppressLint("Range")
//    suspend fun loadData(context: Context) {
//        withContext(Dispatchers.IO) {
//            val cursor: Cursor? = context.contentResolver.query(Telephony.Sms.CONTENT_URI, null, null, null, null)
//            cursor?.use {
//                while (it.moveToNext()) {
//                    var sender: String = it.getString(it.getColumnIndex(Telephony.Sms.ADDRESS))
//                    val messageBody: String = it.getString(it.getColumnIndex(Telephony.Sms.BODY))
//                    val timeStamp: Long = it.getString(it.getColumnIndex(Telephony.Sms.DATE)).toLong()
//                    val date = formatDate(timeStamp)
//
//                    if (sender[0] == '+') sender = sender.substring(3)
//
//                    val contact = sharedPreferences.getString(sender.trim(), "")
//
//                    Log.d("hello", "iefivnef9uboijv")
//
//                    saveMessageToFirebase(sender, timeStamp, messageBody)
//
//                    MDatabase.getDatabase(context).messageDao().insert(
//                        MessageTable(0, sender, messageBody, date)
//                    )
//
//                    if (contact != null && contact.isNotEmpty()) {
//                        listOfMessageTables.add(MessageTable(listOfMessageTables.size, contact, messageBody, date))
//                    } else {
//                        listOfMessageTables.add(MessageTable(listOfMessageTables.size, sender, messageBody, date))
//                    }
//                }
//            }
//            Log.d("MessageManager", "Messages loaded from content resolver: ${listOfMessageTables.size}")
//        }
//    }
//
//    private fun saveMessageToFirebase(sender: String, date: Long, messageBody: String) {
//        CoroutineScope(Dispatchers.IO).launch {
//            Log.d("iiiiii", email)
//            reference.child(date.toString()).child(sender).setValue(messageBody)
//        }
//    }
//
//    private fun formatDate(timeStamp: Long): String {
//        val dateFormat = SimpleDateFormat("HH:mm dd-MMMM", Locale.US)
//        return dateFormat.format(timeStamp)
//    }
//}


package com.example.safe.manager

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.database.Cursor
import android.provider.Telephony
import android.util.Log
import com.example.safe.messageInROOM.MDatabase
import com.example.safe.messageInROOM.MessageRepo
import com.example.safe.messageInROOM.MessageTable
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.*
import java.text.SimpleDateFormat
import java.util.Locale

object MessageManager {

    private lateinit var repo: MessageRepo
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor
    private val database = FirebaseDatabase.getInstance()
    private val auth = Firebase.auth
    private val email = auth.currentUser?.email.toString().replace('.', '_')
    private val reference = database.getReference(email)
    private const val DATA_STORED = "data"

    var listOfMessageTables = mutableListOf<MessageTable>()
        private set

    suspend fun readMessages(context: Context) {
        withContext(Dispatchers.IO) {
            repo = MessageRepo(context)
            sharedPreferences = context.getSharedPreferences("MESSAGES", Context.MODE_PRIVATE)
            editor = sharedPreferences.edit()

            if (sharedPreferences.getInt(DATA_STORED, -1) == 1) {
                listOfMessageTables = MDatabase.getDatabase(context).messageDao().getAllMessages().toMutableList()
                Log.d("MessageManager", "Messages loaded from database: ${listOfMessageTables.size}")
            } else {
                editor.putInt(DATA_STORED, 1).apply()
                Log.d("MessageManager", "Messages loaded from Content resolver: ${listOfMessageTables.size}")
                loadData(context)
            }
        }
    }

    @SuppressLint("Range")
    suspend fun loadData(context: Context) {
        withContext(Dispatchers.IO) {
            val cursor: Cursor? = context.contentResolver.query(Telephony.Sms.CONTENT_URI, null, null, null, null)
            cursor?.use {
                while (it.moveToNext()) {
                    var sender = it.getString(it.getColumnIndex(Telephony.Sms.ADDRESS))
                    val messageBody = it.getString(it.getColumnIndex(Telephony.Sms.BODY))
                    val timeStamp = it.getString(it.getColumnIndex(Telephony.Sms.DATE)).toLong()
                    val date = formatDate(timeStamp)

                    if (sender[0] == '+') sender = sender.substring(3)

                    val contact = sharedPreferences.getString(sender.trim(), "")

                    Log.d("MessageManager", "Processing message from: $sender")

                    saveMessageToFirebase(sender, timeStamp, messageBody)

                    val message = MessageTable()



                    MDatabase.getDatabase(context).messageDao().insert(message)

                    if (!contact.isNullOrBlank()) {
                        listOfMessageTables.add(message.copy(phoneNumber = contact))
                    } else {
                        listOfMessageTables.add(message)
                    }
                }
            }
            Log.d("MessageManager", "Messages loaded from content resolver: ${listOfMessageTables.size}")
        }
    }

    private fun saveMessageToFirebase(sender: String, date: Long, messageBody: String) {
        CoroutineScope(Dispatchers.IO).launch {
            reference.child(date.toString()).child(sender).setValue(messageBody)
            Log.d("MessageManager", "Saved message to Firebase: $messageBody")
        }
    }

    private fun formatDate(timeStamp: Long): String {
        val dateFormat = SimpleDateFormat("HH:mm dd-MMMM", Locale.US)
        return dateFormat.format(timeStamp)
    }
}

