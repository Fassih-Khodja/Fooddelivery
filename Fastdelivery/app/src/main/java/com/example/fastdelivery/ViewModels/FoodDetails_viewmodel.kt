package com.example.fastdelivery.ViewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class FoodDetails_viewmodel:ViewModel() {
    val totalprice = MutableLiveData<Int>()
    fun setuptotalprice(quantity:Int,price:Int){
        totalprice.value=quantity*price
    }
}