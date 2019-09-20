package com.cezma.app.data.model

import com.squareup.moshi.Json

data class CommentsResponse(
    @field:Json(name = "totalcount")
    val totalcount: Int,
    @field:Json(name = "pages")
    val pages: Int,
    @field:Json(name = "currentpage")
    val currentpage: Int,
    @field:Json(name = "comments")
    val comments: List<CommentModel>
)

data class CommentsBody(
    val ad_id: String
)