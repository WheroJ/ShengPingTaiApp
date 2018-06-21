package com.cmcc.pp.ui.transactiondata.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.cmcc.pp.R
import com.cmcc.pp.entity.transactiondata.CommonItem

/**
 * Created by shopping on 2018/1/2 10:56.
 * https://github.com/wheroj
 */
class CommonAdapter(data: ArrayList<CommonItem>) : BaseQuickAdapter<CommonItem, BaseViewHolder>(R.layout.item_bind, data) {
    override fun convert(helper: BaseViewHolder?, item: CommonItem?) {
        helper?.setText(R.id.itemCommon_centerText, item?.centerText)
                ?.setText(R.id.itemCommon_bottomText, item?.bottomText)
                ?.setImageResource(R.id.itemCommon_img, item?.imgResId!!)
    }
}