package com.cs4520.assignment5

import android.annotation.SuppressLint
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [ProductData::class], version = 1)
abstract class ProductDatabase : RoomDatabase() {
    abstract fun productDao(): ProductDAO

    companion object {
        //According to piazza this is how I do it
        @SuppressLint("StaticFieldLeak")
        private var instance: ProductDatabase? = null
        @SuppressLint("StaticFieldLeak")
        private var context : Context? = null
        @Synchronized
        fun getInstance(): ProductDatabase {
            if(instance == null) {
                instance = Room.databaseBuilder(
                    context!!.applicationContext, ProductDatabase::class.java,
                    "productdata"
                ).allowMainThreadQueries().build()
            }

            return instance!!

        }

        fun setContext(ctx: Context){
            context = ctx
        }
    }
}