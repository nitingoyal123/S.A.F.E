package com.example.safe

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.safe.adapters.HomeContactRecyclerViewAdapter

class HomeFragment : Fragment() {

    private lateinit var rvContacts : RecyclerView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view = inflater.inflate(R.layout.fragment_home, container, false)
        rvContacts = view.findViewById(R.id.rvContacts)

        rvContacts.layoutManager = LinearLayoutManager(requireContext())

        if (ContactManager.listOfContacts.size == 0) {
            ContactManager.loadContacts(requireContext())
        }
            rvContacts.adapter = HomeContactRecyclerViewAdapter(requireContext(), ContactManager.listOfContacts)

        var image = view.findViewById<ImageView>(R.id.profileImage)
        image.setOnClickListener {
            toMain(it)
        }

        return view
    }
    fun toMain(view : View) {
        startActivity(Intent(requireContext(),MainActivity::class.java))
    }

//    override fun onResume() {
//        super.onResume()
//        if (areContactsLoaded == false) {
//            areContactsLoaded = true
//            loadContacts()
//        }
//        rvContacts.adapter = HomeContactRecyclerViewAdapter(requireContext(),listOfContacts)
//    }

}