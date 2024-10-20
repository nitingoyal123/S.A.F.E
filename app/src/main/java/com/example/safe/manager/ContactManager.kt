package com.example.safe.manager

import android.annotation.SuppressLint
import android.content.Context
import android.provider.ContactsContract
import android.util.Log
import com.example.safe.utility.AppUtility
import com.google.i18n.phonenumbers.PhoneNumberUtil
import com.google.i18n.phonenumbers.Phonenumber
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object ContactManager {

    private const val CONTACTS = "contacts"
    private const val CONTACTS_LOADED = "contactsLoaded"

    @SuppressLint("Range")
    suspend fun loadContacts(context: Context) {
        Log.d("Progress", "Load Contacts Called")
        withContext(Dispatchers.IO) {
            val sharedPreferences = context.getSharedPreferences(CONTACTS, Context.MODE_PRIVATE)

            if (sharedPreferences.getInt(CONTACTS_LOADED, -1) == 101) {
                Log.d("Contact", "Contacts already exists")
                return@withContext
            }
                Log.d("Contact", "Contacts not exists")

            val editor = sharedPreferences.edit()
            val contentResolver = context.contentResolver
            val cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, "${ContactsContract.Contacts.DISPLAY_NAME} ASC")

            cursor?.use {
                while (it.moveToNext()) {
                    val name = it.getString(it.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
                    val contactID = it.getString(it.getColumnIndex(ContactsContract.Contacts._ID))
                    val phoneNumber = getPhoneNumberFromContactId(contactID, context)
//                    val photo = it.getString(it.getColumnIndex(ContactsContract.Contacts.PHOTO_ID))

                    if (!name.isNullOrBlank() && phoneNumber.isNotBlank()) {
                        val mobileNumber = AppUtility.formatPhoneNumber(phoneNumber)
                        editor.putString(mobileNumber, name)
                        editor.apply()
                        Log.d("Contacts", "$mobileNumber : $name")
                    }
                }
            }
            editor.putInt(CONTACTS_LOADED, 101).commit()
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
