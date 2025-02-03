package com.example.fastdelivery.Models.Repositories

import android.util.Log
import com.example.fastdelivery.Models.DataClasses.Orders
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class OrdersFetch_repository(private val db:FirebaseFirestore,private val IDuser:String) {
    suspend fun fetchorders():List<Orders>{ // asuspend function must get called from a coroutine; not on the main thread.
         return withContext(Dispatchers.IO){ // Dispatchers introuce the thread where the code inside coroutines eexecuted
             // coroutines allow us to run some code on another thread than the main thread
             // withcontext : is the one that decide on which thread the code should executed
             // and i can switch the context inside the code , for example i start with IO context , and switch to Main by : withcontext(Dispatchers.Main)
             // even if i used coroutines on the main thread , it will not block the main thread (asynchronisation)
             // so you can call the Dispatchers.IO here on the , coroutines , "which is on the MyOrders_view_model"
             // the coroutines is the one who create the new thread and not withContext, so if used two of this function on the same coroutines , it will executed on the same thread , so sequential
             
             try {
                 Log.d("orders","it got entered")
                 val collection= db.collection("Users").document(IDuser).collection("My_Orders").get().await()
                 val orderslist= collection.documents.mapNotNull {  document ->
                     Log.d("orders","it got entered2")
                     document.toObject(Orders::class.java)
                 }
                 Log.d("orders","dun orders get good")
                 orderslist
             }catch (e: Exception) {
                 Log.d("orders",e.toString())
                 emptyList()
             }

         }
    }

}