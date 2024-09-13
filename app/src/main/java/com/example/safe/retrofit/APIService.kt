package com.example.safe.retrofit

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface APIService {
    @POST("predict")
    fun predictText(@Body userInput: Map<String, String>): Call<APIResponse>
}