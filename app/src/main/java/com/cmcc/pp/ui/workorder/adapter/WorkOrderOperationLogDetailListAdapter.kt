package com.cmcc.pp.ui.workorder.adapter

import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.cmcc.pp.R
import com.cmcc.pp.entity.WorkOrder.WorkOrderOperationLogDetailEntity

/**
 * Created by shopping on 2018/1/2 11:03.
 * https://github.com/wheroj
 */
class WorkOrderOperationLogDetailListAdapter(data: ArrayList<WorkOrderOperationLogDetailEntity>) : BaseQuickAdapter<WorkOrderOperationLogDetailEntity, BaseViewHolder>(R.layout.item_operationnote, data) {
    override fun convert(helper: BaseViewHolder?, item: WorkOrderOperationLogDetailEntity?) {
        helper?.itemView!!.findViewById<TextView>(R.id.item_tv_operationLogDetailStepName)
                .text = item?.operationLogDetailStepName

        helper?.itemView!!.findViewById<TextView>(R.id.item_tv_operationLogDetailBeginDayTime)
                .text = item?.operationLogDetailBeginDayTime

        helper?.itemView!!.findViewById<TextView>(R.id.item_tv_operationLogDetailEndDayTime)
                .text = item?.operationLogDetailEndDayTime


        if (item?.operationLogDetailStatus == 1) {
            helper?.itemView!!.findViewById<TextView>(R.id.item_tv_operationLogDetailStatus)
                    .text = "成功"
            helper?.itemView!!.findViewById<TextView>(R.id.item_tv_workOrderNo)
                    .setTextColor(mContext.resources.getColor(R.color.green_0ac))
        } else{
            helper?.itemView!!.findViewById<TextView>(R.id.item_tv_wbandAccount)
                    .setTextColor(mContext.resources.getColor(R.color.main_red))
            helper?.itemView!!.findViewById<TextView>(R.id.item_tv_operationLogDetailStatus)
                    .text = "失败"
        }
    }



}