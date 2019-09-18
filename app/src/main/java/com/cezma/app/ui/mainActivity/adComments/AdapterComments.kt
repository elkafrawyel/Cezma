package com.cezma.app.ui.mainActivity.adComments

import android.widget.RatingBar
import com.bumptech.glide.Glide
import com.cezma.app.R
import com.cezma.app.data.model.CommentModel
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

class AdapterComments :
    BaseQuickAdapter<CommentModel, BaseViewHolder>(R.layout.comment_item_view) {

    override fun convert(helper: BaseViewHolder, item: CommentModel) {
        Glide.with(mContext).load(item.avatar).into(helper.getView(R.id.commentUserImage))
        helper.setText(R.id.commentTv, item.comment)
        helper.setText(R.id.commentUserName, item.username)
        helper.setText(R.id.commentTimeTv, item.createdAt)

        if (item.rating > 0) {
            helper.setGone(R.id.commentRating, true)
            helper.getView<RatingBar>(R.id.commentRating).rating = item.rating.toFloat()
        } else {
            helper.setGone(R.id.commentRating, false)
        }
    }

}