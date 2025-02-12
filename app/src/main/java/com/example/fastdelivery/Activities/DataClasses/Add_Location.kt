package com.example.fastdelivery.Activities.DataClasses

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.fastdelivery.R
import com.example.fastdelivery.databinding.ActivityAddLocationBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import java.util.Locale

class Add_Location : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var binding: ActivityAddLocationBinding
    private lateinit var gMap: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var selectedMarker: Marker? = null
    private lateinit var slocation: Location

    val REQUEST_LOCATION_PERMISSION = 1001

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding= ActivityAddLocationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)


        binding.savelocationbtn.setOnClickListener {
            finish()
        }


    }

    override fun onMapReady(p0: GoogleMap) {
        gMap = p0
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                MainActivity.REQUEST_LOCATION_PERMISSION
            )
// time of reflection : the user don't really need to allow permission to make all this pages
        // works (he stil can choose any location from the map without allowing the locaiton permission on his phone
        }
        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                val userLocation = LatLng(location.latitude, location.longitude)
                val adress = getAddress(userLocation.latitude, userLocation.longitude)
                binding.textCurrentLocation.text=adress
                gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 15f))

                // Add a marker at the user's location
                selectedMarker = gMap.addMarker(
                    MarkerOptions().position(userLocation).title("Your Location").icon(BitmapDescriptorFactory.defaultMarker(
                        BitmapDescriptorFactory.HUE_ORANGE))
                )
                slocation=location
            }
        }
        gMap.setOnMapClickListener { latLng ->
            selectedMarker?.remove() // Remove the previous marker
            selectedMarker = gMap.addMarker(MarkerOptions().position(latLng).title("Selected Location").icon(BitmapDescriptorFactory.defaultMarker(
                BitmapDescriptorFactory.HUE_ORANGE)))
            val adress = getAddress(latLng.latitude, latLng.longitude)
            binding.textCurrentLocation.text=adress
            slocation = Location("").apply {
                latitude = latLng.latitude
                longitude = latLng.longitude
            }
        // i can use viewmodel , you know what i think its better
        // cuz i'm gonna use this in another activity
        }


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
        resultIntent.putExtra("savedlocation", slocation)
        setResult(Activity.RESULT_OK, resultIntent)
        super.finish()
    }

}