package com.zrspring.libv2.network

import android.app.Activity
import android.text.TextUtils
import com.zrspring.libv2.BuildConfig
import com.zrspring.libv2.util.LogPrinter
import com.zrspring.libv2.util.VerifyUtils
import com.zrspring.libv2.view.ProgressDlg
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import okhttp3.ResponseBody
import org.json.JSONObject
import java.lang.ref.WeakReference


/**
 * Created by shopping on 2017/12/22 10:46.
 * https://github.com/wheroj
 */
object RxUtils {

    var compositeDisposable = CompositeDisposable()

    abstract class DefaultListener(dialog: ProgressDlg? = null) : HttpListener {

        private var weakReference: WeakReference<ProgressDlg>? = null

        init {
            weakReference = WeakReference<ProgressDlg>(dialog)
        }

        override fun onError(e: Throwable) {
            if (BuildConfig.DEBUG) {
                LogPrinter.e("onError", e.message)
            }
            weakReference?.get()?.dismiss()
        }

        override fun onComplete() {
            weakReference?.get()?.dismiss()
        }

        override fun onStart(d: Disposable) {
            weakReference?.get()?.show()
        }
    }

    abstract class DialogListener(var context: Activity? = null) : HttpListener {

        private var weakReference: WeakReference<Activity>? = null
        private var progressDlg: ProgressDlg? = null

        init {
            if (context != null) {
                weakReference = WeakReference<Activity>(context)
                progressDlg = ProgressDlg(weakReference!!.get()!!, "正在获取数据......")
            }

        }

        override fun onError(e: Throwable) {
            if (BuildConfig.DEBUG) {
                LogPrinter.e("onError", e.message)
            }
            progressDlg?.dismiss()
        }

        override fun onComplete() {
            progressDlg?.dismiss()
        }

        override fun onStart(d: Disposable) {
            progressDlg?.show()
        }
    }


    interface HttpListener {
        fun onError(e: Throwable)
        fun onComplete()
        fun onStart(d: Disposable)
        fun onResult(result: String)
    }

    /**
     *  利用 rxjava 访问接口数据
     * @param observable 必传，rxjava Observable<? extends BaseModel>
     * @param httpListener 必传，可传默认实现 ${link RxUtils.DefaultListener}
     */
    fun <M : BaseModel, O : Observable<M>> acquire(
            observable: O, httpListener: HttpListener?) {
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<M> {
                    override fun onError(e: Throwable) {
                        httpListener?.onError(e)
                    }

                    override fun onComplete() {
                        httpListener?.onComplete()
                    }

                    override fun onNext(model: M) {
                    }

                    override fun onSubscribe(d: Disposable) {
                        httpListener?.onStart(d)
                    }
                })
    }

    fun acquireString(observable: Observable<ResponseBody>
                      , httpListener: HttpListener? = null) {
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map { t: ResponseBody -> t.string() }
                .subscribe(object : Observer<String> {

                    override fun onError(e: Throwable) {
                        httpListener?.onError(e)
                    }

                    override fun onComplete() {
                        httpListener?.onComplete()
                    }

                    override fun onNext(model: String) {
                        val jsonObject = JSONObject(model)

                        var baseModel = BaseModel()
                        baseModel.Result = jsonObject.optInt("result", -1)
                        baseModel.ResultData = jsonObject.optString("resultData")
                        if (TextUtils.isEmpty(baseModel.ResultData))baseModel.ResultData = jsonObject.optString("ResultData")
                        if (baseModel.Result == -1)baseModel.Result = jsonObject.optInt("Result", -1)
                        baseModel.SequenceId = jsonObject.optString("sequenceId")

                        when (baseModel.Result) {
                            0 ->
                                if (VerifyUtils.isExist(baseModel.SequenceId)) {
                                    httpListener?.onResult(baseModel.ResultData)
                                } else {
                                    httpListener?.onError(Throwable("请求编号异常"))
                                }
                            0x001 -> httpListener?.onError(Throwable("error-01：失败"))
                            0x002 -> httpListener?.onError(Throwable("服务器内部错误"))
                            0x003 -> httpListener?.onError(Throwable("验证码错误"))
                            0x004 -> httpListener?.onError(Throwable("账号或者密码错误"))
                            0x005 -> httpListener?.onError(Throwable("Token超时"))
                            0x006 -> httpListener?.onError(Throwable("没有权限"))
                            else -> {
                                //处理其他不成功的情況
                                if (baseModel.Result == -1) {
                                    httpListener?.onError(Throwable("未知异常，请联系系统管理员"))
                                } else {
                                    httpListener?.onError(Throwable(baseModel.Result.toString()))
                                }
                            }
                        }
                    }

                    override fun onSubscribe(d: Disposable) {
                        compositeDisposable.add(d)
                        httpListener?.onStart(d)
                    }
                })
    }

    fun cancelRequest() {
        compositeDisposable.dispose()
        compositeDisposable.clear()
//        compositeDisposable = CompositeDisposable()
    }
}