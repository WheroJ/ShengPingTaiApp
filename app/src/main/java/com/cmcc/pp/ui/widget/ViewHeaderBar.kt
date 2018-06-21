package com.cmcc.pp.ui.widget

import android.content.Context
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.cmcc.pp.R

/**
 * Created by shopping on 2017/12/25 10:06.
 * https://github.com/wheroj
 */
class ViewHeaderBar : LinearLayout {
    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, -1)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private var mTvBack: TextView? = null
    private var mIvBack: ImageView? = null
    private var mIvRight: ImageView? = null
    private var mTvSubTitle: TextView? = null
    private var mTvTitle: TextView? = null
    private var mTvRight:TextView? = null
    private var mLlCenter: LinearLayout? = null

    init {

        View.inflate(context, R.layout.layout_viewheaderbar, this)
        mTvBack = findViewById(R.id.headerBar_tvBack)
        mLlCenter = findViewById(R.id.headerBar_center)
        mTvTitle = findViewById(R.id.headerBar_tvTitle)
        mTvSubTitle = findViewById(R.id.headerBar_tvSubTitle)
        mIvBack = findViewById(R.id.headerBar_ivBack)
        mTvRight = findViewById(R.id.headerBar_tvRightTitle)
        mIvRight = findViewById(R.id.headerBar_ivRight)

        mIvBack?.setOnClickListener {
            mListener?.onBackClick(CLICK_LEFT)
        }
        mTvBack?.setOnClickListener {
            mListener?.onBackClick(CLICK_LEFT)
        }

        mLlCenter?.setOnClickListener{
            mListener?.onCenterClick(CLICK_CENTER)
        }

        mTvRight?.setOnClickListener{
            mListener?.onRightClick(CLICK_RIGTH)
        }

        mIvRight?.setOnClickListener{
            mListener?.onRightClick(CLICK_RIGTH)
        }
    }

    /**
     * 左边显示文字和图片
     */
    fun showLeftTextAndDrawable(backText: String, resId: Int) {
        showLeftText(backText)
        val drawable = ContextCompat.getDrawable(context, resId)
        /// 这一步必须要做,否则不会显示.
        drawable.setBounds(0, 0, drawable.minimumWidth, drawable.minimumHeight)
        mTvBack?.setCompoundDrawables(drawable, null, null, null)
    }

    /**
     * 右边显示文字和图片
     */
    fun showRightTextAndDrawable(rightText: String, resId: Int) {
        showRightText(rightText)
        val drawable = ContextCompat.getDrawable(context, resId)
        /// 这一步必须要做,否则不会显示.
        drawable.setBounds(0, 0, drawable.minimumWidth, drawable.minimumHeight)
        mTvBack?.setCompoundDrawables(null, null, drawable, null)
    }

    /**
     * 左边显示文字
     */
    fun showLeftText(backText: String) {
        mIvBack?.visibility = View.GONE
        mTvBack?.visibility = View.VISIBLE
        mTvBack?.text = backText
    }

    fun showLeftText(backText: Int) {
        mIvBack?.visibility = View.GONE
        mTvBack?.visibility = View.VISIBLE
        mTvBack?.setText(backText)
    }

    fun setLeftTextColor(resId: Int){
        mTvBack?.setTextColor(resources.getColor(resId))
    }


    /**
     * 左边显示图片： 默认
     */
    fun showLeftImage(resId: Int) {
        mIvBack?.visibility = View.VISIBLE
        mTvBack?.visibility = View.GONE
        mIvBack?.setImageResource(resId)
    }

    /**
     * 右边显示图片
     */
    fun showRightImage(resId: Int) {
        mIvRight?.visibility = View.VISIBLE
        mTvRight?.visibility = View.GONE
        mIvRight?.setImageResource(resId)
    }

    /**
     * 设置大标题
     */
    fun setTitle(title: String) {
        mTvTitle?.visibility = View.VISIBLE
        mTvTitle?.text = title
    }

    /**
     * 设置大标题
     */
    fun setTitle(title: Int) {
        mTvTitle?.visibility = View.VISIBLE
        mTvTitle?.setText(title)
    }


    /**
     * 设置大标题和小标题
     */
    fun setTitleAndSub(title: String, subTitle: String) {
        setTitle(title)
        mTvSubTitle?.text = subTitle

    }

    /**
     * 设置小标题
     */

    fun setSubTitle(subTitle: String) {
        mTvSubTitle?.visibility = View.VISIBLE
        mTvSubTitle?.text = subTitle

    }

    /**
     * 隐藏左边图片和文字
     */

    fun setLeftAndImageGone(){
        mIvBack?.visibility = View.GONE
        mTvBack?.visibility = View.GONE
    }


    /**
     * 右边边显示文字
     */
    fun showRightText(rightText: String) {
        mTvRight?.visibility = View.VISIBLE
        mTvRight?.text = rightText
    }

    fun showRightText(rightText: Int) {
        mTvRight?.visibility = View.VISIBLE
        mTvRight?.setText(rightText)
    }

    interface OnViewClickListener {
        fun onBackClick(viewType: Int)
        fun onCenterClick(viewType: Int)
        fun onRightClick(viewType: Int)
    }

    private var mListener: OnViewClickListener? = null
    fun setOnViewClickListener(listener: OnViewClickListener) {
        mListener = listener
    }

    companion object {
        val CLICK_LEFT: Int = 1
        val CLICK_RIGTH: Int = 2
        val CLICK_CENTER: Int = 3

    }
}