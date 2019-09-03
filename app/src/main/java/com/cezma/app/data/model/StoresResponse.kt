package com.cezma.app.data.model


import com.squareup.moshi.Json

data class StoresResponse(
    @field:Json(name = "stores")
    val stores: List<StoreModel>
)