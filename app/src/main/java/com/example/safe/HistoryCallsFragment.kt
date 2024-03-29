package com.example.safe

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.CallLog
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.recyclerview.widget.RecyclerView
import com.example.safe.model.Call

class HistoryCallsFragment : Fragment() {

    private var PERMISSION_REQUEST_CODE = 1001

    private lateinit var rvCalls : RecyclerView
    var listOfCalls = mutableListOf<Call>()
    @SuppressLint("MissingInflatedId")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view = inflater.inflate(R.layout.fragment_history_calls, container, false)



        if (checkSelfPermission(requireContext(),Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED){
            requestPermissions(arrayOf(Manifest.permission.READ_CALL_LOG),PERMISSION_REQUEST_CODE)
        }

        readCalls()

        rvCalls = view.findViewById(R.id.rvCalls)

        rvCalls.layoutManager = LinearLayoutManager(requireContext())
        rvCalls.adapter = HistoryCallRecyclerViewAdapter(requireContext(),listOfCalls)

        return view

    }

    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_CODE && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            readCalls()
    }

    private fun readCalls() {

        var cursor = requireContext().contentResolver.query(CallLog.Calls.CONTENT_URI,null,null,null,CallLog.Calls.DATE + " DESC")
        if (cursor!= null && cursor.moveToFirst()) {
            var numberIndex = cursor.getColumnIndex(CallLog.Calls.NUMBER)
            var dateIndex = cursor.getColumnIndex(CallLog.Calls.DATE)
            CallLog.Calls.NUMBER_PRESENTATION
            do {
                var number = cursor.getString(numberIndex)
                var date = cursor.getString(dateIndex)
                listOfCalls.add(Call(number, date, true))
            } while (cursor.moveToNext())
            cursor.close()
        } else {
            TODO()
        }


    }
}