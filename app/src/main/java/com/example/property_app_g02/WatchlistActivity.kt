package com.example.property_app_g02

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.property_app_g02.databinding.ActivityWatchlistBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class WatchlistActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWatchlistBinding
    private val db = Firebase.firestore
    private lateinit var auth: FirebaseAuth

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

        // Fetch watchlist items for the current user
        db.collection("properties")
            .document(userId)
            .collection("address")
            .get()
            .addOnSuccessListener { documents ->
                val watchlistItems = documents.map { doc ->
                    doc.getString("address") ?: "No Title" // Example field
                }

                // Set up RecyclerView
//                binding.recyclerView.apply {
//                    layoutManager = LinearLayoutManager(this@WatchlistActivity)
//                    adapter = WatchlistAdapter(watchlistItems)
//                }
            }
            .addOnFailureListener { exception ->
                Log.e("WatchlistActivity", "Error fetching watchlist: ", exception)
                Toast.makeText(this, "Failed to load watchlist.", Toast.LENGTH_SHORT).show()
            }
    }
}
