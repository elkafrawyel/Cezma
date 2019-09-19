package com.cezma.app.data.model

import com.squareup.moshi.Json

data class NotificationsResponse(
    @field:Json(name = "currentpage")
    val currentpage: Int,
    @field:Json(name = "notifications")
    val notifications: List<NotificationModel>,
    @field:Json(name = "pages")
    val pages: Int,
    @field:Json(name = "totalcount")
    val totalcount: Int,
    @field:Json(name = "unreadcount")
    val unreadcount: Int
)

data class NotificationModel(
    @field:Json(name = "ad_id")
    val adId: String,
    @field:Json(name = "ad_img")
    val adImg: String,
    @field:Json(name = "ad_title")
    val adTitle: String,
    @field:Json(name = "comment_id")
    val commentId: String,
    @field:Json(name = "created_at")
    val createdAt: String,
    @field:Json(name = "id")
    val id: Int,
    @field:Json(name = "is_read")
    val isRead: Int,
    @field:Json(name = "message")
    val message: String,
    @field:Json(name = "type")
    val type: String,
    @field:Json(name = "typenumber")
    val typenumber: Int,
    @field:Json(name = "user_id")
    val userId: Int
)
