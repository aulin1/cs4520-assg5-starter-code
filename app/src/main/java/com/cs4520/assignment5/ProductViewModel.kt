package com.cs4520.assignment5

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.UnknownHostException

class ProductViewModel() : ViewModel() {
    private val _ResponseData = mutableStateListOf<ProductData>()
    val ResponseData = _ResponseData.toMutableStateList()
    private val repository = DataRepository()

    private val database = ProductDatabase.getInstance()

    init {
        makeApiCall()
    }


    private fun makeApiCall(input: String?=null) {
        val dao = database.productDao()

        viewModelScope.launch(Dispatchers.IO) {
            try{
                val response = repository.getAllRepository() //TA says that crashing here if nothing is loaded is ok
                withContext(Dispatchers.Main) {
                    try {
                        if (response.isSuccessful) {
                            _ResponseData.clear()
                            response.body()?.let { _ResponseData.addAll(it) }

                            response.body()?.let { dao.insertAll(it) }
                        } else {
                            _ResponseData.clear()
                        }
                    } catch (e: Throwable) {
                        _ResponseData.clear()
                    }
                }
            } catch (e : UnknownHostException){ //access database
                withContext(Dispatchers.Main){
                    val databaseEntries = dao.getProducts()
                    if (databaseEntries.isEmpty()) {
                        _ResponseData.clear()
                    } else {
                        val newData = ArrayList<ProductData>()
                        for(item in databaseEntries){
                            newData.add(item)
                        }
                        _ResponseData.addAll(newData)
                    }
                }
            }
        }
    }
}