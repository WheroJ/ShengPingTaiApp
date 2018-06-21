package com.cmcc.pp.ui.widget

import android.animation.Animator
import android.animation.IntEvaluator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.drawable.Drawable
import android.support.v4.content.ContextCompat
import android.text.TextUtils
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import com.cmcc.pp.R
import com.cmcc.pp.util.UIUtils
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

/**
 * Created by shopping on 2017/12/26 9:15.
 * https://github.com/wheroj
 */
class RefreshLinearLayout : LinearLayout {
    private var mTvText: TextView? = null
    private var mIvIcon: ImageView? = null
    private var mRlContent: RelativeLayout? = null
    private var mHeaderBackground: Int
    private var mHeaderIcon: Drawable? = null
    private var mHeaderText: String? = null
    private var mHeaderStatus: Int
    private var mHeaderAnim: Boolean
    private var mHeaderIsOpen: Boolean = false
    private val mHeaderHeight: Int = UIUtils.dip2px(50)

    companion object {
        const val LOADING: Int = 1
        const val SUCCESS: Int = 2
        const val FAIL: Int = 3
    }

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attributeSet: AttributeSet?) : this(context, attributeSet, -1)

    constructor(context: Context, attributeSet: AttributeSet?, defStyleAttr: Int) : super(context, attributeSet, defStyleAttr) {

        val typedArray = context.obtainStyledAttributes(attributeSet, R.styleable.RecycleListView)
        mHeaderBackground = typedArray.getColor(R.styleable.RefreshLinearLayout_headerBackground, resources.getColor(R.color.blue_alpha20))
        mHeaderIcon = typedArray.getDrawable(R.styleable.RefreshLinearLayout_headerImage)
        mHeaderText = typedArray.getString(R.styleable.RefreshLinearLayout_headerText)
        mHeaderStatus = typedArray.getInt(R.styleable.RefreshLinearLayout_headerStatus, 0)
        mHeaderAnim = typedArray.getBoolean(R.styleable.RefreshLinearLayout_headerAnim, true)
        typedArray.recycle()

        initView()
    }

    private fun initView() {
        val view = View.inflate(context, R.layout.layout_header_refresh, null)
        val layoutParam = LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        addView(view, layoutParam)
        mTvText = findViewById(R.id.refreshLinearLayout_text)
        mIvIcon = findViewById(R.id.refreshLinearLayout_icon)
        mRlContent = findViewById(R.id.refreshLinearLayout_rlContent)

        setView(true)
    }

    fun updateStatus(headerStatus: Int, backColor: Int, headerText: String?, iconResId: Int, packUpHeader: Boolean) {
        when (headerStatus) {
            LOADING -> {
                mHeaderBackground = if (headerStatus == -1) {
                    resources.getColor(R.color.blue_alpha20)
                } else {
                    backColor
                }
                mHeaderIcon = if (iconResId == -1) {
                    ContextCompat.getDrawable(context, R.mipmap.icon_net_loading)
                } else {
                    ContextCompat.getDrawable(context, iconResId)
                }
                mHeaderText = if (TextUtils.isEmpty(headerText)) {
                    context.getString(R.string.net_loading)
                } else headerText
            }
            SUCCESS -> {
                mHeaderBackground = if (headerStatus == -1) {
                    resources.getColor(R.color.blue_alpha20)
                } else {
                    backColor
                }
                mHeaderIcon = if (iconResId == -1) {
                    ContextCompat.getDrawable(context, R.mipmap.icon_net_success)
                } else {
                    ContextCompat.getDrawable(context, iconResId)
                }
                mHeaderText = if (TextUtils.isEmpty(headerText)) {
                    context.getString(R.string.net_success)
                } else headerText
            }
            FAIL -> {
                mHeaderBackground = if (headerStatus == -1) {
                    resources.getColor(R.color.red_alpha20)
                } else {
                    backColor
                }
                mHeaderIcon = if (iconResId == -1) {
                    ContextCompat.getDrawable(context, R.mipmap.icon_net_error)
                } else {
                    ContextCompat.getDrawable(context, iconResId)
                }
                mHeaderText = if (TextUtils.isEmpty(headerText)) {
                    context.getString(R.string.net_error)
                } else headerText
            }
        }
        mHeaderStatus = headerStatus
        setView(packUpHeader)
    }

    fun updateStatus(headerStatus: Int) {
        updateStatus(headerStatus, null, true)
    }

    /**
     * 更新header 状态
     * @param packUpHeader 失败状态下是否收起header
     */
    fun updateStatus(headerStatus: Int, headerText: String?, packUpHeader: Boolean) {
        when (headerStatus) {
            LOADING -> {
                mHeaderBackground = resources.getColor(R.color.blue_alpha20)
                mHeaderIcon = ContextCompat.getDrawable(context, R.mipmap.icon_net_loading)
                mHeaderText = if (TextUtils.isEmpty(headerText)) {
                    context.getString(R.string.net_loading)
                } else headerText
            }
            SUCCESS -> {
                mHeaderBackground = resources.getColor(R.color.blue_alpha20)
                mHeaderIcon = ContextCompat.getDrawable(context, R.mipmap.icon_net_success)
                mHeaderText = if (TextUtils.isEmpty(headerText)) {
                    context.getString(R.string.net_success)
                } else headerText
            }
            FAIL -> {
                mHeaderBackground = resources.getColor(R.color.red_alpha20)
                mHeaderIcon = ContextCompat.getDrawable(context, R.mipmap.icon_net_error)
                mHeaderText = if (TextUtils.isEmpty(headerText)) {
                    context.getString(R.string.net_error)
                } else headerText
            }
        }
        mHeaderStatus = headerStatus
        setView(packUpHeader)
    }

    private fun setView(packUpHeader: Boolean) {
        if (mHeaderStatus == 0) {
            mRlContent?.visibility = View.GONE
            mHeaderIsOpen = false
        } else {
            //已经加载完成的，且header已经关闭，则不再打开界面
//            if (mHeaderStatus == FAIL || mHeaderStatus == SUCCESS) {
//                if (!mHeaderIsOpen) return
//            }
            mRlContent?.visibility = View.VISIBLE
            if (!mHeaderIsOpen) {
                changeHeight(0, mHeaderHeight)
                mHeaderIsOpen = true
            }

            Observable.just(mHeaderIsOpen)
                    .map { mHeaderIsOpen ->
                        if (mHeaderStatus == SUCCESS) {
                            return@map mHeaderIsOpen
                        } else if (mHeaderStatus == FAIL) {
                            //加载失败
                            if (packUpHeader)
                                return@map mHeaderIsOpen
                            else return@map false
                        } else {
                            //加载中
                            return@map false
                        }
                    }
                    .delay(3, TimeUnit.SECONDS)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe { t ->
                        if (t) {
                            changeHeight(mHeaderHeight, 0)
                        }
                    }

            mIvIcon?.setImageDrawable(mHeaderIcon)
            if (mHeaderStatus != LOADING) {
                mIvIcon?.clearAnimation()
            }
            mRlContent?.setBackgroundColor(mHeaderBackground)
            mTvText?.text = mHeaderText
            when (mHeaderStatus) {
                FAIL -> mTvText?.setTextColor(resources.getColor(R.color.red_ff2f2c))
                else -> mTvText?.setTextColor(resources.getColor(R.color.main_blue))
            }
        }
    }

    /**
     * 主动收起header
     */
    fun packUpHeader() {
        if (mRlContent?.visibility == View.VISIBLE) {
            changeHeight(mHeaderHeight, 0)
        } else mRlContent?.visibility = View.GONE
    }

    private fun rotateImage() {
        var anim = RotateAnimation(0f, 360f, Animation.RELATIVE_TO_SELF
                , 0.5f, Animation.RELATIVE_TO_SELF, 0.5f)
        anim.duration = 1000
        anim.repeatCount = Animation.INFINITE
        anim.repeatMode = Animation.RESTART
        mIvIcon?.startAnimation(anim)
    }

    private fun changeHeight(startHeight: Int, endHeight: Int) {
        var valueAnimator = ValueAnimator.ofInt(0, 1)
        valueAnimator.duration = 500
        valueAnimator.addUpdateListener { var1 ->

            var evaluator = IntEvaluator()
            val evaluate = evaluator.evaluate(var1.animatedFraction, startHeight, endHeight)
            mRlContent?.layoutParams?.height = evaluate
            mRlContent?.requestLayout()
        }

        var listener = object : Animator.AnimatorListener {
            override fun onAnimationRepeat(p0: Animator?) {
            }

            override fun onAnimationCancel(p0: Animator?) {
            }

            override fun onAnimationStart(p0: Animator?) {
            }

            override fun onAnimationEnd(p0: Animator?) {
                if (endHeight == 0) {
                    mRlContent?.visibility = View.GONE
                    mHeaderIsOpen = false
                } else if (endHeight == mHeaderHeight) {
                    if (mHeaderStatus == LOADING && mHeaderAnim) {
                        rotateImage()
                    }
                }
            }
        }
        valueAnimator.addListener(listener)
        valueAnimator.start()
    }
}