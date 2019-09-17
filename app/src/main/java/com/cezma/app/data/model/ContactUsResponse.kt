package com.cezma.app.data.model


import com.squareup.moshi.Json

data class ContactUsResponse(
    @field:Json(name = "status")
    val status: Boolean,
    @field:Json(name = "message")
    val message: String
)


data class ContactUsBody(
    val full_name:String,
    val email:String,
    val message:String,
    val phone:String,
    val subject:String
)