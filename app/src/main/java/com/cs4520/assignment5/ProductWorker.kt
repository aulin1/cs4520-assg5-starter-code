package com.cs4520.assignment5

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.UnknownHostException

class ProductWorker(appContext: Context, workerParams: WorkerParameters):
    CoroutineWorker(appContext, workerParams) {

    private fun checkList(p: ArrayList<ProductData>) : ArrayList<ProductData>{
        val result = ArrayList<ProductData>()
        for(item in p){
            var newItem : ProductData
            if(item.type == "Equipment"){
                if(item.name == "" || item.price < 0){ //invalid answers, so we don't add it to the list
                    continue
                } else {
                    if(!isInList(item, result)){ //no repeats
                        result.add(item)
                    }
                }
            } else if(item.type == "Food"){
                if(item.name == "" || item.price <0 || item.expiryDate == null){
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
                return true
            }
        }
        return false
    }

    override suspend fun doWork(): Result {
        //Log.d("Testing Worker", "testing")
        val dao = ProductDatabase.getInstance().productDao()
        var isSuccessful = 0 //if we ever need to know that it fails
        withContext(Dispatchers.IO) {
            try {
                val response = DataRepository().getAllRepository()
                withContext(Dispatchers.Main) {
                    try {
                        if (response.isSuccessful) {
                            val result = response.body()?.let { checkList(it) }

                            if (result != null) {
                                dao.insertAll(result)
                            }
                        } else {
                            isSuccessful = 1 //not successful
                        }
                    } catch (e: Throwable) {
                        isSuccessful = 1 //not successful
                    }
                }
            } catch (e: UnknownHostException) {
                isSuccessful = 1 //not successful
            }
        }

        return Result.success()
    }
}