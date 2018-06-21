package com.cmcc.pp.ui.widget

import android.content.Context
import android.util.AttributeSet
import android.widget.ExpandableListView


/**
 * Created by shopping on 2018/1/3 9:56.
 * https://github.com/wheroj
 */
class CustomExpandableListView(context: Context, attrs: AttributeSet)
    : ExpandableListView(context, attrs) {

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE shr 2,

                MeasureSpec.AT_MOST)

        super.onMeasure(widthMeasureSpec, expandSpec)
    }
}