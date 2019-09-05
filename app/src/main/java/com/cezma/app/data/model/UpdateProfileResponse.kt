package com.cezma.app.data.model


import com.squareup.moshi.Json

data class UpdateProfileResponse(
    @field:Json(name = "status")
    val status: Boolean?,
    @field:Json(name = "message")
    val message: String?
)
data class UpdateProfileBody(
    var first_name: String,
    var last_name: String,
    var username: String,
    var email: String,
    var phone: String,
    var phonecode: String,
    var gender: String,
    var country: String,
    var state: String,
    var new_password: String? = null,
    var old_password: String? = null,
    var city: String
)