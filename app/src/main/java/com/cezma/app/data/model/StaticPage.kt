package com.cezma.app.data.model


import com.squareup.moshi.Json

data class StaticPage(
    @field:Json(name = "id")
    val id: Int?,
    @field:Json(name = "page_name")
    val pageName: String?,
    @field:Json(name = "page_slug")
    val pageSlug: String?,
    @field:Json(name = "page_col")
    val pageCol: String?,
    @field:Json(name = "page_content")
    val pageContent: String?,
    @field:Json(name = "created_at")
    val createdAt: String?,
    @field:Json(name = "updated_at")
    val updatedAt: String?
)