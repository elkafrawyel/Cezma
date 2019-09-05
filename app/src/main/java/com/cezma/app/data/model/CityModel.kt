package com.cezma.app.data.model


import com.squareup.moshi.Json

data class CityModel(
    @field:Json(name = "id")
    val id: Int?,
    @field:Json(name = "name")
    val name: String,
    @field:Json(name = "state_id")
    val stateId: Int?,
    @field:Json(name = "country_id")
    val countryId: Any?
){
    override fun toString(): String {
        return name
    }
}