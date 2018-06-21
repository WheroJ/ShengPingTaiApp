package com.cmcc.pp.ui.platformmonitoring.adapter

import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.cmcc.pp.R
import com.cmcc.pp.entity.platformmonitoring.DialingDetailEntity

/**
 * Created by shopping on 2018/1/2 11:03.
 * https://github.com/wheroj
 */
class DialingDetailAdapter(data: ArrayList<DialingDetailEntity>) : BaseQuickAdapter<DialingDetailEntity, BaseViewHolder>(R.layout.item_resultforsericedialing, data) {
    override fun convert(helper: BaseViewHolder?, item: DialingDetailEntity?) {
        helper?.itemView!!.findViewById<TextView>(R.id.result_tv_step)
                .text = ("第"+item?.stage+"步")
        helper.itemView!!.findViewById<TextView>(R.id.result_tv_dialingMessage)
                .text = item?.dialingMessage
        helper.itemView!!.findViewById<TextView>(R.id.result_tv_beginTime)
                .text = item?.beginTime



        if(item?.status.equals("0")){
            helper.itemView!!.findViewById<TextView>(R.id.result_tv_status)
                    .text = "成功"
            helper.itemView!!.findViewById<TextView>(R.id.result_tv_status)
                    .setTextColor(mContext.resources.getColor(R.color.green_0ac))
        }else{
            helper.itemView!!.findViewById<TextView>(R.id.result_tv_status)
                    .text = "失败"
            helper.itemView!!.findViewById<TextView>(R.id.result_tv_status)
                    .setTextColor(mContext.resources.getColor(R.color.main_red))
        }
    }


}