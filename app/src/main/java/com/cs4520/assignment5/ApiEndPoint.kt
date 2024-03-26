package com.cs4520.assignment5

import retrofit2.Response
import retrofit2.http.GET

interface ApiEndPoint {
    @GET(Api.ENDPOINT)
    //fun getProductList(@Query("q") q : String) : Call<ResponseModel>
    suspend fun getProductList() : Response<ArrayList<ProductData>>
}