package com.cs4520.assignment5

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface ProductDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(product: ProductData)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(products: List<ProductData>)

    @Update
    fun update(product: ProductData)

    @Delete
    fun delete(product: ProductData)

    @Query("SELECT * FROM productdata")
    fun getProducts(): List<ProductData>
}