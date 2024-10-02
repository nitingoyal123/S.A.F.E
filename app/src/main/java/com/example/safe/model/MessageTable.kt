package com.example.safe.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "messages")
data class MessageTable(
    @PrimaryKey
    var timeStamp : Long,
    var phoneNumber : String = "",
    var message : String = "",
    var date : String = "",
    var spam : Boolean = false
)
