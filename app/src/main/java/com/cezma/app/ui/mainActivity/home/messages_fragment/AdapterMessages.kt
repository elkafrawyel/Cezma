package com.cezma.app.ui.mainActivity.home.messages_fragment

import com.bumptech.glide.Glide
import com.cezma.app.R
import com.cezma.app.data.model.MessageModel
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

class AdapterMessages :
    BaseQuickAdapter<MessageModel, BaseViewHolder>(R.layout.messages_item_view) {

    override fun convert(helper: BaseViewHolder, item: MessageModel) {
        helper.addOnClickListener(R.id.messageItem)

        helper.setText(R.id.userNameMessagesItem, item.msgFrom)
        helper.setText(R.id.messageTextMessagesItem, item.message)
        helper.setText(R.id.messageTimeMessagesItem, item.createdAt)
        Glide.with(mContext).load(item.ad!!.thumbnails[0])
            .into(helper.getView(R.id.userImageMessagesItem))
    }

}