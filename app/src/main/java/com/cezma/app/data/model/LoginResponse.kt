package com.cezma.app.data.model

import com.squareup.moshi.Json

data class LoginResponse(

    @field:Json(name = "error")
    val error: String?,
    @field:Json(name = "message")
    val message: String?,
    @field:Json(name = "token_type")
    val tokenType: String?,
    @field:Json(name = "expires_in")
    val expiresIn: Int?,
    @field:Json(name = "access_token")
    val accessToken: String?,
    @field:Json(name = "refreshToken")
    val refreshToken: String?
)

data class LoginBody(
    val email: String,
    val password: String
)

data class RefreshTokenBody(
    val refresh_token:String
)

data class LogoutResponse(
    @field:Json(name = "status")
    val status: Boolean,
    @field:Json(name = "message")
    val message: String
)