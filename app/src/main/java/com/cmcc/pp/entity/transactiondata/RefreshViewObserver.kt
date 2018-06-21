package com.cmcc.pp.entity.transactiondata

import com.cmcc.pp.ui.widget.RefreshLinearLayout

/**
 * Created by shopping on 2018/1/22 9:52.
 * https://github.com/wheroj
 */
class RefreshViewObserver(private val refreshView: RefreshLinearLayout) {
    fun changeViewStatus(status: Int) {
        refreshView.updateStatus(status)
    }
}