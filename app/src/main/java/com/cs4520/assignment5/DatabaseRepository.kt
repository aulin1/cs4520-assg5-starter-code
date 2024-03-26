package com.cs4520.assignment5

class DataRepository {
    private val retrofit = RetrofitClient.getRetrofitInstance()

    suspend fun getAllRepository() = retrofit.getProductList()
}