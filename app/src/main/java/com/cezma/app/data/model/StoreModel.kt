package com.cezma.app.data.model


import com.squareup.moshi.Json

data class StoreModel(
    @field:Json(name = "id")
    val id: Int,
    @field:Json(name = "owner_id")
    val ownerId: Int,
    @field:Json(name = "username")
    val username: String,
    @field:Json(name = "title")
    val title: String?,
    @field:Json(name = "short_desc")
    val shortDesc: String?,
    @field:Json(name = "long_desc")
    val longDesc: String?,
    @field:Json(name = "category")
    val category: Int?,
    @field:Json(name = "logo")
    val logo: String?,
    @field:Json(name = "cover")
    val cover: String?,
    @field:Json(name = "country")
    val country: String?,
    @field:Json(name = "state")
    val state: Int?,
    @field:Json(name = "city")
    val city: Int?,
    @field:Json(name = "address")
    val address: String?,
    @field:Json(name = "fb_page")
    val fbPage: String?,
    @field:Json(name = "tw_page")
    val twPage: String?,
    @field:Json(name = "yt_page")
    val ytPage: String?,
    @field:Json(name = "go_page")
    val goPage: String?,
    @field:Json(name = "website")
    val website: String?,
    @field:Json(name = "status")
    val status: Int?,
    @field:Json(name = "tawk")
    val tawk: String?,
    @field:Json(name = "ends_at")
    val endsAt: String?,
    @field:Json(name = "created_at")
    val createdAt: String?,
    @field:Json(name = "updated_at")
    val updatedAt: String?
)