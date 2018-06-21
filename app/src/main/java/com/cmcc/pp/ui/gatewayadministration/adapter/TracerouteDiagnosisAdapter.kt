package com.cmcc.pp.ui.gatewayadministration.adapter

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.cmcc.pp.R
import com.cmcc.pp.entity.gateway.RouteHopsListData
import com.cmcc.pp.entity.gateway.WANListData

/**
 * Created by shopping on 2018/1/2 11:03.
 * https://github.com/wheroj
 */
class TracerouteDiagnosisAdapter(data: ArrayList<RouteHopsListData>) : BaseQuickAdapter<RouteHopsListData, BaseViewHolder>(R.layout.item_traceroutediagnosis_result, data) {
    override fun convert(helper: BaseViewHolder?, item: RouteHopsListData?) {
        helper?.itemView!!.findViewById<TextView>(R.id.item_tv_hopHost)
                .text = item?.hopHost
        helper.itemView!!.findViewById<TextView>(R.id.item_tv_hopHostAddress)
                .text = item?.hopHostAddress
        helper.itemView!!.findViewById<TextView>(R.id.item_tv_hopRTTimes)
                .text = item?.hopRTTimes
        helper.itemView!!.findViewById<TextView>(R.id.item_tv_hopErrorCode)
                .text = item?.hopErrorCode

    }
}