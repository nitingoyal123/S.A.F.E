package com.example.safe

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.database.Cursor
import android.os.Bundle
import android.provider.Telephony
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.safe.model.Message
import com.example.safe.retrofit.APIResponse
import com.example.safe.retrofit.RetrofitClient
import com.example.safe.retrofit.UserInputRequest
import kotlinx.coroutines.Dispatchers
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.Executors

class HistoryMessagesFragment : Fragment() {

    private lateinit var rv : RecyclerView

    var listOfMessages = mutableListOf<Message>()
    private val PERMISSION_REQUEST_CODE = 1001

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        if (checkSelfPermission(requireContext(),Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(Manifest.permission.READ_SMS), PERMISSION_REQUEST_CODE)
        } else {

            readMessages()
        }

        val view = inflater.inflate(R.layout.fragment_history_messages, container, false)

        rv = view.findViewById(R.id.rvMessages)

        rv.layoutManager = LinearLayoutManager(requireContext())
        rv.adapter = HistoryMessageRecyclerViewAdapter(requireContext(),listOfMessages)

        return view
    }

    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_CODE && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            readMessages()
        }
    }

    private fun readMessages() {
        val cursor : Cursor? = requireContext().contentResolver.query(Telephony.Sms.CONTENT_URI, null, null, null, null)
        cursor?.use {
            while (it.moveToNext()) {
                val sender : String = it.getString(it.getColumnIndex(Telephony.Sms.ADDRESS))
                val messageBody : String = it.getString(it.getColumnIndex(Telephony.Sms.BODY))
                val date : String = it.getString(it.getColumnIndex(Telephony.Sms.DATE))

                var executor = Executors.newSingleThreadExecutor();
                executor.execute( Runnable{
                    sendAPIRequest(messageBody, sender, date)
                })
            }
        }
    }

    private fun sendAPIRequest(message: String,sender : String, date : String) {

        val apiService = RetrofitClient.apiService
        val call = apiService.predictText(mapOf("user_input" to message))

        call.enqueue(object : Callback<APIResponse> {
            override fun onResponse(call: Call<APIResponse>, response: Response<APIResponse>) {
                Log.d("HEYAAA", response.body()?.prediction.toString())
                if (response.isSuccessful) {
                    val apiResponse = response.body()
                    if (apiResponse != null) {
                        if (apiResponse.prediction.equals("Spam")) {
                            listOfMessages.add(Message(sender.toString(),message.toString(),date.toString(),true))
                        } else {
                            listOfMessages.add(Message(sender.toString(),message.toString(),date.toString(),false))
                        }
                    }
                } else {
                    Log.e("TAGY","ERRORRR")
                    Toast.makeText(requireContext(),"Something went wrong, Try again later !",Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<APIResponse>, t: Throwable) {
                // API call failed
                Log.d("TAGY","ERRORRRRRRRR")
                // Handle failure
            }
        })
    }

}