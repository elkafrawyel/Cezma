package com.cezma.app.data.model


import com.squareup.moshi.Json

data class BadgeCountResponse(
    @field:Json(name = "notficationcount")
    val notificationsCount: Int?,
    @field:Json(name = "messagecount")
    val messagesCount: Int?
)