package com.cmcc.pp.ui.platformmonitoring.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.cmcc.pp.R

/**
 * Created by shopping on 2018/1/5 11:50.
 * https://github.com/wheroj
 */
class TimeListAdapter(data: ArrayList<String>): BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_time, data) {
    override fun convert(helper: BaseViewHolder?, item: String?) {
        helper?.setText(R.id.itemTime_time, item)
    }
}