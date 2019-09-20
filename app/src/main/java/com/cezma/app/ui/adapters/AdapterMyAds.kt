package com.cezma.app.ui.adapters

import com.bumptech.glide.Glide
import com.cezma.app.R
import com.cezma.app.data.model.Ad
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

class AdapterMyAds : BaseQuickAdapter<Ad, BaseViewHolder>(R.layout.ad_item_view) {

    override fun convert(helper: BaseViewHolder, item: Ad) {

        helper.setText(R.id.adTitleTv, item.title)
        helper.setText(R.id.productPriceTv, "${item.price} ${item.currency}")
        helper.setText(R.id.productByWhoTv, item.username)
        helper.setText(R.id.adCategoryTv, item.categoryName)
        helper.setText(R.id.adSubCategoryTv, item.subCategoryName)

        if (item.isFeatured == 1){
            helper.setVisible(R.id.featured,true)
            helper.setVisible(R.id.upgradeTv,false)
        }else{
            helper.setVisible(R.id.featured,false)
            helper.setVisible(R.id.upgradeTv,true)
        }

        Glide.with(mContext).load(item.photos[0]).into(helper.getView(R.id.productImgv))
        helper.addOnClickListener(R.id.adItem,R.id.upgradeTv)
    }

}