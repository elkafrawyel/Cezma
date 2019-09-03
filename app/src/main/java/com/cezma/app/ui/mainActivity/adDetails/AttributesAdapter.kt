package com.cezma.app.ui.mainActivity.adDetails

import com.cezma.app.R
import com.cezma.app.data.model.AttributeModel
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

class AttributesAdapter :
    BaseQuickAdapter<AttributeModel, BaseViewHolder>(R.layout.attributes_item_view) {

    override fun convert(helper: BaseViewHolder, item: AttributeModel) {
        helper.setText(R.id.valueTv, item.value)
        helper.setText(R.id.nameTv, item.name)
    }
}