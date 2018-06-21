package com.cmcc.pp.ui

import android.content.BroadcastReceiver
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import com.cmcc.pp.R
import com.cmcc.pp.entity.UserEntity
import com.cmcc.pp.request.UserHttpUtils
import com.cmcc.pp.util.Constant
import com.cmcc.pp.util.IntentUtils
import com.cmcc.pp.util.TokenUtils
import com.zrspring.libv2.network.RxUtils
import com.zrspring.libv2.util.JsonUtils
import com.zrspring.libv2.util.SPUtil
import com.zrspring.libv2.util.ToastUtils
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.litepal.crud.DataSupport
import java.util.concurrent.TimeUnit

/**
 * Created by shopping on 2018/1/23 11:47.
 * https://github.com/wheroj
 */
class ReLoginReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        var action = intent?.action
        when(action) {
            Constant.reGetToken -> getNewToken(context)
            Constant.reLoginAction -> reLogin(context)
            Intent.ACTION_CLOSE_SYSTEM_DIALOGS -> monitorKey(intent, context)
        }
    }

    private fun monitorKey(intent: Intent?, context: Context?) {
        var reason = intent?.getStringExtra(Constant.SYSTEM_DIALOG_REASON_KEY)
        if (reason != null) {
            when (reason) {
                Constant.SYSTEM_DIALOG_REASON_HOME_KEY -> stopService(context)
                Constant.SYSTEM_DIALOG_REASON_LOCK -> stopService(context)
            }
        }
    }

    private fun stopService(context: Context?) {
        SPUtil.saveBoolean(Constant.IS_RUN_BACK, true)
        IntentUtils.stopServcie(context)
    }

    private fun reLogin(context: Context?) {
        ToastUtils.show(R.string.relogin)
        Observable.timer(3, TimeUnit.SECONDS, Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    IntentUtils.goReLogin(context)
                })
    }

    private fun getNewToken(context: Context?) {

        UserHttpUtils.getNewOpenId(TokenUtils.getToken(), object : RxUtils.DefaultListener(){
            override fun onResult(result: String) {
                var contentValues = ContentValues()
                val userEntity = TokenUtils.getCurrentLoginUser()
                contentValues.put("openId", JsonUtils.getJsonStringObjInKeyValue(result, "openId"))
                contentValues.put("expireIn", JsonUtils.getIntObjInKeyValue(result, "expireIn"))
                contentValues.put("tokenSaveTime", System.currentTimeMillis())
                if (userEntity != null)
                    DataSupport.updateAll(UserEntity::class.java, contentValues, "(telePhone='" + userEntity.telePhone + "')")
            }

            override fun onError(e: Throwable) {
                super.onError(e)
                if ("-106" == e.message) {
                    //openId为空
                    reLogin(context)
                }
            }
        })
    }
}