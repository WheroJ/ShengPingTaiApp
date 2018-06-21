package com.zrspring.libv2.okhttp3;

import android.content.Context;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zrspring.libv2.util.LogUtil;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

/**
 * Version: 1.0
 * Author: rui
 * CreatDate: 2016年12月12日14:16 16
 * Encoding:UTF-8
 * Description:
 */
public class MyOkhttp3 {

    public static final long  READ_TIME_OUT = 10000L;
    public static final long  CONNECT_TIME_OUT = 10000L;

    public static void init() {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(CONNECT_TIME_OUT, TimeUnit.MILLISECONDS)
                .readTimeout(READ_TIME_OUT, TimeUnit.MILLISECONDS)
                //其他配置
                .build();
        OkHttpUtils.initClient(okHttpClient);
        LogUtil.infoMessage("MyOkhttp3","MyOkhttp3 initialize");


    }

    public static void cancelRequestByTag(String tag){
        OkHttpUtils.getInstance().cancelTag(tag);
    }
}
