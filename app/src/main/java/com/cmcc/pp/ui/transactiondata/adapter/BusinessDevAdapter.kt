package com.cmcc.pp.ui.transactiondata.adapter

import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.cmcc.pp.R
import com.cmcc.pp.entity.transactiondata.BusinessDev
import com.cmcc.pp.ui.widget.BarGraphView

/**
 * Created by shopping on 2018/1/2 11:03.
 * https://github.com/wheroj
 */
class BusinessDevAdapter(data: ArrayList<BusinessDev>) : BaseQuickAdapter<BusinessDev, BaseViewHolder>(R.layout.item_business_dev, data) {
    override fun convert(helper: BaseViewHolder?, item: BusinessDev?) {
        helper?.itemView!!.findViewById<TextView>(R.id.itemBusinessDev_tvCompanyName)
                .text = item?.manufacturerName
        helper.itemView!!.findViewById<BarGraphView>(R.id.itemBusinessDev_bar1)
                ?.setData(item?.inventoryRatio!!, BarGraphView.BLUE, item.bePutInStorageTotalQuantity)
        helper.itemView!!.findViewById<BarGraphView>(R.id.itemBusinessDev_bar2)
                ?.setData(item?.putPackRatio!!, BarGraphView.YELLOW, item.installQuantity)
    }
}