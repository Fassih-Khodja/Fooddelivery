package com.example.fastdelivery.Models.Repositories


import com.example.fastdelivery.Models.DataClasses.Categorie_food
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class Categorie_repository(private val firestore:FirebaseFirestore) {
   // private val firestore = FirebaseFirestore.getInstance() // instance of the firestore , i will think later to make it as an argument of the class
  /*  fun fetchCategories(callback: (List<Categorie_food>?) -> Unit) {
        Log.d("fetchCategories","the fetchCategories get called")
        // the needen of the callback
        //Go ahead and do whatever you need to do. When the data is ready, I’ll let you know what to do next
        // so without the calback , the app will freeze until the fetch is completed
        firestore.collection("Categories_food")
            .get()
            .addOnSuccessListener { documents ->
                Log.d("fetchCategories","the fetchCategoriesis succefull")
                Log.d("test firestore s", documents.toString())
                val dataList = documents.mapNotNull { document ->
                    Log.d("test firestore ", documents.toString())
                    document.toObject(Categorie_food::class.java)
                }
                callback(dataList)
            }
            .addOnFailureListener {
                Log.d("fetchCategories","the fetchCategories faillure")
                callback(null)
            }
    } */
    suspend fun fetchCategories():List<Categorie_food>{
        return  withContext(Dispatchers.IO) {
  try {
    val collection= firestore.collection("Categories_food").get().await()
      //If successful, await() returns the QuerySnapshot, and the coroutine resumes.
    val categoireslist= collection.documents.mapNotNull {  document ->
        document.toObject(Categorie_food::class.java)
    }
    categoireslist


} catch (e: Exception) {
//If there’s an error, the coroutine will throw an exception, which can be handled appropriately (e.g., using try-catch).
     emptyList()
}}

    }


}