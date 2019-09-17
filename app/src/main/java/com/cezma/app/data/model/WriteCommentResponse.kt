package com.cezma.app.data.model


import com.squareup.moshi.Json

data class WriteCommentResponse(
    @Json(name = "status")
    val status: Boolean,
    @Json(name = "msg")
    val msg: String
)

data class WriteCommentBody(
    val ad_id: String,
    val comment: String,
    val stars: Int
)