package com.example.fastdelivery.ViewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.fastdelivery.Models.DataClasses.Orders
import com.example.fastdelivery.Models.Repositories.OrdersFetch_repository
import com.example.fastdelivery.Models.Repositories.Orderschange_repository
import kotlinx.coroutines.launch

class MyOrders_view_model(private val ordersrepo:OrdersFetch_repository,private val orderschangerepo:Orderschange_repository):ViewModel() {

val listorders=MutableLiveData<List<Orders>?>()
    init {
        fetchDataOrders() // this has to be on init
    }
   private fun fetchDataOrders(){
       // the viewmodelscope is to ensure that the lifecycle ofthe coroutine is the same as the viewmodel , when it destroyed the coroutine gonna cancelled
        viewModelScope.launch { // this create coroutine
            val data=ordersrepo.fetchorders().toMutableList()
            listorders.value=data
        }
    }
     fun AddOrder(neworder:Orders){
        viewModelScope.launch { // this create coroutine
            orderschangerepo.addorder(neworder)
            val data = listorders.value?.toMutableList() ?: mutableListOf()
            data.add(neworder)
            listorders.value = data
        }
    }

    class Factory(private val ordersrepo:OrdersFetch_repository,private val orderschangerepo:Orderschange_repository ) : ViewModelProvider.Factory { // to pass arguments
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MyOrders_view_model::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return MyOrders_view_model( ordersrepo,orderschangerepo) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
