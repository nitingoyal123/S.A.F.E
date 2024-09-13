//package com.example.safe
//
//import android.app.Dialog
//import android.graphics.Color
//import android.os.Bundle
//import android.util.Log
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.TextView
//import androidx.constraintlayout.widget.ConstraintLayout
//import androidx.fragment.app.Fragment
//import androidx.recyclerview.widget.LinearLayoutManager
//import androidx.recyclerview.widget.RecyclerView
//import com.example.safe.manager.MessageManager
//import com.example.safe.messageInROOM.MessageTable
//import com.example.safe.retrofit.APIResponse
//import com.example.safe.retrofit.RetrofitClient
//import retrofit2.Call
//import retrofit2.Callback
//import retrofit2.Response
//
//class HomeFragment : Fragment() {
//
//    private lateinit var listOfMessageTable : List<MessageTable>
//
//    private lateinit var rv : RecyclerView
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        val view = inflater.inflate(R.layout.fragment_home, container, false)
//
//        var adapter = HistoryMessageRecyclerViewAdapter(requireContext(), MessageManager.listOfMessageTables)
//
//        rv = view.findViewById(R.id.rvMessages)
//
//        rv.layoutManager = LinearLayoutManager(requireContext())
//        rv.adapter = adapter
//
//        adapter.onItemClick = {
//            showMyDialog(it)
//        }
//        return view
//    }
//
//    private fun showMyDialog(messageTable: MessageTable) {
//
//        var dialoG = Dialog(requireContext())
//        dialoG.setContentView(R.layout.message_fake_or_not_layout)
//        var txt_fake_or_not = dialoG.findViewById<TextView>(R.id.txt_scam)
//        var txt_sender = dialoG.findViewById<TextView>(R.id.txt_sender_name)
//        var txt_message = dialoG.findViewById<TextView>(R.id.txt_message_body)
//        var layout = dialoG.findViewById<ConstraintLayout>(R.id.layout1)
//
//        txt_message.text = messageTable.message
//        txt_sender.text = messageTable.phoneNumber
//        var spam = sendAPIRequest(messageTable.message)
//        if (spam) {
//            txt_fake_or_not.text = "LIKELY SPAM"
//            layout.setBackgroundColor(Color.argb(100, 218, 0 , 0))
//        }
//        else {
//            txt_fake_or_not.text = ""
//            layout.setBackgroundColor(Color.argb(86, 182, 245, 174))
//        }
//        dialoG.show()
//    }
//
//}
//
//private fun sendAPIRequest(message: String) : Boolean {
//    var ans : Boolean = false
//
//    val apiService = RetrofitClient.apiService
//    val call = apiService.predictText(mapOf("user_input" to message))
//
//    call.enqueue(object : Callback<APIResponse> {
//        override fun onResponse(call: Call<APIResponse>, response: Response<APIResponse>) {
//            Log.d("HEYAAA", response.body()?.prediction.toString())
//            if (response.isSuccessful) {
//                val apiResponse = response.body()
//                if (apiResponse != null) {
//                    if (apiResponse.prediction.equals("Spam")) {
//                        Log.d("CHECKING11", "hiiiii")
//                    }
//                }
//            } else {
//                Log.e("TAGY", "ERRORRR")
//            }
//        }
//
//        override fun onFailure(call: Call<APIResponse>, t: Throwable) {
//            // API call failed
//            Log.d("TAGY","ERRORRRRRRRR")
//            // Handle failure
//        }
//    })
//    return ans
//}
//
//
//
//
////import android.content.Intent
////import android.os.Bundle
////import android.view.LayoutInflater
////import android.view.View
////import android.view.ViewGroup
////import android.widget.ImageView
////import androidx.fragment.app.Fragment
////import androidx.recyclerview.widget.LinearLayoutManager
////import androidx.recyclerview.widget.RecyclerView
////import com.example.safe.adapters.HomeContactRecyclerViewAdapter
////
////class HomeFragment : Fragment() {
////
////    private lateinit var rvContacts : RecyclerView
////
////    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
////        var view = inflater.inflate(R.layout.fragment_home, container, false)
////        rvContacts = view.findViewById(R.id.rvContacts)
////
////        rvContacts.layoutManager = LinearLayoutManager(requireContext())
////
////        if (ContactManager.listOfContacts.size == 0) {
////            ContactManager.loadContacts(requireContext())
////        }
////            rvContacts.adapter = HomeContactRecyclerViewAdapter(requireContext(), ContactManager.listOfContacts)
////
////        var image = view.findViewById<ImageView>(R.id.profileImage)
////        image.setOnClickListener {
////            toMain(it)
////        }
////
////        return view
////    }
////    fun toMain(view : View) {
////        startActivity(Intent(requireContext(),MainActivity::class.java))
////    }
////}


