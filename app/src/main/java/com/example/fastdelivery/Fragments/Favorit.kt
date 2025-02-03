package com.example.fastdelivery.Fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.fastdelivery.R

class Favorit : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favorit, container, false)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d("YourFragmentfavorite", "Fragment view is being destroyed maybe its backstack")
        // Set binding to null to avoid memory leaks
        //  binding = null
    }
    override fun onDestroy() {
        super.onDestroy()
        Log.d("YourFragmentfavorite", "Fragment is being destroyed")
        // At this point, the fragment is being removed from memory
    }
}
