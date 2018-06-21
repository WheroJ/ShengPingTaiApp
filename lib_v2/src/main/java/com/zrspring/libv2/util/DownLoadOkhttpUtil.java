package com.zrspring.libv2.util;


import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.FileCallBack;

/**
 * Version: 1.0
 * Author: rui
 * CreatDate: 2016年11月22日下午8:20 20
 * Encoding:UTF-8
 * Description:
 */
public class DownLoadOkhttpUtil {


    public static void downLoadFile(String downUrl, FileCallBack callBack){
        // savePath
        OkHttpUtils//
                .get()//
                .url(downUrl)//
                .build()//
                .execute(callBack);

    }

}
