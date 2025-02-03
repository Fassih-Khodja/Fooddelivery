package com.example.fastdelivery.Fragments

import android.content.Intent
import android.os.Bundle
import android.transition.TransitionInflater
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.example.fastdelivery.Activities.DataClasses.My_Cart
import com.example.fastdelivery.Models.DataClasses.Cart
import com.example.fastdelivery.Models.DataClasses.Food
import com.example.fastdelivery.ViewModels.FoodDetails_viewmodel
import com.example.fastdelivery.ViewModels.shared_VM_ActNav_FragFoodDetails
import com.example.fastdelivery.databinding.FragmentFoodDetailsChildBinding
import android.app.Activity
import coil.load
import com.example.fastdelivery.Activities.DataClasses.SignUpActivity


class FoodDetails_Child : Fragment() {
    private lateinit var binding:FragmentFoodDetailsChildBinding
    private lateinit var sharedmodel: shared_VM_ActNav_FragFoodDetails
    private val model: FoodDetails_viewmodel by viewModels()
    private  var item_dataclass: Food? =null
    private var quantity:Int=1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       sharedElementEnterTransition = TransitionInflater.from(context).inflateTransition(android.R.transition.move)
        sharedElementReturnTransition = TransitionInflater.from(context).inflateTransition(android.R.transition.move)
        // setting up the viewmodel
        sharedmodel= ViewModelProvider(requireActivity()).get(shared_VM_ActNav_FragFoodDetails::class.java)
        // this is the method that make this viewmodel instance and the instance that i called on the activity would be the same instance
        //if i used by viewModels() they will be different instance, fragment has his own instance and also the activity
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d("blank","the create view")
        binding=FragmentFoodDetailsChildBinding.inflate(inflater, container, false)

        // Set the transition name dynamically based on the position passed
        val position = arguments?.getInt("position")
        item_dataclass = arguments?.getParcelable("dataclass")
        binding.imageFoodDetail.transitionName = "image_$position"
        binding.imageFoodDetail.load(item_dataclass?.imageUrl)
        binding.nameFoodDetails.text=item_dataclass?.name
        binding.totalpriceFoodDetail.text=item_dataclass?.price.toString()


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        // the observe method should be on the onViewCreated
        model.totalprice.observe(viewLifecycleOwner){newtotalprice->
            Log.d("the observe",newtotalprice.toString())
            // when the total price change , change the text of totalprice
            binding.totalpriceFoodDetail.text=newtotalprice.toString()
        }

        // handling the plus button
        binding.plusBtn.setOnClickListener {
            quantity++
            binding.quantitytext.text=quantity.toString()
            item_dataclass?.price?.let { it1 -> model.setuptotalprice(quantity, it1) }
        }

        // handling the minus button
        binding.minusBtn.setOnClickListener {
            if(quantity>1) {
                quantity--
                binding.quantitytext.text=quantity.toString()
                item_dataclass?.price?.let { it1 -> model.setuptotalprice(quantity, it1) }
            }
        }

        // handling the AddtoCart event
        binding.addcartBtn.setOnClickListener {
        // i think this should be handled on the viewmodel
            val cartitem= Cart(name =item_dataclass?.name!!, price = item_dataclass?.price!!,quantity , imageUrl = item_dataclass!!.imageUrl)
            sharedmodel.addcartlist(cartitem)
        }


    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("destroy","the food details get destroyed")
    }
}