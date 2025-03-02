package com.example.fastdelivery.ViewModels

import android.util.Log
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
    fun save_new_info_user(newinfo:User){
        user?.uid?.let { userId ->
            viewModelScope.launch {
                try {
                    Log.d("edit","yu entred into try")
                    db.collection("Users").document(userId)
                        .update(
                            mapOf(
                                "full_name" to newinfo.full_name,
                                "email" to newinfo.email,
                                "phone_number" to newinfo.phone_number,
                                "bio" to newinfo.bio
                            )
                        )
                        .await() // Wait for Firestore update to complete
                    Log.d("edit","you end the edit")

                    _userInfo.postValue(newinfo) // Update LiveData after successful save

                } catch (e: Exception) {
                    e.printStackTrace() // Log the error
                    Log.d("edit","there is exeption on the edit")

                }
            }
        }

    }

}