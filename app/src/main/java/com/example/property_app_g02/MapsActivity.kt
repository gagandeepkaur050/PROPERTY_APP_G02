package com.example.property_app_g02

import android.location.Address
import android.location.Geocoder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.example.property_app_g02.databinding.ActivityMapsBinding
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.Locale

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap

    lateinit var binding: ActivityMapsBinding
    lateinit var geocoder: Geocoder
    val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        geocoder = Geocoder(applicationContext, Locale.getDefault())

        binding.btnGetCoordinates.setOnClickListener {

        }

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

    }



    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Setup the map zoom controls
        mMap.uiSettings.isZoomControlsEnabled = true

        // Coordinates for Downtown Toronto
        val downtownToronto = LatLng(43.651070, -79.347015)
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(downtownToronto, 10f)) // You can adjust the zoom level here

        // Enable zoom gestures
        mMap.uiSettings.isZoomGesturesEnabled = true

        getMapLocations()
    }

    fun getMapLocations() {

        Log.d("TESTING", "FROM FIRESTORE")

        db.collection("properties")
            .get()
            .addOnSuccessListener {
                    results: QuerySnapshot ->
                // 1. for each document in the collection,
                for (document: QueryDocumentSnapshot in results) {
                    // 2. convert the document to a Student object
                    val houseFromDb:House = document.toObject(House::class.java)

                    // 3. show it in a map
                    // a. Get location data from the house
                    val pos:LatLng = LatLng(houseFromDb.lat, houseFromDb.long)
                    Log.d("TESTING", "${pos.latitude}, ${pos.longitude}")
                    // b. Create a map marker object using the house's location data
                    // c. Add it to the map
                    mMap.addMarker(
                        MarkerOptions().position(pos)
                            // popup window
                            .title(houseFromDb.address)
                            .snippet(houseFromDb.landlord)
                    )


                }
            }
            .addOnFailureListener { exception ->
                Log.w("", "Error getting documents.", exception)
            }
    }

}
