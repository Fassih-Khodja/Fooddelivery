package com.example.fastdelivery.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView

import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fastdelivery.Adapters.SearchAdapter
import com.example.fastdelivery.Models.DataClasses.Food
import com.example.fastdelivery.databinding.FragmentSearchBinding

class Search : Fragment() {
private lateinit var binding:FragmentSearchBinding
    private lateinit var recyclerviewsearch: RecyclerView
    private lateinit var searchapater: SearchAdapter
    private lateinit var searchview: SearchView
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
        val l= arrayListOf(
            Food(name = "test1", price = 10, categorie = "Pizza", bestseller = false, imageUrl = ""),
            Food(name = "test2", price = 10, categorie = "Pizza", bestseller = false, imageUrl = "")
            ,
            Food(name = "test33", price = 10, categorie = "Pizza", bestseller = false, imageUrl = ""),
            Food(name = "test3", price = 10, categorie = "Pizza", bestseller = false, imageUrl = ""),
            Food(name = "test34", price = 10, categorie = "Pizza", bestseller = false, imageUrl = ""),
            Food(name = "test4", price = 10, categorie = "Pizza", bestseller = false, imageUrl = "")
            ,
            Food(name = "test14", price = 10, categorie = "Pizza", bestseller = false, imageUrl = ""),
            Food(name = "test41", price = 10, categorie = "Pizza", bestseller = false, imageUrl = "")
            ,
            Food(name = "test56454", price = 10, categorie = "Pizza", bestseller = false, imageUrl = ""),
            Food(name = "test6", price = 10, categorie = "Pizza", bestseller = false, imageUrl = "")
            ,
            Food(name = "test", price = 10, categorie = "Pizza", bestseller = false, imageUrl = "")
        )
        searchview=binding.searchview
        recyclerviewsearch=binding.searchrecyclerview
        searchapater= SearchAdapter(l)
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


}