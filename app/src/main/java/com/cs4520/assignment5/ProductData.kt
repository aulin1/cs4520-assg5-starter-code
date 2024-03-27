package com.cs4520.assignment5

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ProductData(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val name : String,
    val type : String,
    val expiryDate : String?,
    var price: Float
)