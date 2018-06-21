package com.cmcc.pp.ui.gatewayadministration.adapter

import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.cmcc.pp.R
import com.cmcc.pp.entity.gateway.WarningMessage
import com.cmcc.pp.entity.transactiondata.BusinessDev
import com.cmcc.pp.ui.widget.BarGraphView

/**
 * Created by shopping on 2018/1/2 11:03.
 * https://github.com/wheroj
 */
class WarningMessageAdapter(data: ArrayList<WarningMessage>) : BaseQuickAdapter<WarningMessage, BaseViewHolder>(R.layout.item_gateway_warningmessage, data) {
    override fun convert(helper: BaseViewHolder?, item: WarningMessage?) {
        helper?.itemView!!.findViewById<TextView>(R.id.item_warningmessage_alarmName)
                .text = item?.alarmName
        helper.itemView!!.findViewById<TextView>(R.id.item_warningmessage_alarmNum)
                .text = item?.alarmNum
        helper.itemView!!.findViewById<TextView>(R.id.item_warningmessage_alarmType)
                .text = item?.alarmType
        helper.itemView!!.findViewById<TextView>(R.id.item_warningmessage_alarmToggle)
                .text = item?.alarmToggle
        helper.itemView!!.findViewById<TextView>(R.id.item_warningmessage_alarmAppendInfo)
                .text = item?.alarmAppendInfo

    }

}