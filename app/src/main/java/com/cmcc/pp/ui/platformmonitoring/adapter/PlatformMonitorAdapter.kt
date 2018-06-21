package com.cmcc.pp.ui.platformmonitoring.adapter

import android.content.Context
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.cmcc.pp.R
import com.cmcc.pp.base.ViewHolder
import com.cmcc.pp.util.UIUtils

/**
 * Created by shopping on 2018/1/25 11:08.
 * https://github.com/wheroj
 */
class PlatformMonitorAdapter(data: HashMap<String, ArrayList<HashMap<String, HashMap<String, String>>>>?, context: Context): RecyclerView.Adapter<PlatformMonitorHolder>() {

    private var mData: HashMap<String, ArrayList<HashMap<String, HashMap<String, String>>>>? = null
    private var mContext: Context

    private var dataSize: Int = 0
    private var iterator: Iterator<String>? = null

    init {
        mData = data
        this.mContext = context

        if (mData != null) {
            iterator = mData!!.keys.iterator()
            while (iterator!!.hasNext()) {
                iterator!!.next()
                dataSize++
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): PlatformMonitorHolder {
        var recyclerView = RecyclerView(mContext)
        var layoutParams: RecyclerView.LayoutParams = RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        recyclerView.setPadding(UIUtils.dip2px(15), UIUtils.dip2px(10), UIUtils.dip2px(15), UIUtils.dip2px(10))
        layoutParams.setMargins(0, UIUtils.dip2px(15), 0, 0)
        recyclerView.layoutParams = layoutParams
        recyclerView.setBackgroundColor(mContext.resources.getColor(R.color.white))
        recyclerView.layoutManager = GridLayoutManager(mContext, 3, GridLayoutManager.VERTICAL, false)
        return PlatformMonitorHolder(recyclerView, mContext)
    }

    override fun getItemCount(): Int {
        return dataSize
    }

    override fun onBindViewHolder(holder: PlatformMonitorHolder?, position: Int) {
        holder?.setData(getItem(position), position)
    }

    private fun getItem(position: Int): ArrayList<HashMap<String, java.util.HashMap<String, String>>>? {
        return mData?.get((position + 1).toString())
    }
}

class PlatformMonitorHolder(itemView: View, private var context: Context): ViewHolder<ArrayList<HashMap<String, HashMap<String, String>>>>(itemView) {

    lateinit var recyclerView: RecyclerView
    init {
        if (itemView is RecyclerView) {
            recyclerView = itemView
        }
    }
    override fun setData(data: ArrayList<HashMap<String, HashMap<String, String>>>?, position: Int) {
        recyclerView.adapter = PlatformMonitorModuleAdapter(data!!, context)
    }
}