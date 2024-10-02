package com.example.safe.utility

import android.util.Log
import com.example.safe.retrofit.APIResponse
import com.example.safe.retrofit.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.awaitResponse

object AppUtility {

    suspend fun sendAPIRequest(message: String): Boolean {
        return try {
            val apiService = RetrofitClient.apiService
            val response = apiService.predictText(mapOf("user_input" to message)).awaitResponse()

            if (response.isSuccessful) {
                val apiResponse = response.body()
                // Return false if spam is 0, otherwise true
                apiResponse?.spam != 0
            } else {
                Log.e("API_CALL_ERROR", "Failed to get API response")
                false // Default to false if response is not successful
            }
        } catch (e: Exception) {
            Log.e("API_CALL_ERROR", "API call failed", e)
            false // Default to false on failure
        }
    }


}