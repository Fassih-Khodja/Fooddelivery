package com.example.fastdelivery.Fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavOptions
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fastdelivery.Adapters.FavoritAdapter
import com.example.fastdelivery.Adapters.HeartFavoritClickListner
import com.example.fastdelivery.Models.DataClasses.Food
import com.example.fastdelivery.R
import com.example.fastdelivery.ViewModels.Favorit_viewmodel
import com.example.fastdelivery.databinding.FragmentFavoritBinding

class Favorit : Fragment(), HeartFavoritClickListner {
    private lateinit var binding: FragmentFavoritBinding
    private lateinit var recyclerviewfavorite: RecyclerView
    private lateinit var favoriteadapter: FavoritAdapter
    private val model: Favorit_viewmodel by activityViewModels()
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

recyclerviewfavorite=binding.favoriterecyclerview
        favoriteadapter=FavoritAdapter(model.favoritfoodlist.value,this)
        recyclerviewfavorite.adapter=favoriteadapter
       // val spaceItemDecoration = Decoration_Items(5,recyclerviewfavorite)
      //  recyclerviewfavorite.addItemDecoration(spaceItemDecoration)

        recyclerviewfavorite.layoutManager=  GridLayoutManager(context, 2)

        model.favoritfoodlist.observe(viewLifecycleOwner){newfavoritlist->
            Log.d("the observe",newfavoritlist.toString())
            if (newfavoritlist != null) {
                favoriteadapter.updatefavoritfoodlist(newfavoritlist)

            }
        }

    }

    override fun heartclick(item_to_remove:Food) {
        model.removefoodfromfavorit(item_to_remove)
    }

    override fun item_click_listener(imageView: ImageView, position: Int, dataclass_item: Food) {
        val bundle = Bundle().apply {
            putInt("position", position)
            putParcelable("dataclass",dataclass_item) // pass the data class
            putString("source","favoritelist")
        }

        val extras = FragmentNavigatorExtras(imageView to "image_f$position") //should i use this ? this is shared element
        findNavController().navigate(
            R.id.action_favoriteFragment_to_foodDetailsChild,
            bundle,  // Bundle args (if any)
            NavOptions.Builder()
                .setLaunchSingleTop(true)
                .setPopUpTo(R.id.home, inclusive = false)
                .setRestoreState(true)
                .build(),  // NavOptions (if any)
            extras  // Shared element transition
        )
    }

}
