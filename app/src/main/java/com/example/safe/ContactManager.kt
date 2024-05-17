package com.example.safe

import android.annotation.SuppressLint
import android.content.Context
import android.provider.ContactsContract
import android.util.Log
import com.example.safe.model.Contact

object ContactManager {




    var listOfContacts = mutableListOf<Contact>()
    private set

    @SuppressLint("Range")
    fun loadContacts(context : Context): MutableList<Contact> {
        var sharedPreferences = context.getSharedPreferences("Contacts", Context.MODE_PRIVATE)
        var editor = sharedPreferences.edit()
        val contentResolver = context.contentResolver
        val cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI,null,null,null, "${ContactsContract.Contacts.DISPLAY_NAME} ASC")

        cursor?.use {
            while (it.moveToNext()) {
                val name =
                    cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
                val contactID =
                    cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID))
                var phoneNumber = getPhoneNumberFromContactId(contactID, context)
                val photo =
                    cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.PHOTO_ID))
                Log.d("HUH", "$name $phoneNumber")
                if (name != null && phoneNumber != null) {
                    phoneNumber = phoneNumber.replace(" ", "")
                    if (phoneNumber.length > 3 && phoneNumber[0] == '+') {
                        editor.putString(phoneNumber.substring(3), name)
                        listOfContacts.add(Contact(name,phoneNumber.substring(3), R.drawable.airtel))
                    }
                    else {
                        listOfContacts.add(Contact(name, phoneNumber, R.drawable.airtel))
                        editor.putString(phoneNumber, name)
                    }
                    editor.apply()
                    Log.d("HUHHH", sharedPreferences.getString(phoneNumber, "").toString())
                }
            }
        }
        return listOfContacts
        cursor?.close()
    }

    private fun getPhoneNumberFromContactId(contactId: String, context : Context): String {
        var phoneNumber = ""
        val cursor = context.contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            null,
            "${ContactsContract.CommonDataKinds.Phone.CONTACT_ID} = ?",
            arrayOf(contactId),
            null
        )
        if (cursor != null && cursor.moveToFirst()) {
            val numberIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
            phoneNumber = cursor.getString(numberIndex)
            cursor.close()
        }
        return phoneNumber
    }

}