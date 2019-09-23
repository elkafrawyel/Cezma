package com.cezma.app.data.model
import com.squareup.moshi.Json

data class AddShopBody(
    val username: String,
    val title: String,
    val short_desc: String,
    val long_desc: String,
    val category: Int
)

data class AddShopResponse(
    @field:Json(name = "status")
    val status: Boolean,
    @field:Json(name = "message")
    val message: String
)