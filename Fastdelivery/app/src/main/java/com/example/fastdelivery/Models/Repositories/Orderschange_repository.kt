package com.example.fastdelivery.Models.Repositories

import android.util.Log
import com.example.fastdelivery.Fragments.My_Orders
import com.example.fastdelivery.Models.DataClasses.Orders
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class Orderschange_repository(private val db: FirebaseFirestore,private val IDuser:String) {
    suspend fun addorder(neworder:Orders){
        return withContext(Dispatchers.IO){
            try {
                db.collection("Users")
                    .document(IDuser).collection("My_Orders").add(neworder)
                    .await()  // Suspends until Firestore task completes

            } catch (e: Exception) {
                Log.w("TAG", "Error writing document", e)
            }

        }
    }
}