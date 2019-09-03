package com.cezma.app.data.model


import com.squareup.moshi.Json

data class FavoriteAd(
    @field:Json(name = "id")
    val id: Int,
    @field:Json(name = "ad_id")
    val adId: Long,
    @field:Json(name = "user_id")
    val userId: Int,
    @field:Json(name = "owner")
    val owner: Int,
    @field:Json(name = "created_at")
    val createdAt: String,
    @field:Json(name = "updated_at")
    val updatedAt: String,
    @field:Json(name = "ad_name")
    val adName: String,
    @field:Json(name = "thumbnails")
    val thumbnails: List<String>
)