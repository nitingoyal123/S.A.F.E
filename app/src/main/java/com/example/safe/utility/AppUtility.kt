package com.example.safe.utility

import android.util.Log
import com.example.safe.retrofit.APIResponse
import com.example.safe.retrofit.RetrofitClient
import com.google.i18n.phonenumbers.PhoneNumberUtil
import com.google.i18n.phonenumbers.Phonenumber
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


    fun formatPhoneNumber(phoneNumber: String): String {
        val phoneUtil = PhoneNumberUtil.getInstance()
        return try {
            // First, try parsing with the default region
            val defaultRegion = "IN"  // You can change the default region code as per your main user base
            var parsedNumber: Phonenumber.PhoneNumber = phoneUtil.parse(phoneNumber, defaultRegion)

            // Check if the parsed number is valid
            if (!phoneUtil.isValidNumber(parsedNumber)) {
                // If not valid, attempt to parse it without a region, to treat it as an international number
                parsedNumber = phoneUtil.parse(phoneNumber, null)
            }

            // Format the number in E.164 format (with country code)
            phoneUtil.format(parsedNumber, PhoneNumberUtil.PhoneNumberFormat.E164)

        } catch (e: Exception) {
            phoneNumber  // Return the original number if parsing fails
        }
    }


}