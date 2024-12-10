package com.example.property_app_g02

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.property_app_g02.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore

class MainActivity : AppCompatActivity() {
    // TAG = used for Log.d
    private val TAG:String = "TESTING"
    lateinit var binding: ActivityMainBinding
    val db = Firebase.firestore

    lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // TODO: Initialize Firebase auth
        auth = Firebase.auth
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


    override fun onStart() {
        super.onStart()


        // TODO: When app loads, check if a user is logged in
        // TODO: IF yes, then directly navigate them to next page
        // val intent = Intent(this@com.example.property_app_g02.MainActivity, Screen2::class.java)
        // startActivity(intent)
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
            }
            .addOnFailureListener {
                    error ->
                binding.tvResults.text = "LOGIN FAILURE, check Log.d"
                Log.d("TESTING", "", error)
            }
    }


    fun signupUser(email:String, password:String) {
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
