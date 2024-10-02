package com.example.safe.manager

import android.annotation.SuppressLint
import android.content.Context
import android.provider.ContactsContract
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object ContactManager {

    private const val CONTACTS = "contacts"
    private const val CONTACTS_LOADED = "contactsLoaded"

    @SuppressLint("Range")
    suspend fun loadContacts(context: Context) {
        withContext(Dispatchers.IO) {
            val sharedPreferences = context.getSharedPreferences(CONTACTS, Context.MODE_PRIVATE)

            if (sharedPreferences.getBoolean(CONTACTS_LOADED, false)) {
                return@withContext
            }

            val editor = sharedPreferences.edit()
            val contentResolver = context.contentResolver
            val cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, "${ContactsContract.Contacts.DISPLAY_NAME} ASC")

            cursor?.use {
                while (it.moveToNext()) {
                    val name = it.getString(it.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
                    val contactID = it.getString(it.getColumnIndex(ContactsContract.Contacts._ID))
                    var phoneNumber = getPhoneNumberFromContactId(contactID, context)
                    val photo = it.getString(it.getColumnIndex(ContactsContract.Contacts.PHOTO_ID))

                    if (!name.isNullOrBlank() && !phoneNumber.isNullOrBlank()) {
                        phoneNumber = phoneNumber.replace(" ", "")
                        val key = if (phoneNumber.length > 3 && phoneNumber[0] == '+') {
                            phoneNumber.substring(3)
                        } else {
                            phoneNumber
                        }
                        editor.putString(key, name)
                        Log.d("ContactManager", "Stored contact: $name with phone: $phoneNumber")
                    }
                }
            }
            editor.putBoolean(CONTACTS_LOADED, true).apply()
        }
    }

    private fun getPhoneNumberFromContactId(contactId: String, context: Context): String {
        var phoneNumber = ""
        val cursor = context.contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            null,
            "${ContactsContract.CommonDataKinds.Phone.CONTACT_ID} = ?",
            arrayOf(contactId),
            null
        )
        cursor?.use {
            if (it.moveToFirst()) {
                val numberIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
                phoneNumber = it.getString(numberIndex)
            }
        }
        return phoneNumber
    }
}
