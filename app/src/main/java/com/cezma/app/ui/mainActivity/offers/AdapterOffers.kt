package com.cezma.app.ui.mainActivity.offers

import com.bumptech.glide.Glide
import com.cezma.app.R
import com.cezma.app.data.model.OfferModel
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

class AdapterOffers : BaseQuickAdapter<OfferModel, BaseViewHolder>(R.layout.offer_item_view) {

    override fun convert(helper: BaseViewHolder, item: OfferModel?) {

        Glide.with(mContext).load(item?.thumbnails!![0]).into(helper.getView(R.id.productImgv))
        Glide.with(mContext).load(item.offersenderavatar)
            .into(helper.getView(R.id.offerItemUserImage))
        helper.setText(R.id.adTitleTv, item.adName)
        helper.setText(R.id.productPriceTv, item.price.toString())
        helper.setText(R.id.offerPrice, item.mainPrice.toString())
        helper.setText(R.id.adCategoryTv, item.maincategoryName)
        helper.setText(R.id.adSubCategoryTv, item.subcategoryName)
        helper.setText(R.id.offerItemUserName, item.offersender)

        if (item.isAccepted == null) {
            helper.setGone(R.id.linearActions,true)
            helper.setGone(R.id.actionResult, false)
        }else if(item.isAccepted ==1){

            helper.setGone(R.id.linearActions,false)
            helper.setGone(R.id.actionResult, true)
            helper.setText(R.id.actionResult,mContext.resources.getString(R.string.accepted))
        }

        helper.addOnClickListener(R.id.yes, R.id.no, R.id.offerItem)
    }

}