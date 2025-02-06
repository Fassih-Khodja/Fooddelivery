package com.example.fastdelivery.ViewModels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fastdelivery.Models.DataClasses.Food
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class Favorit_viewmodel :ViewModel() {
val db =FirebaseFirestore.getInstance()
    val user= Firebase.auth.currentUser

    val favoritfoodlist= MutableLiveData<List<Food>?>()
    init {
        fetchfavoritfood()
    }


    fun isfoodfavorit(newfood: Food):Boolean{

        return favoritfoodlist.value?.any { it.name == newfood.name } == true
    }
     fun addfoodfavorit(newfood:Food){
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                Log.d("favorite","the add for favorite food is called")
                val collection=
                    user?.uid?.let { db.collection("Users").document(it).collection("Favorite") }
                if (collection != null) {
                    try {
                        val documentReference = collection.add(newfood).await()
                        val currentList = favoritfoodlist.value.orEmpty().toMutableList()
                        currentList.add(newfood) // Add the new item
                        favoritfoodlist.postValue(currentList)

                        Log.d("Firestore", "Food added with ID: ${documentReference.id}")
                    } catch (e: Exception) {
                        Log.e("Firestore", "Error adding food", e)
                    }
                }
            }

        }
    }

    fun removefoodfromfavorit(newfood: Food){
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                Log.d("favorite","the add for favorite food is called")
                val collection=
                    user?.uid?.let { db.collection("Users").document(it).collection("Favorite") }
                if (collection != null) {
                    try {
                        val querySnapshot = collection.whereEqualTo("name", newfood.name).get().await() // it's faster and better to use the id
                        // so maybe i need to store the id
                        val document = querySnapshot.documents.firstOrNull()
                        document?.reference?.delete()?.await()
                        val currentList = favoritfoodlist.value.orEmpty().toMutableList()
                        val updatedList = currentList.filterNot { it.name== newfood.name }
                        favoritfoodlist.postValue(updatedList)
                    } catch (e: Exception) {
                        Log.e("Firestore", "Error adding food", e)
                    }
                }
            }

        }

    }
    private fun fetchfavoritfood() {
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                Log.d("favorite","the fetch for favorite food is called")
                val collection=
                    user?.uid?.let { db.collection("Users").document(it).collection("Favorite").get().await() }
                val favoritelist= collection?.documents?.mapNotNull { document ->
                    Log.d("orders","it got entered2")
                    document.toObject(Food::class.java)
                }
                // Switch to the main thread before updating LiveData
                withContext(Dispatchers.Main) {
                    if (favoritelist != null) {
                        favoritfoodlist.value = favoritelist
                    }
                }}

        }
    }
}