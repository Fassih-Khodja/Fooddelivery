package com.example.fastdelivery.Fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavOptions
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.example.fastdelivery.Adapters.CategoriesAdapter
import com.example.fastdelivery.Adapters.FoodAdapter
import com.example.fastdelivery.Adapters.FoodonItemClickListner
import com.example.fastdelivery.Adapters.OffersAdapter
import com.example.fastdelivery.Adapters.bestsellerAdapter
import com.example.fastdelivery.Adapters.bestselleronitemclicklistner
import com.example.fastdelivery.Adapters.onItemClickListner
import com.example.fastdelivery.Decorations.Decoration_Items
import com.example.fastdelivery.Models.DataClasses.Food
import com.example.fastdelivery.Models.Repositories.Categorie_repository
import com.example.fastdelivery.Models.Repositories.Food_repository
import com.example.fastdelivery.R
import com.example.fastdelivery.ViewModels.Home_view_model
import com.example.fastdelivery.databinding.FragmentHomeFragmentBinding
import com.google.firebase.firestore.FirebaseFirestore
import kotlin.math.abs


class Home_fragment : Fragment(), onItemClickListner,FoodonItemClickListner,bestselleronitemclicklistner {

    private lateinit var binding: FragmentHomeFragmentBinding
    private lateinit var model: Home_view_model
    private lateinit var offersviewpager: ViewPager2
    private lateinit var recyclerviewcategories: RecyclerView
    private lateinit var recyclerviewbestseller: RecyclerView
    private lateinit var recyclerviewfoods: RecyclerView
    private lateinit var offersAdapter: OffersAdapter
    private lateinit var categoriesAdapter: CategoriesAdapter
    private lateinit var BestsellerAdapter: bestsellerAdapter
    private lateinit var foodAdapter: FoodAdapter
     val firestore = FirebaseFirestore.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("test fragment","the fragment has been created  the last test ")

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d("test fragment","the fragment has been created view.. ")
        // Inflate the layout for this fragment
       // val view = inflater.inflate(R.layout.fragment_home_fragment, container, false)

        binding = FragmentHomeFragmentBinding.inflate(inflater, container, false)

        return binding.root
    }
// this is the main one 
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    Log.d("test fragment","the fragment has been created view 2.. ")
    // initialize of repositories
    val catrepository = Categorie_repository(firestore)
    val foodrepository=Food_repository(firestore)





// initialise of viewmodel with argument repository
    model= ViewModelProvider(requireActivity(), Home_view_model.Factory(catrepository,foodrepository)).get(Home_view_model::class.java)




// setting the recyclerviewcatgeories adapter
    recyclerviewcategories=binding.recyclerviewcategories

    categoriesAdapter=CategoriesAdapter(emptyList(),model, this)

    recyclerviewcategories.adapter=categoriesAdapter
    val spaceItemDecoration = Decoration_Items(50,recyclerviewcategories)
    recyclerviewcategories.addItemDecoration(spaceItemDecoration)
    recyclerviewcategories.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
