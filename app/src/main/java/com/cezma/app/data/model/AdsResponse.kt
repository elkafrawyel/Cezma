package com.cezma.app.data.model


import com.squareup.moshi.Json

data class AdsResponse(
    @field:Json(name = "ads")
    val ads: List<Ad>
)