package com.cmcc.pp.ui.transactiondata.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import android.widget.ImageView
import android.widget.TextView
import com.cmcc.pp.R
import com.cmcc.pp.entity.transactiondata.RepairOrder

/**
 * Created by shopping on 2018/1/2 17:21.
 * https://github.com/wheroj
 */
class RepairOrderAdapter(val context: Context, val data: ArrayList<RepairOrder>) : BaseExpandableListAdapter() {

    override fun getGroup(groupPosition: Int): RepairOrder {
        return data[groupPosition]
    }

    override fun isChildSelectable(groupPosition: Int, childPosition: Int): Boolean {
        return true
    }

    override fun hasStableIds(): Boolean {
        return false
    }

    override fun getGroupView(groupPosition: Int, isExpanded: Boolean, convertView: View?, parent: ViewGroup?): View {
//        var view: View = View.inflate(context, R.layout.item_repair_parent, null)
        var view = convertView
        val parent: RepairOrder = getGroup(groupPosition)
        val holder: ParentViewHolder?
        if (view == null) {
            view = View.inflate(context, R.layout.item_repair_parent, null)
            holder = ParentViewHolder()
            holder.ivLeft = view!!.findViewById(R.id.itemRepairParent_imageLeft)
            holder.ivRight = view.findViewById(R.id.itemRepairParent_imageRight)
            holder.tvText = view.findViewById(R.id.itemRepairParent_text)

            view.tag = holder
        } else holder = view.tag as ParentViewHolder?

        holder?.ivLeft?.setImageResource(parent.parentImgResId)
        holder?.tvText?.text = parent.itemName
        return view
    }

    override fun getChildrenCount(groupPosition: Int): Int {
        return getGroup(groupPosition).dimensionDetailData.size
    }

    override fun getChild(groupPosition: Int, childPosition: Int): RepairOrder.RepairOderChild {
        return getGroup(groupPosition).dimensionDetailData[childPosition]
    }

    override fun getGroupId(groupPosition: Int): Long {
        return groupPosition.toLong()
    }

    override fun getChildView(groupPosition: Int, childPosition: Int, isLastChild: Boolean, convertView: View?, parent: ViewGroup?): View {
        val child: RepairOrder.RepairOderChild = getChild(groupPosition, childPosition)
        val holder: ChildViewHolder?
        var view = convertView
        if (view != null) {
            holder = view.tag as ChildViewHolder?
        } else {
            view = View.inflate(context, R.layout.item_repair_child, null)

            holder = ChildViewHolder()
            holder.tvLeft = view!!.findViewById(R.id.itemRepairChild_left)
            holder.tvRight = view.findViewById(R.id.itemRepairChild_right)

            view.tag = holder
        }

        holder?.tvLeft?.text = child.dimensionDetailNameStr
        holder?.tvRight?.text = child.dimensionDetailValue
        return view
    }

    override fun getChildId(groupPosition: Int, childPosition: Int): Long {
        return groupPosition + childPosition.toLong()
    }

    override fun getGroupCount(): Int {
        return data.size
    }

    class ChildViewHolder {
        var tvLeft: TextView? = null
        var tvRight: TextView? = null
    }

    class ParentViewHolder {
        var ivLeft: ImageView? = null
        var ivRight: ImageView? = null
        var tvText: TextView? = null
    }

}