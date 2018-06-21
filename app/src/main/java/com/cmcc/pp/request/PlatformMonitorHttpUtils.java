package com.cmcc.pp.request;

import com.cmcc.pp.api.Api;
import com.cmcc.pp.api.ParamTools;
import com.zrspring.libv2.network.Client;
import com.zrspring.libv2.network.RxUtils;

import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * Created by shopping on 2018/1/24 15:36.
 * https://github.com/wheroj
 */

public class PlatformMonitorHttpUtils {
    /**
     * 趋势图（终端数量）
     * @param timeType  选择日，取值为day，返回7日内的数据；选择周，取值为week，返回7个周的数据，默认选择日
     * @param listener
     */
    public static void getTerminalTrend(String timeType, RxUtils.HttpListener listener) {
        ParamTools paramTools = new ParamTools();
        paramTools.addPath("platformMonitorService", "getTerminalTrend");
        paramTools.addParam("timeType", timeType);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), paramTools.getParam());
        RxUtils.INSTANCE.acquireString(Client.getApi(Api.class).handle(requestBody), listener);
    }


    /**
     * 趋势图（硬盘容量）
     * @param timeType
     * @param ip
     * @param listener
     */
    public static void getNETrend(String timeType, String ip, RxUtils.HttpListener listener) {
        ParamTools paramTools = new ParamTools();
        paramTools.addPath("platformMonitorService", "getNETrend");
        paramTools.addParam("timeType", timeType);
        paramTools.addParam("ip", ip);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), paramTools.getParam());
        RxUtils.INSTANCE.acquireString(Client.getApi(Api.class).handle(requestBody), listener);
    }

    /**
     * 获取网元概况
     * @param listener
     */
    public static void getOverallView(RxUtils.HttpListener listener) {
        ParamTools paramTools = new ParamTools();
        paramTools.addPath("platformMonitorService", "getOverallView");
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), paramTools.getParam());
        RxUtils.INSTANCE.acquireString(Client.getApi(Api.class).handle(requestBody), listener);
    }
}
