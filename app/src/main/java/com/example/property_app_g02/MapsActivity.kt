package com.example.property_app_g02

import android.graphics.BitmapFactory
import android.location.Geocoder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.example.property_app_g02.databinding.ActivityMapsBinding
import com.google.android.gms.maps.model.Marker
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.Locale
import kotlin.math.max
import kotlin.reflect.typeOf

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

        binding.btnSearch.setOnClickListener {
            getMaximum()
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

    private fun getMapLocations() {

        Log.d("TESTING", "FROM FIRESTORE")

        db.collection("properties")
           .whereEqualTo("isAvailable",true)
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
                    Log.d("TESTING","${houseFromDb.title} : ${houseFromDb.isAvailable}")
                    // b. Create a map marker object using the house's location data
                    // c. Add it to the map
                    mMap.addMarker(
                        MarkerOptions().position(pos)
                            // popup window
                            .title(houseFromDb.address)
                            .snippet(houseFromDb.landlord)
                    )

                    mMap.setOnMarkerClickListener { marker: Marker ->

                        val pos:LatLng = LatLng(marker.position.latitude,marker.position.longitude)
                        getStreetAddress(pos)
                        Log.d("TESTING","${pos.latitude}, ${pos.longitude}")

                        false
                    }

                }
            }
            .addOnFailureListener { exception ->
                Log.w("", "Error getting documents.", exception)
            }


    }

    private fun getStreetAddress(latLng: LatLng){
        Log.d("TESTING","GET ADDRESS, ${latLng.latitude},${latLng.longitude}")
        db.collection("properties")
            .whereEqualTo("lat",latLng.latitude)
            .whereEqualTo("long",latLng.longitude)
            .get()
            .addOnSuccessListener {
                    results:QuerySnapshot ->
                for (document in results) {
                    // convert the document to a Student object
                    val houseFromDb:House = document.toObject(House::class.java)
                    // 3. do something with the student
                    Log.d("TESTING", "GETADDRESS,${houseFromDb.lat}, ${houseFromDb.long}")

                    val output = """
                       ${houseFromDb.address}
                       Number of Bedrooms: ${houseFromDb.numberOfBedrooms}
                       Monthly Price: $${houseFromDb.monthPrice}
                       ${houseFromDb.title}
                       Landlord Info: ${houseFromDb.landlord}
               
                        """.trimIndent()
                    binding.tvResults.text = output
                    binding.btnWatchList.visibility = View.VISIBLE


                     }
            }.addOnFailureListener {
                    exception ->
                Log.w("TESTING", "Error getting documents.", exception)
            }


    }

    private fun getMaximum(){
        var maxFromUI = binding.etMaximum.text.toString().toDoubleOrNull()

        if(maxFromUI == null){
           maxFromUI = 0.0
            getMapLocations()
        } else{
            db.collection("properties")
                .whereLessThanOrEqualTo("monthPrice",maxFromUI)
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
                        Log.d("TESTING","${houseFromDb.title} : ${houseFromDb.isAvailable}")
                        // b. Create a map marker object using the house's location data
                        // c. Add it to the map
                        mMap.clear()

                        mMap.addMarker(
                            MarkerOptions().position(pos)
                                // popup window
                                .title(houseFromDb.address)
                                .snippet(houseFromDb.landlord)
                        )

                        mMap.setOnMarkerClickListener { marker: Marker ->

                            val pos:LatLng = LatLng(marker.position.latitude,marker.position.longitude)
                            getStreetAddress(pos)
                            Log.d("TESTING","${pos.latitude}, ${pos.longitude}")

                            false
                        }

                    }
                }
                .addOnFailureListener { exception ->
                    Log.w("", "Error getting documents.", exception)
                }

        }
        Log.d("TESTING","${maxFromUI}")

    }
}
