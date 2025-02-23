package com.example.fastdelivery.ViewModels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.fastdelivery.Models.DataClasses.Categorie_food
import com.example.fastdelivery.Models.DataClasses.Food
import com.example.fastdelivery.Models.Repositories.Categorie_repository
import com.example.fastdelivery.Models.Repositories.Food_repository
import kotlinx.coroutines.launch

class Home_view_model (private val CategoryRepo : Categorie_repository,
    private val FoodRepo:Food_repository):ViewModel() {

        init {
            fetchData()
            fetchData2()
        }



    val categorieslist = MutableLiveData<List<Categorie_food>?>()

    var foodlistAll= ArrayList<Food>() // the list this who is not a livedata is the result of the fetch , but not the one who gonna display on the screen
    // the foodlistAll will not change at all after the first fetch
    val foodlist=MutableLiveData<List<Food>?>() // livedata list are the one who gonna display on the screen

    val bestsellerlist= MutableLiveData<List<Food>?>()
    // i can do init there , to fetch data

   // when categorieslist change and because it is a livedata notify the observer object
 /*   fun fetchData() {
        Log.d("fetchData","the fetchData get called")
        CategoryRepo.fetchCategories { data ->
            Log.d("fetchData","the fetchCategories inside the fetchdata get called")
            if (data != null) {
                val mutableList: MutableList<Categorie_food> = data.toMutableList()
                mutableList.add(0, Categorie_food("All"))
                categorieslist.value = mutableList
            }
        }
    }*/


   fun fetchData(){
       viewModelScope.launch { // this create coroutine
           val data=CategoryRepo.fetchCategories().toMutableList()
           data.add(0,Categorie_food("All"))
           categorieslist.value=data
       }
   }

    fun fetchData2() {
        Log.d("fetchData","the fetchData2 get called")
        FoodRepo.fetchFood { data ->
            Log.d("fetchData","the fetchCategories inside the fetchdata2 get called")
            if (data != null) {
                foodlistAll = data as ArrayList<Food>
                createfoodcategory("All")
            }
        }
    }


    fun createfoodcategory(category:String) { // this the function that's filter the food depend on the category selected
        Log.d("foodcategory","the food category get called")
        if (category=="All") foodlist.value =foodlistAll
        else {
            Log.d("foodcategory","the food category are on else ")
           val filteredList = foodlistAll.filter { food -> food.categorie == category }

            // Update the LiveData to notify observers
            foodlist.value = filteredList

        }
        createbestseller()
    }

fun createbestseller(){
     bestsellerlist.value= foodlist.value?.filter { food -> food.bestseller} // i need only this line
}

    class Factory( private val catrepository: Categorie_repository,
                   private val foodrepo:Food_repository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(Home_view_model::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return Home_view_model( catrepository,foodrepo) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
    // the viewmodel only when the fragment getcompletly destroyed (not just the view and not just being on the backstack)
    override fun onCleared() {
        super.onCleared()
        Log.d("YourViewModel", "ViewModel is cleared")
    }

}