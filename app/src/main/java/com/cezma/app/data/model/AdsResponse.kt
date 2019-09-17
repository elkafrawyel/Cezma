package com.cezma.app.data.model


import com.squareup.moshi.Json

data class AdsResponse(
    @field:Json(name = "ads")
    val ads: List<Ad>,
    @field:Json(name = "totalcount")
    val count: Int,
    @field:Json(name = "pages")
    val pages: Int,
    @field:Json(name = "currentpage")
    val currentPage: Int
)
