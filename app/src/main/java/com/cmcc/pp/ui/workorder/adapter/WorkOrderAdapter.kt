package com.cmcc.pp.ui.workorder.adapter

import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.cmcc.pp.R
import com.cmcc.pp.entity.WorkOrder.WorkOrderInfoData

/**
 * Created by shopping on 2018/1/2 11:03.
 * https://github.com/wheroj
 */
class WorkOrderAdapter(data: ArrayList<WorkOrderInfoData>) : BaseQuickAdapter<WorkOrderInfoData, BaseViewHolder>(R.layout.item_queryresultsforworkorder, data) {
    override fun convert(helper: BaseViewHolder?, item: WorkOrderInfoData?) {
        helper?.itemView!!.findViewById<TextView>(R.id.item_tv_workOrderNo)
                .text = item?.workOrderNo

        if (item?.businessType == 1) {
            helper.itemView!!.findViewById<TextView>(R.id.item_tv_businessType)
                    .text = "新装"
        } else if (item?.businessType == 2) {
            helper.itemView!!.findViewById<TextView>(R.id.item_tv_businessType)
                    .text = "缴费开机"
        } else if (item?.businessType == 3) {
            helper.itemView!!.findViewById<TextView>(R.id.item_tv_businessType)
                    .text = "欠费停机"
        } else if (item?.businessType == 4) {
            helper.itemView!!.findViewById<TextView>(R.id.item_tv_businessType)
                    .text = "客户申请停机"
        } else if (item?.businessType == 5) {
            helper.itemView!!.findViewById<TextView>(R.id.item_tv_businessType)
                    .text = "客户申请复机"
        } else if (item?.businessType == 6) {
            helper.itemView!!.findViewById<TextView>(R.id.item_tv_businessType)
                    .text = "订户密码变更"
        } else if (item?.businessType == 7) {
            helper.itemView!!.findViewById<TextView>(R.id.item_tv_businessType)
                    .text = "修改速率信息"
        } else if (item?.businessType == 8) {
            helper.itemView!!.findViewById<TextView>(R.id.item_tv_businessType)
                    .text = "拆机"
        } else if (item?.businessType == 9) {
            helper.itemView!!.findViewById<TextView>(R.id.item_tv_businessType)
                    .text = "修改"
        }

        helper.itemView!!.findViewById<TextView>(R.id.item_tv_wbandAccount)
                .text = item?.wbandAccount
        helper.itemView!!.findViewById<TextView>(R.id.item_tv_loid)
                .text = item?.loid
        helper.itemView!!.findViewById<TextView>(R.id.item_tv_workOrderDayTime)
                .text = item?.workOrderDayTime

        if (item?.accountMatchingFlag == 1) {
            helper?.itemView!!.findViewById<TextView>(R.id.item_tv_workOrderNo)
                    .setTextColor(mContext.resources.getColor(R.color.main_blue))
        } else if (item?.accountMatchingFlag == 2) {
            helper?.itemView!!.findViewById<TextView>(R.id.item_tv_wbandAccount)
                    .setTextColor(mContext.resources.getColor(R.color.main_blue))
        } else if (item?.accountMatchingFlag == 3) {
            helper?.itemView!!.findViewById<TextView>(R.id.item_tv_loid)
                    .setTextColor(mContext.resources.getColor(R.color.main_blue))
        }
    }



}