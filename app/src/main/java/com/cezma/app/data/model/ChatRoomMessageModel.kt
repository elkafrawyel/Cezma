package com.cezma.app.data.model


import com.cezma.app.utiles.Injector
import com.chad.library.adapter.base.entity.MultiItemEntity
import com.squareup.moshi.Json

data class ChatRoomMessageModel(
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
    @field:Json(name = "phone")
    val phone: String?,
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
    @field:Json(name = "avatarto")
    val avatarTo: String?,
    @field:Json(name = "avatarfrom")
    val avatarFrom: String?,
    @field:Json(name = "id_to")
    val idTo: Int?,
    @field:Json(name = "id_from")
    val idFrom: Int?,
    var type:Int
) : MultiItemEntity {
    override fun getItemType(): Int {
        return if (Injector.getPreferenceHelper().id == idFrom) {
            // message to me
            type = 0
            type
        } else {
            //message from me
            type = 1
            type
        }
    }
}