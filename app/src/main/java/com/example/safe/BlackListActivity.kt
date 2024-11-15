package com.example.safe

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.platform.LocalGraphicsContext
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.safe.adapters.BlockedContactRecyclerViewAdapter
import com.example.safe.databinding.ActivityBlackListBinding
import com.example.safe.manager.ContactManager
import com.example.safe.messageInROOM.MDatabase
import com.example.safe.model.Contact
import com.example.safe.model.MessageTable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class BlackListActivity : AppCompatActivity() {

    private lateinit var adapter : BlockedContactRecyclerViewAdapter
    private lateinit var binding : ActivityBlackListBinding
    private lateinit var database : MDatabase
    private lateinit var blockedContacts : List<String>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("Progress", "BlockList Activity Started")
        binding = DataBindingUtil.setContentView(this, R.layout.activity_black_list)
        database = MDatabase.getDatabase(this@BlackListActivity)
        blockedContacts = listOf()
        adapter = BlockedContactRecyclerViewAdapter(this@BlackListActivity, blockedContacts)
        binding.rvBlockedContacts.layoutManager = LinearLayoutManager(this@BlackListActivity)
        binding.rvBlockedContacts.adapter = adapter

        loadDataAndUpdateUI()

        adapter.onItemClick = {
            showUnblockAlertDialog(it)
        }

    }

    private fun loadDataAndUpdateUI() {
        Log.d("blockedContacts", blockedContacts.toString())
        CoroutineScope(Dispatchers.IO).launch {
            blockedContacts = database.messageDao().getBlockedPhoneNumbers()
            Log.d("blockedContacts", blockedContacts.toString())
            withContext(Dispatchers.Main) {
                adapter.updateData(blockedContacts)
                updateUI()
            }
        }
    }

    private fun updateUI() {
        if (blockedContacts.size == 0) {
            binding.tvBlockedContacts.visibility = View.VISIBLE
        } else {
            binding.tvBlockedContacts.visibility = View.INVISIBLE
        }
        adapter.notifyDataSetChanged()
    }

    private fun showUnblockAlertDialog(contact: Contact) {
        // Create and show the AlertDialog
        val builder = AlertDialog.Builder(this)
        builder.setMessage("Unblock?")
            .setCancelable(false)  // Prevent closing the dialog by tapping outside
            .setPositiveButton("Yes") { dialog, id ->
                // Handle the "Yes" action here
                unblockContact(contact)
            }
            .setNegativeButton("No") { dialog, id ->
                // Handle the "No" action here (e.g., dismiss the dialog)
                dialog.dismiss()
            }
        // Create the AlertDialog and show it
        val alert = builder.create()
        alert.show()
    }

    // Example method to unblock a contact (you can customize this)
    private fun unblockContact(contact: Contact) {
        CoroutineScope(Dispatchers.IO).launch {
            database.messageDao().unBlockMessagesUsingPhoneNumber(contact.phoneNumber)
            loadDataAndUpdateUI()
        }
//        updateUI()
    }

//    private fun checkContacts() {
//        CoroutineScope(Dispatchers.IO).launch {
//            blockedContacts.addAll(database.messageDao().getBlockedPhoneNumbers()).also {
//                withContext(Dispatchers.Main) {
//                    updateUI()
//                }
//            }
//        }
//
//    }
//
//    private fun updateUI() {
//        binding.progressBar.visibility = View.INVISIBLE
//        adapter.notifyDataSetChanged()
//    }
}