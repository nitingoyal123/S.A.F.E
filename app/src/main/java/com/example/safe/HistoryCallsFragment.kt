package com.example.safe

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.provider.CallLog
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.safe.model.Call
import java.text.SimpleDateFormat
import java.util.Locale

class HistoryCallsFragment : Fragment() {

    private var PERMISSION_REQUEST_CODE = 1001

    private lateinit var rvCalls : RecyclerView

    lateinit var sharedPreferences : SharedPreferences

    lateinit var editor : SharedPreferences.Editor

    var
        listOfCalls = mutableListOf<Call>()
        @SuppressLint("MissingInflatedId")
        override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
            var view = inflater.inflate(R.layout.fragment_history_calls, container, false)

            sharedPreferences = requireContext().getSharedPreferences("Contacts", Context.MODE_PRIVATE)
            editor = sharedPreferences.edit()

            rvCalls = view.findViewById(R.id.rvCalls)

            if (listOfCalls.isEmpty()) {
                readCalls()

                rvCalls.layoutManager = LinearLayoutManager(requireContext())
                rvCalls.adapter = HistoryCallRecyclerViewAdapter(requireContext(),listOfCalls)
            }
            return view
    }

    private fun readCalls() {

        var cursor = requireContext().contentResolver.query(CallLog.Calls.CONTENT_URI,null,null,null,CallLog.Calls.DATE + " DESC")
        if (cursor!= null && cursor.moveToFirst()) {
            var numberIndex = cursor.getColumnIndex(CallLog.Calls.NUMBER)
            var dateIndex = cursor.getColumnIndex(CallLog.Calls.DATE)
            CallLog.Calls.NUMBER_PRESENTATION
            do {
                var sender = cursor.getString(numberIndex)
                var timeStamp = cursor.getString(dateIndex).toLong()
                var date = formatDate(timeStamp)
                if (sender[0] == '+') sender = sender.substring(3)

                var contact = sharedPreferences.getString(sender.trim(), "")
                Log.d("HUH", contact.toString())

                if (contact != null && !contact.equals("")) listOfCalls.add(Call(contact.toString(),date.toString(),true))
                else listOfCalls.add(Call(sender.toString(),date.toString(),false))
            } while (cursor.moveToNext())
            cursor.close()
        } else {
            TODO()
        }
    }

    override fun onResume() {
        super.onResume()
        if (listOfCalls.isEmpty()) {
            readCalls()
        }
    }

    private fun formatDate(timeStamp: Long): String {
        var dateFormat = SimpleDateFormat("HH:mm dd-MMMM", Locale.US)
        return dateFormat.format(timeStamp)
    }
}