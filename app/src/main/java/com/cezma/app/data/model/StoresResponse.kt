package com.cezma.app.data.model


import com.squareup.moshi.Json

data class StoresResponse(
    @field:Json(name = "store")
    val stores: List<StoreModel>,
    @field:Json(name = "totalcount")
    val count: Int,
    @field:Json(name = "pages")
    val pages: Int,
    @field:Json(name = "currentpage")
    val currentPage: Int
)