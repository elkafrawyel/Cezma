package com.cezma.app.data.model


import com.squareup.moshi.Json

data class HomeAdsResponse(
    @Json(name = "totalcount")
    val totalcount: Int,
    @Json(name = "pages")
    val pages: Int,
    @Json(name = "currentpage")
    val currentpage: Int,
    @Json(name = "ads")
    val ads: List<Ad>
)
