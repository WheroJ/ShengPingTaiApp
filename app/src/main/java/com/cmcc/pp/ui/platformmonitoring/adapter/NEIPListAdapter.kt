package com.cmcc.pp.ui.platformmonitoring.adapter

import android.widget.LinearLayout
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.cmcc.pp.R
import com.cmcc.pp.util.IntentUtils

/**
 * Created by shopping on 2018/1/25 15:19.
 * https://github.com/wheroj
 */
class NEIPListAdapter(data: ArrayList<HashMap<String, String>>)
    : BaseQuickAdapter<HashMap<String, String>, BaseViewHolder>(R.layout.item_serverlist, data) {
    private val status = arrayOf("", "正常", "无法连接", "服务未启动", "数据异常")

    override fun convert(helper: BaseViewHolder?, item: HashMap<String, String>?) {
        val iterator = item!!.keys.iterator()

        var key = ""
        while (iterator.hasNext()) {
            key = iterator.next()
        }

        helper?.setText(R.id.item_seriverlist_ip, key)

        try {
            /**
             * 4：数据异常  3：服务未启动  2：无法连接  1：正常
             */
            val value = item[key]!!.toInt()
            if (value < status.size) {
                helper?.setText(R.id.item_seriverlist_status, status[value])
            }

            if (value == 1) {
                //正常
                helper?.setTextColor(R.id.item_seriverlist_status, mContext.resources.getColor(R.color.gray_light))
            } else {
                helper?.setTextColor(R.id.item_seriverlist_status, mContext.resources.getColor(R.color.main_red))
            }
        } catch (e: NumberFormatException) {
            e.printStackTrace()
        }

        helper?.getView<LinearLayout>(R.id.item_lin_seriverlist)
                ?.setOnClickListener{
                    IntentUtils.openServerDetails(mContext, key)
                }
    }

}