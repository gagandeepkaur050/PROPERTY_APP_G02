package com.example.property_app_g02.models

import com.google.firebase.firestore.DocumentId

data class UserProfile(
    @DocumentId
    var id:String = "",
    var email:String = "",
    var password:String = "",
    var watchlist:MutableList<String> = mutableListOf(""),
    @JvmField
    var isLandlord:Boolean = false
)
