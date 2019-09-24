package com.cezma.app.data.model


import com.squareup.moshi.Json

data class MyStoreResponse(
    @field:Json(name = "store")
    val store: MyStoreModel?
)