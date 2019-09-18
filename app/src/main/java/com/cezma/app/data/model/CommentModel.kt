package com.cezma.app.data.model


import com.squareup.moshi.Json

data class CommentModel(
    @field:Json(name = "id")
    val id: Int,
    @field:Json(name = "ad_id")
    val adId: Int,
    @field:Json(name = "user_id")
    val userId: Int,
    @field:Json(name = "store_id")
    val storeId: Int,
    @field:Json(name = "comment")
    val comment: String,
    @field:Json(name = "rating")
    val rating: Int,
    @field:Json(name = "is_approved")
    val isApproved: Int,
    @field:Json(name = "username")
    val username: String,
    @field:Json(name = "avatar")
    val avatar: String,
    @field:Json(name = "created_at")
    val createdAt: String,
    @field:Json(name = "updated_at")
    val updatedAt: String
)