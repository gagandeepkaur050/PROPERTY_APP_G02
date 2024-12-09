package com.example.property_app_g02

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.example.property_app_g02.databinding.ActivityMainBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth


class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    // create a Firebase auth variable
    lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // initialize Firebase auth
        auth = Firebase.auth

        supportActionBar!!.setTitle("Property")

//        binding.btnLogin.setOnClickListener {
//            // navigate the user to Screen #2
//            // a. Create the Intent object
//            val intent: Intent = Intent(this@MainActivity, userlogin::class.java)
//            startActivity(intent)
//        }

    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.settings_menu, menu)
        return true
    }
//
//
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
//            R.id.mi_new_tab -> {
//                Log.d("TESTING", "New Tab button clicked!")
//                val intent = Intent(this@MainActivity, Screen2Activity::class.java)
//                startActivity(intent)
//                return true
//            }
            R.id.userlogin -> {
                Log.d("TESTING", "Incognito button clicked!")
                val intent: Intent = Intent(this@MainActivity, userlogin::class.java)
                startActivity(intent)
                return true
            }
            R.id.logout -> {
                Log.d("TESTING", "History button clicked!")

                return true
            }
            R.id.watchlist -> {
                Log.d("TESTING", "Browing Data cleared!")
                val intent: Intent = Intent(this@MainActivity, Watchlist::class.java)
                startActivity(intent)
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }





}
