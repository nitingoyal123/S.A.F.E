package com.example.safe.messageInROOM

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "messages")
data class MessageTable(

    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var phoneNumber : String = "",
    var message : String = "",
    var date : String = "",
    var spam : Boolean = false

)
