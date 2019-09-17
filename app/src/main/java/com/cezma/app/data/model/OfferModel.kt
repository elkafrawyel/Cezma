package com.cezma.app.data.model


import com.squareup.moshi.Json

data class OfferModel(
    @field:Json(name = "id")
    val id: Int,
    @field:Json(name = "ad_id")
    val adId: Int,
    @field:Json(name = "offer_by")
    val offerBy: Int,
    @field:Json(name = "offer_to")
    val offerTo: Int,
    @field:Json(name = "price")
    val price: Int,
    @field:Json(name = "mainprice")
    val mainPrice: Int,
    @field:Json(name = "is_accepted")
    var isAccepted: Int?,
    @field:Json(name = "created_at")
    val createdAt: String?,
    @field:Json(name = "updated_at")
    val updatedAt: String?,
    @field:Json(name = "subcategory_name")
    val subcategoryName: String?,
    @field:Json(name = "maincategory_name")
    val maincategoryName: String?,
    @field:Json(name = "ad_name")
    val adName: String?,
    @field:Json(name = "thumbnails")
    val thumbnails: List<String?>?,
    @field:Json(name = "offersender")
    val offersender: String?,
    @field:Json(name = "offersenderavatar")
    val offersenderavatar: String?
)