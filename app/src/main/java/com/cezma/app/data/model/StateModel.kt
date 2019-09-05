package com.cezma.app.data.model


import com.squareup.moshi.Json

data class StateModel(
    @field:Json(name = "id")
    val id: Int?,
    @field:Json(name = "name")
    val name: String,
    @field:Json(name = "country_id")
    val countryId: Int?
){
    override fun toString(): String {
        return name
    }
}