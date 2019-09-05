package com.cezma.app.data.model


import com.squareup.moshi.Json

data class AdsByOwnerResponse(
    @field:Json(name = "data")
    val ads: List<Ad>
)