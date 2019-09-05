package com.cezma.app.data.model


import com.squareup.moshi.Json

data class CountryModel(
    @field:Json(name = "id")
    val id: Int?,
    @field:Json(name = "sortname")
    val sortName: String?,
    @field:Json(name = "name")
    val name: String,
    @field:Json(name = "phonecode")
    val phoneCode: Int?
){
    override fun toString(): String {
        return name
    }
}