package com.cezma.app.ui.mainActivity.home.notifications_fragment

import com.bumptech.glide.Glide
import com.cezma.app.R
import com.cezma.app.data.model.NotificationModel
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

class AdapterNotifications : BaseQuickAdapter<NotificationModel, BaseViewHolder>(R.layout.notification_item_view) {

    override fun convert(helper: BaseViewHolder, item: NotificationModel) {

        helper.setText(R.id.userNameNotificationItem, item.adTitle)
        helper.setText(R.id.notificationTextNotificationItem, item.message)
        helper.setText(R.id.timeNotificationItem, item.createdAt)


        Glide.with(mContext).load(item.adImg).into(helper.getView(R.id.imageNotificationItem))
        helper.addOnClickListener(R.id.notis_item)
    }

}