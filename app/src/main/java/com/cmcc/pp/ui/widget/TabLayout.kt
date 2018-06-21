package com.cmcc.pp.ui.widget

import android.annotation.SuppressLint
import android.content.Context
import android.support.annotation.IdRes
import android.util.AttributeSet
import android.view.View
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.RelativeLayout
import com.cmcc.pp.R

/**
 * Created by shopping on 2018/1/4 10:36.
 * https://github.com/wheroj
 */
class TabLayout : RelativeLayout {

    private var leftText: String?
    private var rightText: String?
    private var radioGroup: RadioGroup? = null

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attributeSet: AttributeSet?) : this(context, attributeSet, -1)

    constructor(context: Context, attributeSet: AttributeSet?, defStyleAttr: Int) : super(context, attributeSet, defStyleAttr) {

        val typedArray = context.obtainStyledAttributes(attributeSet, R.styleable.TabLayout)
        leftText = typedArray.getString(R.styleable.TabLayout_leftButton)
        rightText = typedArray.getString(R.styleable.TabLayout_rightButton)
        typedArray.recycle()

        initView()
    }

    interface OnTabCheckedChangeListener {
        fun onCheckedChanged(group: RadioGroup, @IdRes checkedId: Int)
    }

    private var mListener: OnTabCheckedChangeListener? = null
    fun setOnCheckChangeListener(listener: OnTabCheckedChangeListener) {
        mListener = listener
    }

    @SuppressLint("WrongViewCast")
    private fun initView() {
        View.inflate(context, R.layout.layout_tab, this)
        radioGroup = findViewById(R.id.layoutTab_radioGroup)
        radioGroup?.setOnCheckedChangeListener({
            group, checkId
                    -> if(mListener != null) mListener?.onCheckedChanged(group, checkId)
        })
        findViewById<RadioButton>(R.id.layoutTab_left).text = leftText
        findViewById<RadioButton>(R.id.layoutTab_right).text = rightText
    }

    @SuppressLint("WrongViewCast")
    fun setData(left: String, right: String) {
        findViewById<RadioButton>(R.id.layoutTab_left).text = left
        findViewById<RadioButton>(R.id.layoutTab_right).text = right
    }

    fun getCheckedId(): Int {
        return if (radioGroup != null) radioGroup?.checkedRadioButtonId!! else -1
    }
}