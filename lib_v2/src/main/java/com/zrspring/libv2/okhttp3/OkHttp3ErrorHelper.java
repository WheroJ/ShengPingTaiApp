package com.zrspring.libv2.okhttp3;

import android.content.Context;

import com.google.gson.JsonParseException;
import com.zrspring.libv2.R;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

/**
 * Version: 1.0
 * Author: rui
 * CreatDate: 2016年12月12日14:49 49
 * Encoding:UTF-8
 * Description:
 */
public class OkHttp3ErrorHelper {

    public static String getMessage(Context context, Exception error, int responseCode) {
        if (isTimeOutProblem(error)) {
            return context.getResources().getString(R.string.lib_okhttp_out_of_time);
        } else if (isConnectionProblem(error)) {
            return context.getResources().getString(R.string.lib_okhttp_net_error);
        } else if (isNetworkProblem(error)) {
            //return context.getResources().getString(R.string.volley_error_on_neterror);
            return context.getResources().getString(R.string.lib_okhttp_no_connect_error);
        }else if (isJsonParseProblem(error)) {
            return context.getResources().getString(R.string.lib_okhttp_server_data_error);
        }
        return getServerErrorInfo(context, responseCode);
    }

    //数据解析错误
    private static boolean isJsonParseProblem(Object error) {
        return (error instanceof JsonParseException);
    }

    //连接超时
    private static boolean isTimeOutProblem(Object error) {
        return (error instanceof SocketTimeoutException);
    }

    //无网络连接
    private static boolean isConnectionProblem(Object error) {
        return (error instanceof ConnectException);
    }

    //网络错误
    private static boolean isNetworkProblem(Object error) {
        return (error instanceof UnknownHostException);
    }

    private static String getServerErrorInfo(Context context, int responseCode) {
        String errorStr;
        switch (responseCode) {
            case 404:
            case 422:
            case 401:
                errorStr = context.getResources().getString(R.string.lib_okhttp_server_inside_error);
                break;
            default:
                errorStr = context.getResources().getString(R.string.lib_okhttp_server_inside_error);
        }
        return errorStr;
    }

}
