package com.cezma.app.data.model

import com.squareup.moshi.Json

data class OffersResponse(
    @Json(name = "totalcount")
    val count: Int,
    @Json(name = "pages")
    val pages: Int,
    @Json(name = "currentpage")
    val currentPage: Int,
    @Json(name = "offers")
    val offers: List<OfferModel>
)

data class OffersActionResponse(
    @Json(name = "status")
    val status: Boolean,
    @Json(name = "msg")
    val msg: String
)

data class OffersActionBody(
    val accept: Int,
    val ad_id: Int,
    val offerid: Int
)
