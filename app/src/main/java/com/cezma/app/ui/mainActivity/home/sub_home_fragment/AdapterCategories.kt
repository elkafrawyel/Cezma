package com.cezma.app.ui.mainActivity.home.sub_home_fragment

import com.cezma.app.R
import com.cezma.app.data.model.CategoryModel
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

class AdapterCategories : BaseQuickAdapter<CategoryModel, BaseViewHolder>(R.layout.category_home_item_view) {

    override fun convert(helper: BaseViewHolder, item: CategoryModel) {
        helper.setText(R.id.categoryName,item.categoryName)
        helper.addOnClickListener(R.id.categoryName)
    }

}