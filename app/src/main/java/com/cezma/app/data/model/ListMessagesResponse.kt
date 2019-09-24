package com.cezma.app.data.model

import com.squareup.moshi.Json

data class ListMessagesResponse(
    @Json(name = "totalcount")
    val totalcount: Int,
    @Json(name = "pages")
    val pages: Int,
    @Json(name = "currentpage")
    val currentpage: Int,
    @Json(name = "count")
    val count: Int,
    @Json(name = "messages")
    val messages: List<MessageModel>
)