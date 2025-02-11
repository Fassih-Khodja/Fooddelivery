package com.example.fastdelivery.Activities.DataClasses

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fastdelivery.Adapters.MyCartAdapter
import com.example.fastdelivery.Adapters.OnCartItemChangeQuantity
import com.example.fastdelivery.Decorations.Decoration_Items
import com.example.fastdelivery.Models.DataClasses.Cart
import com.example.fastdelivery.Models.DataClasses.Orders
import com.example.fastdelivery.Models.Repositories.OrdersFetch_repository
import com.example.fastdelivery.Models.Repositories.Orderschange_repository
import com.example.fastdelivery.ViewModels.MyOrders_view_model
import com.example.fastdelivery.databinding.ActivityMyCartBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
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
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback
    private lateinit var spannable: Spannable
    val db = FirebaseFirestore.getInstance()
    val auth= FirebaseAuth.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding=ActivityMyCartBinding.inflate(layoutInflater)
        setContentView(binding.root)


        getcurrentlocation()


        spannable = SpannableString(binding.editTextBtn.text).apply {
            setSpan(UnderlineSpan(), 0, length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)}
        binding.editTextBtn.text = spannable

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
                if (location != null) {
                    val latitude = location.latitude
                    val longitude = location.longitude
                    Log.d("Location", "Lat: $latitude, Lng: $longitude")

                    val address = getAddress(latitude, longitude)
                    binding.textCurrentLocation.text = address
                } else {
                    binding.textCurrentLocation.text = "Location not found,Please add your location"
                }
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