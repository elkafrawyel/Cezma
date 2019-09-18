package com.cezma.app.data.model

import com.squareup.moshi.Json

data class CommentsResponse(
    @field:Json(name = "comments")
    val comments: List<CommentModel>
)

data class CommentsBody(
    val ad_id: String
)