package com.cezma.app.data.model


import com.squareup.moshi.Json

data class PagesResponse(
    @field:Json(name = "page")
    val page: StaticPage
)