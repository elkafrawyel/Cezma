package com.cezma.app.data.model

import com.squareup.moshi.Json

data class CategoriesResponse(
    @field:Json(name = "categories")
    val categories: List<CategoryModel>
)