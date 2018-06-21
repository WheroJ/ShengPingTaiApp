package com.cmcc.pp.ui.gatewayadministration.adapter

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.cmcc.pp.R
import com.cmcc.pp.entity.NetworkElement
import com.cmcc.pp.entity.gateway.WANListData
import com.cmcc.pp.entity.gateway.WarningMessage

/**
 * Created by shopping on 2018/1/2 11:03.
 * https://github.com/wheroj
 */
class WANListDataAdapter(data: ArrayList<WANListData>) : BaseQuickAdapter<WANListData, BaseViewHolder>(R.layout.item_selectwanlink, data) {
    override fun convert(helper: BaseViewHolder?, item: WANListData?) {
        helper?.itemView!!.findViewById<TextView>(R.id.item_tv_connectionType)
                .text = item?.connectionType
        helper.itemView!!.findViewById<TextView>(R.id.item_tv_userName)
                .text = item?.userName
        helper.itemView!!.findViewById<TextView>(R.id.item_tv_vlanIdMark)
                .text = item?.vlanIdMark
        helper.itemView!!.findViewById<TextView>(R.id.item_tv_wanConnectionType)
                .text = item?.wanConnectionType

        if(item?.isChecked!!) {
            helper.itemView!!.findViewById<ImageView>(R.id.item_image_choiced).visibility = View.VISIBLE
        } else {
            helper.itemView!!.findViewById<ImageView>(R.id.item_image_choiced).visibility = View.GONE
        }
    }


}