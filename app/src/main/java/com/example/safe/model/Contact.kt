package com.example.safe.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "contacts")
data class Contact (
    @PrimaryKey
    var phoneNumber : String,
    var name : String,
    var blocked : Boolean
)
