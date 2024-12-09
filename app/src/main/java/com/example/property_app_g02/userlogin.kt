package com.example.property_app_g02

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.property_app_g02.databinding.ActivityUserloginBinding

class userlogin : AppCompatActivity() {
    lateinit var binding: ActivityUserloginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserloginBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.btnGoBack.setOnClickListener {
            // goes back to the originating screen (previous screen)
            //  - Whichever screen sent you here
            finish()
        }
    }

}