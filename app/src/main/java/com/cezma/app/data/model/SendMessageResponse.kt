package com.cezma.app.data.model


import com.squareup.moshi.Json

data class SendMessageResponse(
    @field:Json(name = "status")
    val status: Boolean,
    @field:Json(name = "message")
    val message: String
)

data class SendMessageBody(
    var message: String
)