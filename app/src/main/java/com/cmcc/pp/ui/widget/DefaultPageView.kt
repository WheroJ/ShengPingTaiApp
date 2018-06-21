package com.cmcc.pp.ui.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.cmcc.pp.R

/**
 * Created by shopping on 2017/12/25 11:09.
 * https://github.com/wheroj
 */
class DefaultPageView: LinearLayout {
    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, -1)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr : Int) : super(context, attrs, defStyleAttr)

    private var mIvError: ImageView? = null
    private var mTvErrorText: TextView? = null
    init {
        View.inflate(context, R.layout.layout_defaultpage, this)
        mTvErrorText = findViewById(R.id.defaultPageView_text)
        mIvError = findViewById(R.id.defaultPageView_image)
    }

    /**
     * 错误图片和文本
     */
    fun setErrorImageAndText(resId: Int, text: String) {
        mIvError?.visibility = View.VISIBLE
        mTvErrorText?.visibility = View.VISIBLE
        mIvError?.setImageResource(resId)
        mTvErrorText?.text = text
    }


    /**
     * 设置错误图片
     */
    fun setErrorImage(resId: Int) {
        mIvError?.visibility = View.VISIBLE
        mTvErrorText?.visibility = View.VISIBLE
        mIvError?.setImageResource(resId)
    }

    /**
     * 设置错误文本
     */
    fun setErrorText(text: String) {
        mIvError?.visibility = View.VISIBLE
        mTvErrorText?.visibility = View.VISIBLE
        mTvErrorText?.text = text
    }
}