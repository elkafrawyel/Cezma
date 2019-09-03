package com.cezma.app.ui.mainActivity.subCategory

import com.bumptech.glide.Glide
import com.cezma.app.R
import com.cezma.app.data.model.SubCategoryModel
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

class AdapterSubCategories : BaseQuickAdapter<SubCategoryModel, BaseViewHolder>(R.layout.category_item_view) {

    override fun convert(helper: BaseViewHolder, item: SubCategoryModel) {

        Glide.with(mContext).load(item.icon).into(helper.getView(R.id.imageCategoryItem))
        helper.setText(R.id.categoryNameTv, item.categoryName)

        helper.addOnClickListener(R.id.categoryItem)

    }

}