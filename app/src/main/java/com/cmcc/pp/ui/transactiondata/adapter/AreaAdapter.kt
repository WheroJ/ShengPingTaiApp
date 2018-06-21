package com.cmcc.pp.ui.transactiondata.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.cmcc.pp.R
import com.cmcc.pp.entity.transactiondata.AreaItem

/**
 * Created by shopping on 2018/1/3 15:26.
 * https://github.com/wheroj
 */
class AreaAdapter(data: List<AreaItem>)
    : BaseQuickAdapter<AreaItem, BaseViewHolder>(R.layout.item_network_element_list, data) {
    override fun convert(helper: BaseViewHolder?, item: AreaItem?) {
        helper?.setText(R.id.itemNEList_name, item?.areaName)
    }
}