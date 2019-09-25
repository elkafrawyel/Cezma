package com.cezma.app.ui.mainActivity.chatRoom

import com.bumptech.glide.Glide
import com.cezma.app.R
import com.cezma.app.data.model.ChatRoomMessageModel
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

class AdapterChatRoom(data: MutableList<ChatRoomMessageModel>?) :
    BaseMultiItemQuickAdapter<ChatRoomMessageModel, BaseViewHolder>(data) {

    init {
        addItemType(0, R.layout.message_from_item_view)
        addItemType(1, R.layout.message_to_item_view)
    }

    override fun convert(helper: BaseViewHolder?, item: ChatRoomMessageModel) {

        when (helper?.itemViewType) {
            0 -> {
                Glide.with(mContext).load(item.avatarFrom).into(helper.getView(R.id.from_image))
                helper.setText(R.id.from_message,item.message)
//                helper.setText(R.id.from_time,item.createdAt)

            }

            1 -> {
                Glide.with(mContext).load(item.avatarTo).into(helper.getView(R.id.to_image))
                helper.setText(R.id.to_message,item.message)
//                helper.setText(R.id.to_time,item.createdAt)
            }
            else -> {
            }
        }
    }

}