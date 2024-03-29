package com.example.safe

import android.content.Intent
import android.os.Bundle
import android.provider.ContactsContract
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.safe.adapters.HomeContactRecyclerViewAdapter
import com.example.safe.model.Contact
import com.example.safe.model.HistoryDataItem
import com.example.safe.model.HomeDataItem

class HomeFragment : Fragment() {

    private lateinit var rvContacts : RecyclerView

    private var listOfContacts = mutableListOf<Contact>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view = inflater.inflate(R.layout.fragment_home, container, false)
        rvContacts = view.findViewById(R.id.rvContacts)

        loadContacts2()

        rvContacts.layoutManager = LinearLayoutManager(requireContext())
        rvContacts.adapter = HomeContactRecyclerViewAdapter(requireContext(),listOfContacts)

        var image = view.findViewById<ImageView>(R.id.profileImage)
        image.setOnClickListener {
            toMain(it)
        }

        return view
    }


    private fun loadContacts2() {
        listOfContacts.add(Contact("nitin","idufubwefuwe",R.drawable.airtel))
        listOfContacts.add(Contact("nitin","idufubwefuwe",R.drawable.airtel))
        listOfContacts.add(Contact("nitin","idufubwefuwe",R.drawable.airtel))
        listOfContacts.add(Contact("nitin","idufubwefuwe",R.drawable.airtel))
        listOfContacts.add(Contact("nitin","idufubwefuwe",R.drawable.airtel))
        listOfContacts.add(Contact("nitin","idufubwefuwe",R.drawable.airtel))
        listOfContacts.add(Contact("nitin","idufubwefuwe",R.drawable.airtel))
        listOfContacts.add(Contact("nitin","idufubwefuwe",R.drawable.airtel))
        listOfContacts.add(Contact("nitin","idufubwefuwe",R.drawable.airtel))
        listOfContacts.add(Contact("nitin","idufubwefuwe",R.drawable.airtel))
        listOfContacts.add(Contact("nitin","idufubwefuwe",R.drawable.airtel))

    }

//    private fun loadContacts() {
//
//        var cursor = requireContext().contentResolver.query(ContactsContract.Contacts.CONTENT_URI,null,null,null,null)
//        if (cursor != null && cursor.moveToFirst()) {
//            var nameIndex = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)
//            var numberIndex = cursor.getColumnIndex(ContactsContract.Contacts._ID)
//            var photoIndex = cursor.getColumnIndex(ContactsContract.Contacts.PHOTO_ID)
//            do {
//                var name = cursor.getString(nameIndex)
//                var phoneNumber = cursor.getString(numberIndex)
//
//                listOfContacts.add(Contact(name,phoneNumber,R.drawable.airtel))
//
//            } while (cursor.moveToNext())
//        }
//
//    }

    fun toMain(view : View) {
        startActivity(Intent(requireContext(),MainActivity::class.java))
    }

}