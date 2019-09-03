package com.cezma.app.ui.mainActivity.followingShops

import com.bumptech.glide.Glide
import com.cezma.app.R
import com.cezma.app.data.model.StoreModel
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

class AdapterFollowingShops : BaseQuickAdapter<StoreModel, BaseViewHolder>(R.layout.following_shops_item_view) {

    override fun convert(helper: BaseViewHolder, item: StoreModel) {
        Glide.with(mContext).load(item.cover).into(helper.getView(R.id.followingShopImage))
        Glide.with(mContext).load(item.logo).into(helper.getView(R.id.imageFollowingShopsItem))
        helper.setText(R.id.followingShopsTitle,item.title)

        helper.addOnClickListener(R.id.storeItem)
    }

}