package com.cezma.app.data.model

import com.squareup.moshi.Json

data class CategoryModel(
    @field:Json(name = "id")
    val id: Int?,
    @field:Json(name = "category_name")
    val categoryName: String?,
    @field:Json(name = "category_slug")
    val categorySlug: String?,
    @field:Json(name = "is_sub")
    val isSub: Int?,
    @field:Json(name = "icon")
    val icon: String?,
    @field:Json(name = "created_at")
    val createdAt: String?,
    @field:Json(name = "updated_at")
    val updatedAt: String?,
    @field:Json(name = "parent_category")
    val parentCategory: Int?
){
    override fun toString(): String {
        return categoryName!!
    }
}