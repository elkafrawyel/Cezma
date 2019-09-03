package com.cezma.app.ui.mainActivity.favourites

import com.bumptech.glide.Glide
import com.cezma.app.R
import com.cezma.app.data.model.FavoriteAd
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

class AdapterFavourites: BaseQuickAdapter<FavoriteAd, BaseViewHolder>(R.layout.favourites_item_view) {

    override fun convert(helper: BaseViewHolder, item: FavoriteAd) {

        helper.setText(R.id.adTitleTv, item.adName)
        Glide.with(mContext).load(item.thumbnails[0]).into(helper.getView(R.id.productImgv))
        helper.addOnClickListener(R.id.adItem,R.id.favouriteItemFavImg)
    }

}