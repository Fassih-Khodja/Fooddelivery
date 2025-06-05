package com.example.fastdelivery.Fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fastdelivery.Adapters.SearchAdapter
import com.example.fastdelivery.Adapters.searchitemclicklistner
import com.example.fastdelivery.Models.DataClasses.Food
import com.example.fastdelivery.Models.Repositories.Categorie_repository
import com.example.fastdelivery.Models.Repositories.Food_repository
import com.example.fastdelivery.R
import com.example.fastdelivery.ViewModels.Home_view_model
import com.example.fastdelivery.databinding.FragmentSearchBinding
import com.google.firebase.firestore.FirebaseFirestore

class Search : Fragment(),searchitemclicklistner {
private lateinit var binding:FragmentSearchBinding
    private lateinit var recyclerviewsearch: RecyclerView
    private lateinit var searchapater: SearchAdapter
    private lateinit var searchview: SearchView
    private lateinit var model: Home_view_model
    val firestore = FirebaseFirestore.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentSearchBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val catrepository = Categorie_repository(firestore)
        val foodrepository= Food_repository(firestore,requireContext().applicationContext)
        model= ViewModelProvider(requireActivity(), Home_view_model.Factory(catrepository,foodrepository)).get(Home_view_model::class.java)
        // using require activity i don't have to fetch again the foodlist

Log.d("test",model.foodlistAll.toString())

        searchview=binding.searchview
        recyclerviewsearch=binding.searchrecyclerview
        searchapater= SearchAdapter(model.foodlistAll,this)
        recyclerviewsearch.adapter=searchapater
        // val spaceItemDecoration = Decoration_Items(5,recyclerviewfavorite)
        //  recyclerviewfavorite.addItemDecoration(spaceItemDecoration)
recyclerviewsearch.layoutManager= LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        searchview.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean { // this is when the user press enter or search
                searchapater.filter.filter(query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean { // this is get called every time the textin the seach view changes
                searchapater.filter.filter(newText)
                return true
            }

        })
    }

    override fun searchonitemclick(position: Int, dataclass_item: Food, imageView: ImageView) {
        val bundle = Bundle().apply {
            putInt("position", position)
            putParcelable("dataclass",dataclass_item) // pass the data class
            putString("source","searchlist")
        }

        val extras = FragmentNavigatorExtras(imageView to "image_s$position") //should i use this ?
        findNavController().navigate(
            R.id.action_searchFragment_to_foodDetailsChild,
            bundle,  // Bundle args (if any)
            null,  // NavOptions (if any)
            extras  // Shared element transition
        )
    }


}