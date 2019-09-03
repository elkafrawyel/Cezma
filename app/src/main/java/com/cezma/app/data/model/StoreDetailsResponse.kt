package com.cezma.app.data.model


import com.squareup.moshi.Json

data class StoreDetailsResponse(
    @Json(name = "store")
    val store: StoreModel
)