package com.example.fastdelivery.ViewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fastdelivery.Models.DataClasses.User
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class Navigation_User_VM: ViewModel() {
    val db = FirebaseFirestore.getInstance()
    val user= Firebase.auth.currentUser

    private val _userInfo = MutableLiveData<User?>()
    val userInfo: LiveData<User?> = _userInfo
    init {
        getUserInformations()
    }

    private fun getUserInformations() {
        user?.uid?.let { userId ->
            viewModelScope.launch {
                try {
                    val snapshot = db.collection("Users").document(userId).get().await()
                    _userInfo.postValue(snapshot.toObject(User::class.java)) // Update LiveData
                    // You can update LiveData or StateFlow to notify UI
                } catch (e: Exception) {
                    e.printStackTrace() // Handle the error
                }
            }
        }
    }
}