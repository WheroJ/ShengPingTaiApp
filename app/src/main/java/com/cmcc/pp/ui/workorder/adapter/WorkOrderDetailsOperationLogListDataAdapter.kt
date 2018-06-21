package com.cmcc.pp.ui.workorder.adapter

import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.cmcc.pp.R
import com.cmcc.pp.entity.WorkOrder.WorkOrderDetailsOperationLogListDataEntity
import com.cmcc.pp.entity.WorkOrder.WorkOrderInfoData

/**
 * Created by shopping on 2018/1/2 11:03.
 * https://github.com/wheroj
 */
class WorkOrderDetailsOperationLogListDataAdapter(data: ArrayList<WorkOrderDetailsOperationLogListDataEntity>) : BaseQuickAdapter<WorkOrderDetailsOperationLogListDataEntity, BaseViewHolder>(R.layout.item_workorderoperationloglistdata, data) {
    override fun convert(helper: BaseViewHolder?, item: WorkOrderDetailsOperationLogListDataEntity?) {
        helper?.itemView!!.findViewById<TextView>(R.id.item_workOrderOperationLogListData_operationLogDayTime)
                .text = item?.operationLogDayTime

        if (item?.operationLogStatus == 1) {
            helper.itemView!!.findViewById<TextView>(R.id.item_workOrderOperationLogListData_operationLogStatus)
                    .text = "成功"
            helper?.itemView!!.findViewById<TextView>(R.id.item_workOrderOperationLogListData_operationLogStatus)
                    .setTextColor(mContext.resources.getColor(R.color.green_0ac))
        } else {
            helper.itemView!!.findViewById<TextView>(R.id.item_workOrderOperationLogListData_operationLogStatus)
                    .text = "失败"
            helper?.itemView!!.findViewById<TextView>(R.id.item_workOrderOperationLogListData_operationLogStatus)
                    .setTextColor(mContext.resources.getColor(R.color.main_red))
        }
    }


}