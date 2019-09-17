package com.cezma.app.data.model


import com.squareup.moshi.Json

data class AdOfferResponse(
    @field:Json(name = "status")
    val status: Boolean,
    @field:Json(name = "msg")
    val msg: String
)

data class AdOfferBody(
    var ad_id:String,
    var price:String
)