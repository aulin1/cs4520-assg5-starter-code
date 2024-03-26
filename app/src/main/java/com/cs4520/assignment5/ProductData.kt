package com.cs4520.assignment5

import androidx.room.Entity
import androidx.room.PrimaryKey

sealed class Product(name: String, type: String, expiry: String?, price: Float){

    //types of products
    data class FoodProduct(val name: String, val expiry: String, val price: Float) : Product(name = name, "Food", expiry = expiry, price = price)
    data class EquipmentProduct(val name: String, val price: Float) : Product(name = name, "Equipment", expiry = null, price = price)
}

@Entity
data class ProductData(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val name : String,
    val type : String,
    val expiryDate : String?,
    var price: Float
)