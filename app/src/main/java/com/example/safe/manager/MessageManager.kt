package com.example.safe.manager

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.database.Cursor
import android.provider.Telephony
import android.util.Log
import com.example.safe.messageInROOM.MDatabase
import com.example.safe.messageInROOM.MessageRepo
import com.example.safe.model.MessageTable
import com.example.safe.utility.AppUtility
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.*
import java.text.SimpleDateFormat
import java.util.Locale

object MessageManager {

    private const val CONTACTS = "contacts"
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
        Log.d("Progress", "Read messages called")
        withContext(Dispatchers.IO) {
            repo = MessageRepo(context)
            sharedPreferences = context.getSharedPreferences(CONTACTS, Context.MODE_PRIVATE)
            editor = sharedPreferences.edit()
            Log.d("checkSP", sharedPreferences.getInt(DATA_STORED, -1).toString())
            if (sharedPreferences.getInt(DATA_STORED, -1) == 12) {
                Log.d("Progress", "Messages loading from database")
                listOfMessageTables = MDatabase.getDatabase(context).messageDao().getAllMessages().toMutableList()
            } else {
                Log.d("Progress", "Messages loading from mobile phone")
                editor.putInt(DATA_STORED, 12).apply()
                loadData(context)
            }
        }
    }

    @SuppressLint("Range")
    suspend fun loadData(context: Context) {
        var count = 0
        sharedPreferences = context.getSharedPreferences(CONTACTS, Context.MODE_PRIVATE)
        withContext(Dispatchers.IO) {
            val cursor: Cursor? = context.contentResolver.query(Telephony.Sms.CONTENT_URI, null, null, null, null)
            cursor?.use {
                while (it.moveToNext() && count < 1000) {
                    count++
                    var sender = it.getString(it.getColumnIndex(Telephony.Sms.ADDRESS))
                    val messageBody = it.getString(it.getColumnIndex(Telephony.Sms.BODY))
                    val timeStamp = it.getString(it.getColumnIndex(Telephony.Sms.DATE)).toLong()

                    sender = AppUtility.formatPhoneNumber(sender)

                    val contact = sender

                    saveMessageToFirebase(contact, timeStamp, messageBody)

                    val message = MessageTable(timeStamp=timeStamp, phoneNumber = contact, message = messageBody, date = formatDate(timeStamp))

                    Log.d("dataLoading", message.toString())

                    MDatabase.getDatabase(context).messageDao().insert(message)

                    if (contact.isNotBlank()) {
                        listOfMessageTables.add(message.copy(phoneNumber = contact))
                    } else {
                        listOfMessageTables.add(message)
                    }
                }
            }
        }
    }

    private fun saveMessageToFirebase(sender: String, date: Long, messageBody: String) {
        CoroutineScope(Dispatchers.IO).launch {
            reference.child(date.toString()).child(sender).setValue(messageBody)
//            Log.d("MessageManager", "Saved message to Firebase: $messageBody")
        }
    }

    private fun formatDate(timeStamp: Long): String {
        val dateFormat = SimpleDateFormat("HH:mm dd-MMMM", Locale.US)
        return dateFormat.format(timeStamp)
    }
}

