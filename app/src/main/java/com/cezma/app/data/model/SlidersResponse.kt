package com.cezma.app.data.model


import com.squareup.moshi.Json

data class SlidersResponse(
    @field:Json(name = "sliders")
    val sliders: List<String>
)