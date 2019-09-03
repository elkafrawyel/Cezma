package com.cezma.app.ui.mainActivity.home.messages_fragment

import com.cezma.app.R
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

class AdapterMessages: BaseQuickAdapter<String, BaseViewHolder>(R.layout.messages_item_view) {

    override fun convert(helper: BaseViewHolder, item: String) {
        helper.addOnClickListener(R.id.userImageMessagesItem)


    }

}