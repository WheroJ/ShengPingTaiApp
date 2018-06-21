package com.cmcc.pp.ui.platformmonitoring.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.cmcc.pp.R
import com.cmcc.pp.entity.NetworkElement

/**
 * Created by shopping on 2018/1/3 15:26.
 * https://github.com/wheroj
 */
class RecentNEAdapter(data: List<NetworkElement>)
    : BaseQuickAdapter<NetworkElement, BaseViewHolder>(R.layout.item_network_element_grid, data) {
    override fun convert(helper: BaseViewHolder?, item: NetworkElement?) {
        helper?.setText(R.id.itemNEGrid_name, item?.name)
    }

    fun update(newData: List<NetworkElement>) {
        data.clear()
        data.addAll(newData)
        notifyDataSetChanged()
    }
}