package com.cezma.app.data.model


import com.squareup.moshi.Json

data class ProfileResponse(
    @field:Json(name = "profile")
    val userModel: UserModel?
)

