package com.cezma.app.data.model


import com.squareup.moshi.Json

data class AdResponse(
    @field:Json(name = "data")
    val ad: Ad
)