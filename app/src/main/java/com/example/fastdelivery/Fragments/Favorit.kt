package com.example.fastdelivery.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fastdelivery.Adapters.FavoritAdapter
import com.example.fastdelivery.Models.DataClasses.Food
import com.example.fastdelivery.databinding.FragmentFavoritBinding

class Favorit : Fragment() {
    private lateinit var binding: FragmentFavoritBinding
    private lateinit var recyclerviewfavorite: RecyclerView
    private lateinit var favoriteadapter: FavoritAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentFavoritBinding.inflate(inflater, container, false)

        return binding.root

    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val l= arrayListOf(Food(name = "test", price = 10, categorie = "Pizza", bestseller = false, imageUrl = ""),Food(name = "test", price = 10, categorie = "Pizza", bestseller = false, imageUrl = "")
        ,Food(name = "test", price = 10, categorie = "Pizza", bestseller = false, imageUrl = ""),Food(name = "test", price = 10, categorie = "Pizza", bestseller = false, imageUrl = ""),Food(name = "test", price = 10, categorie = "Pizza", bestseller = false, imageUrl = ""),Food(name = "test", price = 10, categorie = "Pizza", bestseller = false, imageUrl = "")
        ,Food(name = "test", price = 10, categorie = "Pizza", bestseller = false, imageUrl = ""),Food(name = "test", price = 10, categorie = "Pizza", bestseller = false, imageUrl = "")
        ,Food(name = "test", price = 10, categorie = "Pizza", bestseller = false, imageUrl = ""),Food(name = "test", price = 10, categorie = "Pizza", bestseller = false, imageUrl = "")
        ,Food(name = "test", price = 10, categorie = "Pizza", bestseller = false, imageUrl = ""))
recyclerviewfavorite=binding.favoriterecyclerview
        favoriteadapter=FavoritAdapter(l)
        recyclerviewfavorite.adapter=favoriteadapter
       // val spaceItemDecoration = Decoration_Items(5,recyclerviewfavorite)
      //  recyclerviewfavorite.addItemDecoration(spaceItemDecoration)

        recyclerviewfavorite.layoutManager=  GridLayoutManager(context, 2)
    }

    }
