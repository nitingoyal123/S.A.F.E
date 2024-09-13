package com.example.safe.retrofit

data class APIResponse(
    var prediction : String,
    var score : String,
    var spam : Int
)
