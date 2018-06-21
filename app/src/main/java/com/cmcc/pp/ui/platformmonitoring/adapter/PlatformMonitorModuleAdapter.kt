package com.cmcc.pp.ui.platformmonitoring.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import com.cmcc.pp.R
import com.cmcc.pp.base.ViewHolder
import com.cmcc.pp.entity.NetworkElement
import com.cmcc.pp.util.IntentUtils
import java.util.*

/**
 * Created by shopping on 2018/1/25 10:09.
 * https://github.com/wheroj
 */
class PlatformMonitorModuleAdapter(data: ArrayList<HashMap<String, HashMap<String, String>>>?, context: Context)
    : RecyclerView.Adapter<PlatformMonitorModuleHolder>() {

    private var context: Context? = null
    private var mData: ArrayList<HashMap<String, HashMap<String, String>>>? = null
    init {
        this.context = context
        this.mData = data
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): PlatformMonitorModuleHolder {
        val view = View.inflate(context, R.layout.item_paltformmonitoring, null)
        return PlatformMonitorModuleHolder(view, context)
    }

    override fun getItemCount(): Int {
        if (mData == null)
            return 0
        return mData!!.size
    }

    override fun onBindViewHolder(moduleHolder: PlatformMonitorModuleHolder?, position: Int) {
        moduleHolder?.setData(mData!![position], position)
    }
}

class PlatformMonitorModuleHolder(item: View, private var context: Context?) : ViewHolder<HashMap<String, HashMap<String, String>>>(item) {

    private var platformName: TextView = itemView.findViewById(R.id.paltform_name)
    private var platformRadio: TextView = itemView.findViewById(R.id.platform_radio)
    private var platformStatus: ImageView = itemView.findViewById(R.id.paltform_status)
    private var platformItem: RelativeLayout = itemView.findViewById(R.id.paltform_item)
    private var platformBottom: LinearLayout = itemView.findViewById(R.id.paltform_bottom)

    override fun setData(data: HashMap<String, HashMap<String, String>>?, position: Int) {
        val iterator = data!!.keys.iterator()
        while (iterator.hasNext()) {
            var key = iterator.next()
            platformName.text = key
            val radio = data[key]!!["normalRatio"].toString()
            val split = radio.split("/")
            if(split != null && split.size == 2) {
                try {
                    val firstInt = split[0].toInt()
                    val secondInt = split[1].toInt()
                    if (firstInt == secondInt) {
                        platformStatus.setImageResource(R.mipmap.icon_green_point)
                        platformItem.setBackgroundResource(R.drawable.bg_white_lin_gare_4radius)
                        platformBottom.setBackgroundColor(context!!.resources.getColor(R.color.gray_f2))
                    } else {
                        platformStatus.setImageResource(R.mipmap.icon_red_point)
                        platformItem.setBackgroundResource(R.drawable.bg_white_lin_red_4radius)
                        platformBottom.setBackgroundColor(context!!.resources.getColor(R.color.red_alpha15))
                    }
                } catch (e: NumberFormatException) {
                    e.printStackTrace()
                }
            }
            platformRadio.text = radio

            itemView.setOnClickListener{
                var element = NetworkElement()
                element.name = key
                element.isRecent = 2
                element.recentTime = System.currentTimeMillis()
                element.saveOrUpdate("(name='$key')")
                IntentUtils.openServerlist(context, element)
            }
        }
    }
}
