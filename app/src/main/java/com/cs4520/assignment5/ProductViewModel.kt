package com.cs4520.assignment5

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.UnknownHostException

class ProductViewModel() : ViewModel() {
    private val _ResponseData = MutableLiveData<ArrayList<ProductData>>()
    val ResponseData : LiveData<ArrayList<ProductData>> = _ResponseData
    private val repository = DataRepository()
    private var _progress = MutableLiveData<Int>()
    val progress : LiveData<Int>  = _progress //0- processing, 1 - success, 2- fail

    private val database = ProductDatabase.getInstance()

    init {
        Log.d("Testing", "init viewmodel")
        _progress.value = 0
        makeApiCall()
    }


    private fun checkList(p: ArrayList<ProductData>) : ArrayList<ProductData>{
        val result = ArrayList<ProductData>()
        for(item in p){
            var newItem : ProductData
            if(item.type == "Equipment"){
                if(item.name == "" || item.price < 0){ //invalid answers, so we don't add it to the list
                    Log.d("Testing", "Invalid item equipment")
                    continue
                } else {
                    if(!isInList(item, result)){ //no repeats
                        result.add(item)
                    }
                }
            } else if(item.type == "Food"){
                if(item.name == "" || item.price <0 || item.expiryDate == null){
                    Log.d("Testing", "Invalid item food")
                    continue
                } else {
                    if(!isInList(item, result)){ //no repeats
                        result.add(item)
                    }
                }
            } else { //It isn't a valid type, so we don't add it to the list
                continue
            }
        }
        return result
    }

    private fun isInList(item: ProductData, list: ArrayList<ProductData>) : Boolean{
        for(i in list){
            if(i == item){
                Log.d("Testing", "Repeat Data")
                Log.d("Testing", item.name)
                return true
            }
        }
        return false
    }

    private fun makeApiCall(input: String?=null) {
        val dao = database.productDao()

        viewModelScope.launch(Dispatchers.IO) {
            try{
                val response = repository.getAllRepository() //TA says that crashing here if nothing is loaded is ok
                withContext(Dispatchers.Main) {
                    try {
                        if (response.isSuccessful) {
                            _progress.value = 1
                            Log.d("Testing", "Success")

                            _ResponseData.value = response.body()?.let { checkList(it) }

                            _ResponseData.value?.let { dao.insertAll(it) }
                        } else {
                            _progress.value = 1
                            _ResponseData.value?.clear()
                        }
                    } catch (e: Throwable) {
                        _progress.value = 2
                        Log.d("Testing", "Fail")
                        _ResponseData.value?.clear()
                    }
                }
            } catch (e : UnknownHostException){ //access database
                withContext(Dispatchers.Main){
                    _progress.value = 1
                    val databaseEntries = dao.getProducts()
                    if (databaseEntries.isEmpty()) {
                        _ResponseData.value?.clear()
                    } else {
                        val newData = ArrayList<ProductData>()
                        for(item in databaseEntries){
                            newData.add(item)
                        }
                        _ResponseData.value = newData
                    }
                }
            }
        }
    }
}