package com.example.safe

import android.annotation.SuppressLint
import android.app.Dialog
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.safe.manager.MessageManager
import com.example.safe.messageInROOM.MessageTable
import com.example.safe.retrofit.APIResponse
import com.example.safe.retrofit.RetrofitClient
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFragment : Fragment() {

    private lateinit var rv: RecyclerView
    private var clicked = false

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        rv = view.findViewById(R.id.rvMessages)
        rv.layoutManager = LinearLayoutManager(requireContext())
        val adapter = HistoryMessageRecyclerViewAdapter(requireContext(), MessageManager.listOfMessageTables)
        rv.adapter = adapter

        adapter.onItemClick = { messageTable ->
            rv.isClickable = false
            showMyDialog(messageTable)
        }

        view.findViewById<FloatingActionButton>(R.id.reloadButton).setOnClickListener {
            view.findViewById<ProgressBar>(R.id.reloadProgressBar).visibility = View.VISIBLE
            reloadMsgs()
            adapter.notifyDataSetChanged()
            rv.adapter = adapter
            rv.visibility = View.VISIBLE
            view.findViewById<ProgressBar>(R.id.reloadProgressBar).visibility = View.INVISIBLE
        }

        return view
    }

    private fun reloadMsgs() {
        lifecycleScope.launch {
            MessageManager.loadData(requireContext())
        }
    }

    private fun showMyDialog(messageTable: MessageTable) {
//        clicked = false
        val dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.message_fake_or_not_layout)

        val txtFakeOrNot = dialog.findViewById<TextView>(R.id.txt_scam)
        val txtSender = dialog.findViewById<TextView>(R.id.txt_sender_name)
        val txtMessage = dialog.findViewById<TextView>(R.id.txt_message_body)
        val layout = dialog.findViewById<ConstraintLayout>(R.id.layout1)

        txtMessage.text = messageTable.message
        txtSender.text = messageTable.phoneNumber

        sendAPIRequest(messageTable.message) { isSpam ->
            if (isSpam) {
                txtFakeOrNot.text = "LIKELY SPAM"
                layout.setBackgroundColor(Color.argb(100, 218, 0, 0))
            } else {
                txtFakeOrNot.text = ""
                layout.setBackgroundColor(Color.argb(86, 182, 245, 174))
            }
            dialog.show()
            rv.isClickable = true
        }
    }

    private fun sendAPIRequest(message: String, callback: (Boolean) -> Unit) {
        val apiService = RetrofitClient.apiService
        val call = apiService.predictText(mapOf("user_input" to message))

        call.enqueue(object : Callback<APIResponse> {
            override fun onResponse(call: Call<APIResponse>, response: Response<APIResponse>) {
                if (response.isSuccessful) {
                    val apiResponse = response.body()
                    if (apiResponse != null) {
//                        if (apiResponse.spam  == 0)
//                        val isSpam = apiResponse.prediction.equals("Spam", ignoreCase = true)
                        callback(apiResponse.spam == 1)
                    }
                } else {
                    Log.e("API_CALL_ERROR", "Failed to get API response")
                    callback(false) // Default to not spam
                }
            }

            override fun onFailure(call: Call<APIResponse>, t: Throwable) {
                Log.e("API_CALL_ERROR", "API call failed", t)
                callback(false) // Default to not spam
            }
        })
    }
}
