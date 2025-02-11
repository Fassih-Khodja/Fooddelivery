package com.example.fastdelivery.Fragments

import android.os.Bundle
import android.transition.TransitionInflater
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import coil.load
import com.example.fastdelivery.Models.DataClasses.Cart
import com.example.fastdelivery.Models.DataClasses.Food
import com.example.fastdelivery.R
import com.example.fastdelivery.ViewModels.Favorit_viewmodel
import com.example.fastdelivery.ViewModels.FoodDetails_viewmodel
import com.example.fastdelivery.ViewModels.shared_VM_ActNav_FragFoodDetails
import com.example.fastdelivery.databinding.FragmentFoodDetailsChildBinding


class FoodDetails_Child : Fragment() {
    private lateinit var binding:FragmentFoodDetailsChildBinding
    private lateinit var sharedmodel: shared_VM_ActNav_FragFoodDetails
    private val model: FoodDetails_viewmodel by viewModels()
    private val modelfavorit: Favorit_viewmodel by activityViewModels()

    private  var item_dataclass: Food? =null
    private var quantity:Int=1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       sharedElementEnterTransition = TransitionInflater.from(requireContext()).inflateTransition(android.R.transition.move)
        sharedElementReturnTransition = TransitionInflater.from(requireContext()).inflateTransition(android.R.transition.move)

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
        val source=arguments?.getString("source")
        item_dataclass = arguments?.getParcelable("dataclass")

        item_dataclass?.let {
            if(    modelfavorit.isfoodfavorit(it)){
                binding.favoriteBtn.setImageResource(R.drawable.favorite_btn_icon)
                binding.favoriteBtn.tag = R.drawable.favorite_btn_icon}
        }
        if ( source=="foodlist"){
        binding.imageFoodDetail.transitionName = "image_$position"}
        else if(source=="bestseller") {
            binding.imageFoodDetail.transitionName = "image_b$position"
        }else {
            binding.imageFoodDetail.transitionName = "image_s$position"
        }
        binding.imageFoodDetail.load(item_dataclass?.imageUrl)
        binding.nameFoodDetails.text=item_dataclass?.name
        binding.totalpriceFoodDetail.text=item_dataclass?.price.toString()


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.favoriteBtn.setOnClickListener {
            // Get the current drawable resource ID
            val currentImage = binding.favoriteBtn.tag as? Int ?: R.drawable.favorite_btn_icon_notchecked
Log.d("curentimage",currentImage.toString())
            // Toggle between heart filled and heart empty
            val newImage = if (currentImage == R.drawable.favorite_btn_icon_notchecked) { // here it will take the last line of code inside the if-
                item_dataclass?.let { it1 -> modelfavorit.addfoodfavorit(it1) }
                Log.d("the after addfood", "im here")

                R.drawable.favorite_btn_icon
            } else {
                item_dataclass?.let { it1 -> modelfavorit.removefoodfromfavorit(it1) }
                Log.d("the after removefood", "im here")

                R.drawable.favorite_btn_icon_notchecked
            }
            Log.d("newimage",newImage.toString())
            // Update the button image

            binding.favoriteBtn.animate()
                .scaleX(1.3f)  // Scale up
                .scaleY(1.3f)
                .setDuration(150)
                .withEndAction {
                    Log.d("animate","the setimage is called")
                    binding.favoriteBtn.setImageResource(newImage) // Change image after scaling up

                    binding.favoriteBtn.animate()
                        .scaleX(1f)  // Scale back to normal
                        .scaleY(1f)
                        .setDuration(150)
                        .start()
                }.start()

            // Store the current state using tag
            // the tag is used just to store the state
            binding.favoriteBtn.tag = newImage
        }






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
    }
}