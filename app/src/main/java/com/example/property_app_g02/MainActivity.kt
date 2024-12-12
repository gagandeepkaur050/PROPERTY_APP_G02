package com.example.property_app_g02

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.example.property_app_g02.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore

class MainActivity : AppCompatActivity() {
    // TAG = used for Log.d
    private val TAG:String = "TESTING"
    private lateinit var binding: ActivityMainBinding
    val db = Firebase.firestore

    lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar!!.setTitle("Login")
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        //auth = Firebase.auth
        //loadUserData()
        // click handlers
        binding.btnLogin.setOnClickListener {
            // get email and password
            val emailFromUI = binding.etEmail.text.toString()
            val passwordFromUI = binding.etPassword.text.toString()
            // try to login
            loginUser(emailFromUI, passwordFromUI)


        }
        binding.btnSignup.setOnClickListener {
            // get email and password
            val emailFromUI = binding.etEmail.text.toString()
            val passwordFromUI = binding.etPassword.text.toString()
            // try to login
            signupUser(emailFromUI, passwordFromUI)
        }
        binding.btnLogout.setOnClickListener {
            logoutCurrentUser()
        }


    }


    // menu code here
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

                //finish()
                return true
            }
            R.id.watchlist -> {
//                val intent = Intent(this@MapsActivity, WatchlistActivity::class.java)
//                startActivity(intent)
                if (auth.currentUser == null) {
                    // User not authenticated, navigate to MainActivity
                    val intent = Intent(this@MainActivity, MainActivity::class.java)
                    startActivity(intent)
                } else {
                    // User is authenticated, navigate to Watchlist
                    val intent = Intent(this@MainActivity, WatchlistActivity::class.java)
                    startActivity(intent)
                }
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onResume() {
        super.onResume()
        checkIfUserLoggedIn()

    }


    // helper functions for Firebase Auth
    fun loginUser(email:String, password:String) {
        // TODO: Code to login
        auth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                val snackbar = Snackbar.make(binding.root, "LOGIN SUCCESS!", Snackbar.LENGTH_LONG)
                snackbar.show()
                // disable or enable the login button
                checkIfUserLoggedIn()
                //val intent = Intent(this@MainActivity, WatchlistActivity::class.java)
                ///startActivity(intent)
                val intent = Intent(this@MainActivity, WatchlistActivity::class.java)
                startActivity(intent)

            }
            .addOnFailureListener {
                    error ->
                binding.tvResults.text = "LOGIN FAILURE, check Log.d"
                Log.d("TESTING", "", error)
                val snackbar = Snackbar.make(binding.root, "LOGIN fail!", Snackbar.LENGTH_LONG)
                snackbar.show()
            }
    }
    private fun signupUser(email:String, password:String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                Snackbar.make(binding.root, "Account created in Firebase Auth", Snackbar.LENGTH_SHORT).show()

                // If the user was successfully created
                // create the corresponding user profile in firestore
                val data: MutableMap<String, Any> = HashMap();
                data["Email"] = binding.etEmail.text.toString()
                data["Password"] = binding.etPassword.text.toString()

                // insert the data into the collection
                db.collection("userProfiles")
                    .add(data)
                    .addOnSuccessListener { docRef ->
                        Log.d("TESTING", "Document successfully added with ID : ${docRef.id}")
                        binding.tvResults.text = "User profile created! ${docRef.id}"

                    }
                    .addOnFailureListener { ex ->
                        Log.e("TESTING", "Exception occurred while adding a document : $ex", )
                    }


            }
            .addOnFailureListener {
                    error ->
                binding.tvResults.text = "ERROR: Check Log.d for error"
                Log.d("TESTING", "Error:", error)
            }

    }


    fun logoutCurrentUser() {
        // TODO: Code to logout
        auth.signOut()
        binding.tvResults.text = "LOGOUT SUCCESS!"
        checkIfUserLoggedIn()
    }


    fun checkIfUserLoggedIn() {
        // TODO: Code to check if user is logged in
        if (auth.currentUser == null) {
            // no user is logged in
            // Then allow them to login to the system
            binding.tvResults.text = "No user logged in, enabling button"
            binding.btnLogin.isEnabled = true
        } else {
            // there is a user logged in already
            // If a user is already logged in, then they should NOT be able to Login
            binding.tvResults.text = "There is a user logged in, disable button"
            binding.btnLogin.isEnabled = false
        }




    }


}
