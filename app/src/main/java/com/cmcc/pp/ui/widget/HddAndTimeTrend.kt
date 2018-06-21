package com.cmcc.pp.ui.widget

import android.content.Context
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import com.cmcc.pp.R
import com.cmcc.pp.ui.platformmonitoring.adapter.TimeListAdapter

/**
 * Created by shopping on 2018/1/5 13:39.
 * https://github.com/wheroj
 */
class HddAndTimeTrend : LinearLayout {

    private lateinit var trendEndNum: TrendView
    private lateinit var trendHDDCapacity: TrendView
    private lateinit var timeList: RecyclerView
    private lateinit var trendTab: TabLayout

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attributeSet: AttributeSet?) : this(context, attributeSet, -1)

    constructor(context: Context, attributeSet: AttributeSet?, defStyleAttr: Int) : super(context, attributeSet, defStyleAttr) {
        View.inflate(context, R.layout.layout_hdd_capability, this)

        initView()
    }

    fun initView() {
        trendEndNum = findViewById(R.id.activityServerDetail_trendEndNum)
        trendTab = findViewById(R.id.activityServerDetail_trendTab)
        trendHDDCapacity = findViewById(R.id.activityServerDetail_trendHDDCapacity)
        timeList = findViewById(R.id.activityServerDetail_timeList)
        timeList.layoutManager = GridLayoutManager(context, 7, GridLayoutManager.VERTICAL, false)
        orientation = LinearLayout.VERTICAL
    }

    fun getTrendTab(): TabLayout {
        return trendTab
    }

    /**
     * 设置图表数据
     */
    fun setData(trendNumList: List<HashMap<String, String>>, trendHDDList: List<HashMap<String, String>>) {
        var trendNumData: ArrayList<String> = ArrayList()
        var trendHDDData: ArrayList<String> = ArrayList()
        var timeListData: ArrayList<String> = ArrayList()
        for (i in trendNumList.indices) {
            trendNumList[i]["terminalNum"]?.let { trendNumData.add(it) }
            trendHDDList[i]["capacity"]?.let { trendHDDData.add(it) }
            trendHDDList[i]["time"]?.let { timeListData.add(it) }
        }
        trendEndNum.setLineColor(resources.getColor(R.color.main_blue))
        trendEndNum.setData(trendNumData)
        trendHDDCapacity.setLineColor(resources.getColor(R.color.main_yellow))
        trendHDDCapacity.setData(trendHDDData)
        timeList.adapter = TimeListAdapter(timeListData)
    }
}