// the observer
    // LiveData notifies Observer objects when underlying data changes
    model.categorieslist.observe(viewLifecycleOwner) { categories ->
        // every time the categorieslist changed this get called to change the ui component (in our case the recyclerview)
        // the observer doesn't work alone he work with livedata
        if (categories != null) {
            Log.d("null","the categories are not null")

            categoriesAdapter.updateCategories(categories)
            // Handle the categories data, e.g., update a RecyclerView adapter
        } else             Log.d("null","the categories are null")

    }







    recyclerviewfoods=binding.recyclerviewfood
    val spaceItemDecoration3= Decoration_Items(100,recyclerviewfoods)
    recyclerviewfoods.addItemDecoration(spaceItemDecoration3)
    foodAdapter=FoodAdapter(emptyList(),this)
    recyclerviewfoods.adapter=foodAdapter
    recyclerviewfoods.layoutManager=LinearLayoutManager(context)

    model.foodlist.observe(viewLifecycleOwner) { foods ->
        // every time the foodlist changed this get called to change the ui component (in our case the recyclerview)
        // the observer doesn't work alone he work with livedata
        if (foods != null) {
            Log.d("null","the food are not null")

            foodAdapter.updatefoods(foods)
            // Handle the categories data, e.g., update a RecyclerView adapter
        } else             Log.d("null","the food are null")

    }





    recyclerviewbestseller=binding.recyclerviewbestseller
    val spaceItemDecoration2= Decoration_Items(50,recyclerviewbestseller)
    recyclerviewbestseller.addItemDecoration(spaceItemDecoration2)
    BestsellerAdapter=bestsellerAdapter(emptyList(),this)
    recyclerviewbestseller.adapter=BestsellerAdapter
    recyclerviewbestseller.layoutManager=LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)


    model.bestsellerlist.observe(viewLifecycleOwner) { bestsellers ->
        // every time the categorieslist changed this get called to change the ui component (in our case the recyclerview)
        // the observer doesn't work alone he work with livedata
        if (bestsellers != null) {
            Log.d("null","the food are not null")

            BestsellerAdapter.updatebestsellers(bestsellers)
            // Handle the categories data, e.g., update a RecyclerView adapter
        } else             Log.d("null","the food are null")

    }


    if (model.categorieslist.value == null) { // i can this without condition on the onCreateView
        Log.d("fetchData","the fetchData is called ")
        model.fetchData() // Only fetch data if it's not already available
    }

    if (model.foodlist.value == null) { // i can this without condition on the onCreateView
        Log.d("fetchData2 ","the fetchData2 is called ")
        model.fetchData2() // Only fetch data if it's not already available
    }





    offersviewpager = binding.viewpagerOffers
    val  l= arrayListOf("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRHu8WKxBAlrepqxs4HLCeCdm9CrbKqcGLBBA&s" ,"https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRCSA-8-qhW3ZjUdCNGrJM_w5_A31jneNdFTA&s","https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcR4hZTIC2f6w9zD8xOX1jIwR_F07rBKNADLaA&s")
    offersAdapter=OffersAdapter(l)
    offersviewpager.adapter = offersAdapter
    offersviewpager.clipToPadding=false
    offersviewpager.clipChildren=false
    offersviewpager.offscreenPageLimit=3
    // offersviewpager.getChildAt(0).overScrollMode=RecyclerView.OVER_SCROLL_NEVER

    val compose= CompositePageTransformer()
    compose.addTransformer(MarginPageTransformer(5))
    compose.addTransformer { page, position ->
        val r:Float=1- abs(position)
        page.scaleY=0.85f+ r *0.14f

    }
    offersviewpager.setPageTransformer(compose)

    /*
      Log.d("test fragment","the recyclerview of categories is called again ")

       */



// testing adding a child fragment
   /* val transaction=childFragmentManager.beginTransaction()// ->
    //Child FragmentManager: Manages nested fragments within another fragment, allowing complex hierarchies.
    transaction.add(R.id.fragment_container_view, FoodDetails_Child())

    transaction.addToBackStack(null)*/



    }


    override fun onDestroyView() {
        super.onDestroyView()
        Log.d("YourFragment", "Fragment view is being destroyed maybe its backstack")
        // Set binding to null to avoid memory leaks
      //  binding = null
    }
    override fun onDestroy() {
        super.onDestroy()
        Log.d("YourFragment", "Fragment is being destroyed")
        // At this point, the fragment is being removed from memory
    }


// hundel the item click listener of recycler view "because it's better to call viewmodel only by the fragment"
    override fun onitemclick(position: Int) { // this is for the categories item click
        Log.d("onitemclick","the onitemclick get called")
        model.createfoodcategory(model.categorieslist.value?.get(position)?.name.toString())
    }



    override fun foodonitemclick(imageView: ImageView, position: Int,dataclass_item:Food) {
       val bundle = Bundle().apply {
            putInt("position", position)
            putParcelable("dataclass",dataclass_item) // pass the data class
           putString("source","foodlist")
        }

        val extras = FragmentNavigatorExtras(imageView to "image_$position") //should i use this ? this is shared element
        findNavController().navigate(
            R.id.action_homeFragment_to_foodDetailsChild,
            bundle,  // Bundle args (if any)
            NavOptions.Builder()
                .setLaunchSingleTop(true)
                .setPopUpTo(R.id.home, inclusive = false)
                .setRestoreState(true)
                .build(),  // NavOptions (if any)
            extras  // Shared element transition
        )
    }

    override fun bestselleronitemclick(position: Int,dataclass_item:Food,imageView: ImageView) {
        val bundle = Bundle().apply {
            putInt("position", position)
            putParcelable("dataclass",dataclass_item) // pass the data class
            putString("source","bestseller")
        }

        val extras = FragmentNavigatorExtras(imageView to "image_b$position") //should i use this ?
        findNavController().navigate(
            R.id.action_homeFragment_to_foodDetailsChild,
            bundle,  // Bundle args (if any)
            null,  // NavOptions (if any)
            extras  // Shared element transition
        )

    }


}