package com.example.fastdelivery.ViewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fastdelivery.Models.DataClasses.Cart

class shared_VM_ActNav_FragFoodDetails: ViewModel() {
    // i think i will make it normal list and not livedata , cuz in my case there is no need for the livedtaa features
    // ican use livedata for the fragment fooddetails to change the price total
    val cartlist = MutableLiveData<List<Cart>?>()

    fun addcartlist(itemCart: Cart){
        val currentList = cartlist.value ?: emptyList()  // (LiveData is immutable)
        if ( currentList.isEmpty()){
            cartlist.value = currentList + itemCart
        } else{
        val existingItemIndex = currentList.indexOfFirst { it.name == itemCart.name }
            if (existingItemIndex !=-1) {
                val updatedItem = currentList[existingItemIndex].copy(quantity = currentList[existingItemIndex].quantity + itemCart.quantity)
                val updatedList = currentList.toMutableList().apply {
                    this[existingItemIndex] = updatedItem
                }
                cartlist.value=updatedList
            } else {
                cartlist.value = currentList + itemCart
            }
        }


    }
    fun checkduplicateitems(itemCart: Cart){
  // here i can minimize the addcartlist by searching first and if there is i will just change the cartlist without calling the fun addcartlist
        //or you know what , i thing it's the same thing

    }

}