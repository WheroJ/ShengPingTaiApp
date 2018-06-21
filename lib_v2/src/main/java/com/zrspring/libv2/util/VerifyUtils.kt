package com.zrspring.libv2.util

import android.text.TextUtils

/**
 * Created by shopping on 2018/1/16 15:51.
 * https://github.com/wheroj
 */
object VerifyUtils {
    fun saveKey(sequenceId: String) {
        SPUtil.saveString(sequenceId, sequenceId)
    }

    fun isExist(sequenceId: String):Boolean {
        val sequenceId = SPUtil.getString(sequenceId, null)
        if (TextUtils.isEmpty(sequenceId)) {
            return false
        }
        SPUtil.removeKey(sequenceId)
        return true
    }
}