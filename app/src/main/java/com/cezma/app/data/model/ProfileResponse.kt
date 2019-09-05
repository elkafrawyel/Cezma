package com.cezma.app.data.model


import com.squareup.moshi.Json
import java.io.File

data class ProfileResponse(
    @field:Json(name = "profile")
    val userModel: UserModel?
)

