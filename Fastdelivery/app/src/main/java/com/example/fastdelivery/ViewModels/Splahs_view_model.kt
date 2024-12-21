package com.example.fastdelivery.ViewModels


import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class Splahs_view_model:ViewModel() {
    val isUserloging=MutableLiveData<Boolean>()
    init { // runs when instance of this class get called
       checkUser()
    }

    private fun checkUser() {
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                val user= Firebase.auth.currentUser

                if (user!=null){
                    Log.d("current","the user there")
                    withContext(Dispatchers.Main) {
                        isUserloging.value = true
                    }
                }
                    //isUserloging.value=true

                else {
                    withContext(Dispatchers.Main) {
                        isUserloging.value = false
                    }//isUserloging.value=false ;  Log.d("current","the user not there") }
            }
          //  delay(2000) // minimum time of showing the splash screen

       }
    }
}}