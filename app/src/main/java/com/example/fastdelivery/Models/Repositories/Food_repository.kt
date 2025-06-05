package com.example.fastdelivery.Models.Repositories

import android.content.Context
import android.util.Log
import com.example.fastdelivery.DAO.AppDataBase
import com.example.fastdelivery.DAO.FoodDAO
import com.example.fastdelivery.Models.DataClasses.Food
import com.google.firebase.firestore.FirebaseFirestore

class Food_repository (private val db: FirebaseFirestore, private val ctx:Context) {
    private val foodDao: FoodDAO = AppDataBase.getDatabase(ctx).foodDao()

    // the repository is just for the intruction with the database , the filter is doing on the viewmodel
    fun fetchFood(callback: (List<Food>?) -> Unit) {
        Log.d("fetchCategories","the fetchFood get called")
        // the needen of the callback
        //Go ahead and do whatever you need to do. When the data is ready, I’ll let you know what to do next
        // so without the calback , the app will freeze until the fetch is completed
        db.collection("Food")
            .get()
            .addOnSuccessListener { documents ->
                Log.d("fetchCategories","the fetchFood succefull")
                Log.d("test firestore s", documents.toString())
                val dataList = documents.mapNotNull { document ->
                    Log.d("test firestore ", documents.toString())
                    document.toObject(Food::class.java)
                }
                callback(dataList)
            }
            .addOnFailureListener {
                Log.d("fetchCategories","the fetchFood faillure")
                callback(null)
            }
    }

     fun insertFoods(foods: List<Food>) {
        foodDao.insertallthelist(foods)
        Log.d("insertfood","the insert done")
    }


}