package com.cezma.app.data.model

import com.squareup.moshi.Json

data class SubCategoriesResponse(
    @field:Json(name = "categories")
    val subCategories: List<SubCategoryModel>?
)