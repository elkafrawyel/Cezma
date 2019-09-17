package com.cezma.app.data.model


import com.squareup.moshi.Json

data class RegisterResponse(
    @Json(name = "status")
    val status: Boolean?,
    @Json(name = "errorMessage")
    val message: String?
)

data class RegisterBody(
    var first_name: String,
    var last_name: String,
    var username: String,
    var email: String,
    var phone: String,
    var phonecode: String = "20",
    var gender: String,
    var password: String? = null,
    var password_confirmation: String? = null,
    var city: String,
    var country:String = "EG"
)