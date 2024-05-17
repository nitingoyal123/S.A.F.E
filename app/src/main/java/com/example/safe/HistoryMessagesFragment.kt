package com.example.safe

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.SharedPreferences
import android.database.Cursor
import android.graphics.Color
import android.os.Bundle
import android.provider.Telephony
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.safe.model.Message
import com.example.safe.retrofit.APIResponse
import com.example.safe.retrofit.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Locale

class HistoryMessagesFragment : Fragment() {

    private lateinit var rv : RecyclerView

    var listOfMessages = mutableListOf<Message>()

    lateinit var sharedPreferences : SharedPreferences

    lateinit var editor : SharedPreferences.Editor

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_history_messages, container, false)

        sharedPreferences = requireContext().getSharedPreferences("Contacts", Context.MODE_PRIVATE)
        editor = sharedPreferences.edit()

        if (listOfMessages.isEmpty()){
            readMessages()
        }

        var adapter = HistoryMessageRecyclerViewAdapter(requireContext(),listOfMessages)

        rv = view.findViewById(R.id.rvMessages)

        rv.layoutManager = LinearLayoutManager(requireContext())
        rv.adapter = adapter

        adapter.onItemClick = {
            showMyDialog(it)
        }



        return view
    }

    private fun showMyDialog(message: Message) {

        var dialoG = Dialog(requireContext())
        dialoG.setContentView(R.layout.message_fake_or_not_layout)
        var txt_fake_or_not = dialoG.findViewById<TextView>(R.id.txt_scam)
        var txt_sender = dialoG.findViewById<TextView>(R.id.txt_sender_name)
        var txt_message = dialoG.findViewById<TextView>(R.id.txt_message_body)
        var layout = dialoG.findViewById<ConstraintLayout>(R.id.layout1)

        txt_message.text = message.message
        txt_sender.text = message.phoneNumber
        if (message.DeepFake) {
            txt_fake_or_not.text = "LIKELY SPAM"
            layout.setBackgroundColor(Color.argb(100, 218, 0 , 0))
        }
        else {
            txt_fake_or_not.text = ""
            layout.setBackgroundColor(Color.argb(86, 182, 245, 174))
        }
        dialoG.show()
    }

    @SuppressLint("Range")
    private fun readMessages() {
        val cursor : Cursor? = requireContext().contentResolver.query(Telephony.Sms.CONTENT_URI, null, null, null, null)
        cursor?.use {
            while (it.moveToNext()) {
                var sender : String = it.getString(it.getColumnIndex(Telephony.Sms.ADDRESS))
                val messageBody : String = it.getString(it.getColumnIndex(Telephony.Sms.BODY))
                val timeStamp : Long = it.getString(it.getColumnIndex(Telephony.Sms.DATE)).toLong()
                val date = formatDate(timeStamp)

                if (sender[0] == '+') sender = sender.substring(3)

                var contact = sharedPreferences.getString(sender.trim(), "")
                Log.d("HUH", contact.toString())

                if (contact != null && !contact.equals("")) listOfMessages.add(Message(contact.toString(),messageBody,date.toString(),true))
                else listOfMessages.add(Message(sender,messageBody,date,false))
            }
            }
        }

    private fun formatDate(timeStamp: Long): String {
        var dateFormat = SimpleDateFormat("HH:mm dd-MMMM", Locale.US)
        return dateFormat.format(timeStamp)
    }
}

    private fun sendAPIRequest(message: String) : Boolean {
        var ans : Boolean = false

        val apiService = RetrofitClient.apiService
        val call = apiService.predictText(mapOf("user_input" to message))

        call.enqueue(object : Callback<APIResponse> {
            override fun onResponse(call: Call<APIResponse>, response: Response<APIResponse>) {
                Log.d("HEYAAA", response.body()?.prediction.toString())
                if (response.isSuccessful) {
                    val apiResponse = response.body()
                    if (apiResponse != null) {
                        if (apiResponse.prediction.equals("Spam")) {
                            Log.d("CHECKING11", "hiiiii")
                        }
                    }
                } else {
                    Log.e("TAGY", "ERRORRR")
                }
            }

            override fun onFailure(call: Call<APIResponse>, t: Throwable) {
                // API call failed
                Log.d("TAGY","ERRORRRRRRRR")
                // Handle failure
            }
        })
        return ans
    }