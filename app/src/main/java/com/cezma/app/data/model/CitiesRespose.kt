package com.cezma.app.data.model


import com.squareup.moshi.Json

data class CitiesRespose(
    @field:Json(name = "data")
    val cities: List<CityModel>
)