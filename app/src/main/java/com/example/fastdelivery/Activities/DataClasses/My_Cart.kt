package com.example.fastdelivery.Activities.DataClasses

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fastdelivery.Adapters.MyCartAdapter
import com.example.fastdelivery.Adapters.OnCartItemChangeQuantity
import com.example.fastdelivery.Decorations.Decoration_Items
import com.example.fastdelivery.Models.DataClasses.Cart
import com.example.fastdelivery.Models.DataClasses.Orders
import com.example.fastdelivery.Models.Repositories.OrdersFetch_repository
import com.example.fastdelivery.Models.Repositories.Orderschange_repository
import com.example.fastdelivery.R
import com.example.fastdelivery.ViewModels.MyOrders_view_model
import com.example.fastdelivery.databinding.ActivityMyCartBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.Locale

class My_Cart : AppCompatActivity(), OnCartItemChangeQuantity {
    private lateinit var binding:ActivityMyCartBinding
    private lateinit var cartlist:ArrayList<Cart> // its better to use a viewmodel for this one
    private lateinit var cartadapter:MyCartAdapter
    private lateinit var model: MyOrders_view_model
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var spannable: Spannable
    val db = FirebaseFirestore.getInstance()
    val auth= FirebaseAuth.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val  resultLauncher = registerForActivityResult(
            // here i will register the launcher with a contract. A contract defines what type of result you're expecting
            // in my case the type is a startactivity for result
            ActivityResultContracts.StartActivityForResult()
        ) { result -> // this is a callback executed every time the activity do (set result) , and will not executed on the first time
            if (result.resultCode == Activity.RESULT_OK) {
                Log.d("the result.code","good")
                //here i will Handle the result here
                val data = result.data
                //result.data retrieves the Intent that was sent back from the second activity. This Intent contains any data that was passed using setResult() from the second activity.
                val choosenlocation = data?.getParcelableExtra<Location>("savedlocation")
                if (choosenlocation != null) {
                    setlocation(choosenlocation)
                }
            } else  Log.d("the result.code","bad")
        }


        binding=ActivityMyCartBinding.inflate(layoutInflater)
        setContentView(binding.root)


        getcurrentlocation()


        spannable = SpannableString(binding.editTextBtn.text).apply {
            setSpan(UnderlineSpan(), 0, length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)}
        binding.editTextBtn.text = spannable


  spannable = SpannableString(binding.editItemsText.text).apply {
            setSpan(UnderlineSpan(), 0, length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)}
        binding.editItemsText.text = spannable

        val ordersrepo= OrdersFetch_repository(db,auth.currentUser!!.uid)
        val orderschangerepo= Orderschange_repository(db,auth.currentUser!!.uid)
        model= ViewModelProvider(this, MyOrders_view_model.Factory(ordersrepo,orderschangerepo)).get(
            MyOrders_view_model::class.java)



        Log.d("activity","has been created")
        cartlist= intent.getParcelableArrayListExtra("CartList")!!
        Log.d("test",cartlist.toString())
        cartadapter=MyCartAdapter(cartlist,this)
        binding.mycartrecyclerview.adapter=cartadapter
        val spaceItemDecoration = Decoration_Items(50,binding.mycartrecyclerview)
        binding.mycartrecyclerview.addItemDecoration(spaceItemDecoration)
        binding.mycartrecyclerview.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)


        binding.placeorderbtn.setOnClickListener {
            model.AddOrder(Orders(cartlist,"",0))
            cartlist= arrayListOf()
        }

        binding.editTextBtn.setOnClickListener {
            val intent=Intent(this,Add_Location::class.java)
           // startActivity(intent)
            resultLauncher.launch(intent)
        }

        binding.editItemsText.setOnClickListener {
if (binding.editItemsText.text=="EDIT ITEMS") Log.d("edit","edit text")
            if ((binding.editItemsText.text.toString()=="EDIT ITEMS" && cartlist.isNotEmpty())|| binding.editItemsText.text.toString()=="DONE"){
                cartadapter.tooglemode()

                val newText = if (cartadapter.isEditMode) "DONE" else "EDIT ITEMS"
            val newColor = if (cartadapter.isEditMode) ContextCompat.getColor(this, R.color.green)
            else ContextCompat.getColor(this, R.color.edit_text)
            val newSpannable = SpannableString(newText).apply {
                setSpan(UnderlineSpan(), 0, length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            }

            // Fade out animation
            binding.editItemsText.animate()
                .alpha(0f) // Fade out
                .setDuration(150)
                .withEndAction {
                    binding.editItemsText.text = newSpannable
                    binding.editItemsText.setTextColor(newColor)


                    // Fade in animation
                    binding.editItemsText.animate()
                        .alpha(1f) // Fade back in
                        .setDuration(150)
                        .start()
                }
                .start()
        }}

    }

    private fun getcurrentlocation() {
        Log.d("current", "getCurrentLocation() called")
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)


        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
           binding.textCurrentLocation.text="NO Location , Please enable gps from your phone"
        } else {
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location ->
               setlocation(location)
            }
            .addOnFailureListener { e ->
                Log.e("Location Error", "Failed to get location: ${e.message}")
                binding.textCurrentLocation.text = "There is no location ,Please Add your location"
            }}
    }

    private fun getAddress(latitude: Double, longitude: Double): String {
        val geocoder = Geocoder(this, Locale.getDefault())
        return try {
            val addresses = geocoder.getFromLocation(latitude, longitude, 1)
            if (addresses != null && addresses.isNotEmpty()) {
                val address = addresses[0]

                // Extracting relevant address details
                val street = address.thoroughfare ?: ""  // Street name
                val city = address.locality ?: ""  // City name
                val state = address.adminArea ?: ""  // State/Province
                val country = address.countryName ?: ""  // Country name

                // Formatting the address (removing empty values)
                listOf(street, city, state, country)
                    .filter { it.isNotEmpty() }
                    .joinToString(", ")
            } else {
                "Unknown location"
            }
        } catch (e: Exception) {
            "Unable to get address"
        }
    }
    private fun setlocation(location: Location){ // maybe its better to use viewmodel
        Log.d("setlocation","setlocation get called")
        if (location != null) {
            val latitude = location.latitude
            val longitude = location.longitude
            val address = getAddress(latitude, longitude)
            binding.textCurrentLocation.text = address
        } else binding.textCurrentLocation.text = "Location not found,Please add your location"
    }

    override fun finish() {
        val resultIntent = Intent()
        Log.d("the setresult",cartlist.toString())
        resultIntent.putParcelableArrayListExtra("CartList2", cartlist)
        setResult(Activity.RESULT_OK, resultIntent)
        super.finish()
    }

    override fun oncartitemclchangequantity(position: Int) {
        Log.d("this ",cartlist[position].quantity.toString())
    }

}