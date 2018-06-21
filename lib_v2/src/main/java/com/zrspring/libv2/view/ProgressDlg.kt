package com.zrspring.libv2.view

import android.app.AlertDialog
import android.content.Context
import android.graphics.drawable.AnimationDrawable
import android.text.TextUtils
import android.view.KeyEvent
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.zrspring.libv2.R
import com.zrspring.libv2.network.RxUtils

/**
 * Created by shopping on 2017/12/22 15:46.
 * https://github.com/wheroj
 */
class ProgressDlg(context: Context, content: String) {

    private var mIvLoading : ImageView? = null
    private var mTvContent : TextView? = null
    private var builder : AlertDialog.Builder? = null
    private var dialog : AlertDialog? = null

    init {
        builder = AlertDialog.Builder(context, R.style.dialog)
        val contentView = View.inflate(context, R.layout.dialog_loading, null)
        builder!!.setView(contentView)
        mIvLoading = contentView.findViewById(R.id.dialogLoading_ivLoading)
        mTvContent = contentView.findViewById(R.id.dialogLoading_tvContent)
        mTvContent!!.text = content
    }

    fun show(content : String?) {
        if(!TextUtils.isEmpty(content)) mTvContent!!.text = content
        mIvLoading!!.setBackgroundResource(R.drawable.loading_white)
        (mIvLoading!!.background as AnimationDrawable).start()
        dialog = builder!!.create()
        dialog!!.setCancelable(true)
        dialog!!.setCanceledOnTouchOutside(false)
        dialog!!.window.attributes.alpha = 0.6f
        dialog!!.setOnKeyListener { _, keyCode, _ ->
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                RxUtils.cancelRequest()
//                RxUtils.compositeDisposable.dispose()
            }
            false
        }
//        dialog!!.window.setType(WindowManager.LayoutParams.TYPE_SYSTEM_DIALOG)
        dialog!!.show()
    }

    fun show() {
        show(null)
    }

    fun dismiss() {
        if (dialog != null && dialog!!.isShowing) {
            dialog!!.dismiss()
        }
    }
}