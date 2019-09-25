package com.cezma.app.data.model

import com.squareup.moshi.Json

data class ChatRoomResponse(
    @field:Json(name = "totalcount")
    val totalcount: Int,
    @field:Json(name = "pages")
    val pages: Int,
    @field:Json(name = "currentpage")
    val currentpage: Int,
    @field:Json(name = "messages")
    val messages: List<ChatRoomMessageModel>
)
data class ChatRoomBody(
    val usersend:String
)