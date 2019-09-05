package com.cezma.app.data.model


import com.squareup.moshi.Json

data class CountriesResponse(
    @field:Json(name = "data")
    val countries: List<CountryModel>
)