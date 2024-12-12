package com.example.property_app_g02

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.property_app_g02.adapters.WatchlistAdapter
import com.example.property_app_g02.databinding.ActivityWatchlistBinding
import com.example.property_app_g02.models.UserProfile
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class WatchlistActivity : AppCompatActivity(), ClickDetectorInterface {

    private lateinit var binding: ActivityWatchlistBinding
    private val db = Firebase.firestore
    private lateinit var auth: FirebaseAuth
    var watchlist = mutableListOf<House>()
    var adapter = WatchlistAdapter(watchlist,this)



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize View Binding
        binding = ActivityWatchlistBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set up ActionBar
        supportActionBar?.apply {
            title = "Your Watchlist"
            setDisplayHomeAsUpEnabled(true)
        }

        // Initialize FirebaseAuth
        auth = FirebaseAuth.getInstance()

        // Load Watchlist from Firestore
        loadWatchlist()

    }
    // 1. Shows the menu , but it doesn't handle clicks on the menu
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.userscreen_menu, menu)
        return true
    }

    // 2. Handling clicks on items in the bar (back button and options menu)
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                // do something when the person presses back
                Log.d("TESTING", "Back button clicked!")
                finish()
                return true
            }
            R.id.mi_go_back -> {
                finish()
                return true
            }
            R.id.logout -> {
                Log.d("TESTING", "Incognito button clicked!")
                auth.signOut()
                finish()
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun loadWatchlist() {
        val currentUser = auth.currentUser
        if (currentUser == null) {
            Toast.makeText(this, "User not authenticated!", Toast.LENGTH_SHORT).show()
            return
        }

        val userId = currentUser.uid
//        val watchlist = mutableListOf<House>()
//        val adapter = WatchlistAdapter(watchlist)

        // Set up RecyclerView
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter

        // Fetch user profile
        db.collection("userProfiles")
            .document(userId)
            .get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val userProfile = document.toObject(UserProfile::class.java)
                    val watchlistIds = userProfile?.watchlist ?: emptyList()

                    Log.d("TESTING", "Watchlist for $userId: $watchlistIds")

                    if (watchlistIds.isEmpty()) {
                        Toast.makeText(this, "Watchlist is empty.", Toast.LENGTH_SHORT).show()
                        return@addOnSuccessListener
                    }

                    for (propertyId in watchlistIds) {
                        if (propertyId.isNotEmpty()) {
                            db.collection("properties")
                                .document(propertyId)
                                .get()
                                .addOnSuccessListener { propertyDoc ->
                                    if (propertyDoc.exists()) {
                                        val house = propertyDoc.toObject(House::class.java)
                                        house?.let {
                                            watchlist.add(it) // Add the house
                                            adapter.notifyItemInserted(watchlist.size - 1) // Notify adapter
                                        }
                                    } else {
                                        Log.w("TESTING", "////Property not found for ID: $propertyId")
                                    }
                                }
                                .addOnFailureListener { exception ->
                                    Log.w("TESTING", "/////Error fetching property: $propertyId", exception)
                                }

                    } else {
                            Log.w("TESTING", "///Invalid propertyId: $propertyId")
                        }
                    }
                } else {
                    Log.d("TESTING", "////No user profile found for ID: $userId")
                }
            }
            .addOnFailureListener { exception ->
                Log.w("TESTING", "Error fetching user profile.", exception)
            }
    }

    override fun deleteFunction(position: Int) {
        ///this.adapter = WatchlistAdapter(watchlist,this)

        Log.d("TESTING","From deletefunction ${position}")
        Log.d("TESTING", "${auth.currentUser?.uid}")

        db.collection("userProfiles")
            .document(auth.currentUser?.uid ?: "")
            .get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val ttt = document.toObject(UserProfile::class.java)
                    ttt?.watchlist?.removeAt(position)
                    //added to update local list
                    watchlist.removeAt(position)
                    db.collection("userProfiles")
                        .document(ttt?.id ?: "")
                        .update("watchlist", ttt?.watchlist)
                        .addOnSuccessListener {
                            adapter.notifyItemRemoved(position)
                            Log.d("TESTING", "DELETE pass")
                        }
                        .addOnFailureListener {
                            Log.d("TESTING","DELETE FAIL")
                        }
            }else {
                    Log.w("TESTING", "ERROR")
                }


           // .update("watchlist",watchlist)
//            .addOnSuccessListener { docRef ->
//                Log.d("TESTING", "Document successfully delete")
//            }
//            .addOnFailureListener { ex ->
//                Log.e("TESTING", "Exception occurred while adding a document : $ex", )
//            }

    }

}
}
