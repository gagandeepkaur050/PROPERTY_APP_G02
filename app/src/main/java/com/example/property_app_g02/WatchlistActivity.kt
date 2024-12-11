package com.example.property_app_g02

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.property_app_g02.databinding.ActivityWatchlistBinding
import com.example.property_app_g02.models.UserProfile
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
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
        db.collection("userProfiles")
            .document(userId)
            .get()
            .addOnSuccessListener {
                    document: DocumentSnapshot ->

                val user:UserProfile? = document.toObject(UserProfile::class.java)


                if (user == null) {
                    Log.d("TESTING", "No results found")
                    return@addOnSuccessListener
                }

                // if you reach this point, then we found a student
                Log.d("TESTING", user.toString())

                /*
                val myArray = arrayOf("A", "B", "C", "D")

for (i in 0 until myArray.size - 1) { // `until` excludes the last index
    println(myArray[i])
}

                 */

                for (i in 0 until user.watchlist.size - 1){
                    Log.d("TESTING",user.watchlist[i])
                    db.collection("properties")
                        .document(user.watchlist[i])
                        .get()
                        .addOnSuccessListener {
                            document2: DocumentSnapshot ->
                            val houseFromDb:House? = document2.toObject(House::class.java)
                            if(houseFromDb == null){
                                Log.d("TESINTG","NULL HOUSE")
                                return@addOnSuccessListener
                            }
                        val price=houseFromDb.monthPrice.toString()
                            val numBeds = houseFromDb.numberOfBedrooms.toString()
                            val address = houseFromDb.address

                            binding.priceText.text = price
                            binding.bedroomsText.text = numBeds
                            binding.addressText.text = address
                        }


                }


            }.addOnFailureListener {
                    exception ->
                Log.w("TESTING", "Error getting documents.", exception)
            }
    }
}
