package com.example.property_app_g02

import com.google.firebase.firestore.DocumentId

data class House(
    var address:String = "",
    var img:String = "",
    var isAvailable:Boolean = true,
    var landlord:String = "",
    var lat:Double = 0.0,
    var long:Double = 0.0,
    var monthPrice:Double = 0.0,
    var numberOfBedrooms:Int = 0,
    var title:String = "",
    @DocumentId
    var id:String = ""
)

