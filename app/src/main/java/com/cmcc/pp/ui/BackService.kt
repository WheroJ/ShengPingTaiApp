package com.cmcc.pp.ui

import android.app.Service
import android.content.Intent
import android.content.IntentFilter
import android.os.IBinder
import com.cmcc.pp.util.Constant
import com.cmcc.pp.util.IntentUtils
import com.cmcc.pp.util.TokenUtils
import com.cmcc.pp.util.UIUtils
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

/**
 * Created by shopping on 2018/1/23 11:40.
 * https://github.com/wheroj
 */
class BackService : Service() {

    private var compositeDisposable = CompositeDisposable()
    private var intentFilter: IntentFilter? = null
    private var receiver : ReLoginReceiver? = null
    private var count: Int = 0
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        intentFilter = IntentFilter()
        intentFilter?.addAction(Constant.reLoginAction)
        intentFilter?.addAction(Constant.reGetToken)
        intentFilter?.addAction(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)
        receiver = ReLoginReceiver()
        registerReceiver(receiver, intentFilter)

        compositeDisposable.add(Observable.interval(0, 1, TimeUnit.MINUTES, Schedulers.io())
                .subscribe {
                    if (TokenUtils.isTokenGoingOutOfDate() && !TokenUtils.isTokenOutOfDate()) {
                        val intent = Intent()
                        intent.action = Constant.reGetToken
                        sendBroadcast(intent)
                    } else if (TokenUtils.isTokenOutOfDate()) {
                        val intent = Intent()
                        intent.action = Constant.reLoginAction
                        sendBroadcast(intent)
                        IntentUtils.stopServcie(UIUtils.getContext())
                        compositeDisposable.dispose()
                    }
                    println(++count)
                })
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.dispose()
        unregisterReceiver(receiver)
    }
}