package com.example.apptunixsample.model

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "Orders")
data class Orders(
    @PrimaryKey(autoGenerate = true)
    var orderId: Long = 0,
    var quantity: String = "",
    var date: Long = 0, // Change type to Long for Unix timestamp
    var fat: String = "",
    var time: String = ""
)
