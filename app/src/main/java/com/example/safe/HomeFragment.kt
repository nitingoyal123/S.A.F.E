package com.example.safe

import android.annotation.SuppressLint
import android.app.Dialog
import android.graphics.Color
import android.icu.text.ListFormatter
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.safe.databinding.FragmentHomeBinding
import com.example.safe.manager.MessageManager
import com.example.safe.model.MessageTable
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
    private lateinit var adapter : HistoryMessageRecyclerViewAdapter
    private lateinit var binding : FragmentHomeBinding

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        Log.d("Progress", "Entered Home fragment")
        Log.d("MessageManager","hello")
        rv = binding.rvMessages
        rv.layoutManager = LinearLayoutManager(requireContext())
        adapter = HistoryMessageRecyclerViewAdapter(requireContext(), MessageManager.listOfMessageTables)
        rv.adapter = adapter

        adapter.onItemClick = { messageTable ->
            rv.isClickable = false
            showMyDialog(messageTable)
        }

        binding.reloadButton.setOnClickListener {
            binding.reloadProgressBar.visibility = View.VISIBLE
            reloadMsgs()
        }

        return binding.root
    }

    private fun reloadMsgs() {
        lifecycleScope.launch {
            MessageManager.loadData(requireContext())
            adapter.notifyDataSetChanged()
            rv.adapter = adapter
            rv.visibility = View.VISIBLE
            binding.reloadProgressBar.visibility = View.INVISIBLE
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
