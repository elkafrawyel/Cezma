package com.cezma.app.data.model

import com.squareup.moshi.Json

data class MessageModel(
    @field:Json(name = "id")
    val id: Int?,
    @field:Json(name = "ad_id")
    val adId: String?,
    @field:Json(name = "msg_from")
    val msgFrom: String?,
    @field:Json(name = "msg_to")
    val msgTo: String?,
    @field:Json(name = "email")
    val email: String?,
    @field:Json(name = "show_email")
    val showEmail: Int?,
    @field:Json(name = "show_phone")
    val showPhone: Int?,
    @field:Json(name = "subject")
    val subject: String?,
    @field:Json(name = "message")
    val message: String?,
    @field:Json(name = "is_read")
    val isRead: Int?,
    @field:Json(name = "created_at")
    val createdAt: String?,
    @field:Json(name = "updated_at")
    val updatedAt: String?,
    @field:Json(name = "ad")
    val ad: Ad?,
    @field:Json(name = "phone")
    val phone: String?
)