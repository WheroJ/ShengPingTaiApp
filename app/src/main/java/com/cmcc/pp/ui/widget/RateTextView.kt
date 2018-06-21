package com.cmcc.pp.ui.widget

import android.content.Context
import android.graphics.Canvas
import android.support.v7.widget.AppCompatTextView
import android.util.AttributeSet

/**
 * Created by shopping on 2018/1/5 11:26.
 * https://github.com/wheroj
 */
class RateTextView : AppCompatTextView {
    private var hadReMeasure = false
    private var rotate = 60
    constructor(context: Context) : this(context, null)

    constructor(context: Context, attributeSet: AttributeSet?) : this(context, attributeSet, -1)

    constructor(context: Context, attributeSet: AttributeSet?, defStyleAttr: Int) : super(context, attributeSet, defStyleAttr)

    override fun onDraw(canvas: Canvas?) {
        if (!hadReMeasure) {
            var newHeight = ((measuredWidth / 2) * Math.sin(Math.toRadians(rotate.toDouble()))) * 2 + measuredHeight
            layoutParams.height = newHeight.toInt()
            requestLayout()
            hadReMeasure = true
        }

        canvas?.rotate(-rotate.toFloat(), (measuredWidth/2).toFloat(), (measuredHeight/2).toFloat())
        super.onDraw(canvas)
    }
}