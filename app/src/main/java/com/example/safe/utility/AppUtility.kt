package com.example.safe.utility

import com.example.safe.messageInROOM.MessageTable
import com.example.safe.model.Call
import com.example.safe.retrofit.APIResponse
import com.example.safe.retrofit.RetrofitClient
import retrofit2.Callback
import retrofit2.Response

object AppUtility {

    fun sendAPIRequest(message : String, callback: (Boolean) -> Unit) {
        val apiService = RetrofitClient.apiService
        val call = apiService.predictText(mapOf("user_input" to message))

        call.enqueue(object : Callback<APIResponse> {
            override fun onResponse(
                call: retrofit2.Call<APIResponse>,
                response: Response<APIResponse>,
            ) {
                TODO("Not yet implemented")
            }

            override fun onFailure(call: retrofit2.Call<APIResponse>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })

    }

}