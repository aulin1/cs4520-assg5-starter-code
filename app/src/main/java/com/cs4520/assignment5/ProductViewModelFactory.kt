package com.cs4520.assignment5

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider


class ProductViewModelFactory() : ViewModelProvider.AndroidViewModelFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProductViewModel::class.java)){
            return ProductViewModel() as T
        }
        throw IllegalArgumentException("Creating ViewModel failed")
    }
}