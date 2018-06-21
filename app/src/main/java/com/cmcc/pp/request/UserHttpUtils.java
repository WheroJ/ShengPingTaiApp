package com.cmcc.pp.request;

import com.cmcc.pp.api.Api;
import com.cmcc.pp.api.ParamTools;
import com.zrspring.libv2.network.Client;
import com.zrspring.libv2.network.RxUtils;

import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * Created by shopping on 2018/1/16 11:23.
 * https://github.com/wheroj
 */

public class UserHttpUtils {
    /**
     * 获取验证码
     */
    public static void getImageVerify(String userName, RxUtils.HttpListener listener) {
        ParamTools paramTools = new ParamTools();
        paramTools.addPath("userManagerService", "getImageVerify");
        paramTools.addParam("userName", userName);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), paramTools.getParam());
        RxUtils.INSTANCE.acquireString(Client.getApi(Api.class).apply(requestBody), listener);
    }

    /**
     * 校验验证码
     */
    public static void verifyImageCode(String verifyCode,String userName, RxUtils.HttpListener listener) {
        ParamTools paramTools = new ParamTools();
        paramTools.addPath("userManagerService", "verifyImageCode");
        paramTools.addParam("verifyCode", verifyCode);
        paramTools.addParam("userName", userName);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), paramTools.getParam());
        RxUtils.INSTANCE.acquireString(Client.getApi(Api.class).apply(requestBody), listener);
    }

    /**
     * 登陆
     */
    public static void login(String userName,String passWord,String verifyCode,RxUtils.HttpListener listener) {
        ParamTools paramTools = new ParamTools();
        paramTools.addPath("userManagerService", "login");
        paramTools.addParam("userName", userName);
        paramTools.addParam("passWord", passWord);
        paramTools.addParam("verifyCode", verifyCode);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), paramTools.getParam());
        RxUtils.INSTANCE.acquireString(Client.getApi(Api.class).apply(requestBody), listener);
    }


    /**
     * 退出
     */
    public static void logout(RxUtils.HttpListener listener) {
        ParamTools paramTools = new ParamTools();
        paramTools.addPath("userManagerService", "logout");
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), paramTools.getParam());
        RxUtils.INSTANCE.acquireString(Client.getApi(Api.class).handle(requestBody), listener);
    }

    /**
     * 获取网关信息数据
     */
    public static void getGatewayInfoData(String userAccount,RxUtils.HttpListener listener) {
        ParamTools paramTools = new ParamTools();
        paramTools.addPath("gatewayDataService", "getGatewayInfoData");
        paramTools.addParam("userAccount", userAccount);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), paramTools.getParam());
        RxUtils.INSTANCE.acquireString(Client.getApi(Api.class).handle(requestBody), listener);
    }


    /**
     * 获取网关明细信息数据
     */
    public static void getGatewayInfoDetailData(String gatewayId,RxUtils.HttpListener listener) {
        ParamTools paramTools = new ParamTools();
        paramTools.addPath("gatewayDataService", "getGatewayInfoDetailData");
        paramTools.addParam("gatewayId", gatewayId);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), paramTools.getParam());
        RxUtils.INSTANCE.acquireString(Client.getApi(Api.class).handle(requestBody), listener);
    }

    /**
     * 获取WAN连接数据
     */
    public static void getWANListData(String gatewayId,RxUtils.HttpListener listener) {
        ParamTools paramTools = new ParamTools();
        paramTools.addPath("gatewayDataService", "getWANListData");
        paramTools.addParam("gatewayId", gatewayId);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), paramTools.getParam());
        RxUtils.INSTANCE.acquireString(Client.getApi(Api.class).handle(requestBody), listener);
    }

    /**
     * 线路诊断
     */
    public static void lineDiagnose(String gatewayId,RxUtils.HttpListener listener) {
        ParamTools paramTools = new ParamTools();
        paramTools.addPath("gatewayDataService", "lineDiagnose");
        paramTools.addParam("gatewayId", gatewayId);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), paramTools.getParam());
        RxUtils.INSTANCE.acquireString(Client.getApi(Api.class).handle(requestBody), listener);
    }

    /**
     * Traceroute诊断
     */
    public static void tracerouteDiagnose(String gatewayId,
                                          String mode,
                                          String host,
                                          String numberOfTries,
                                          String timeout,
                                          String dataBlockSize,
                                          String dscp,
                                          String maxHopCount,
                                          String vlanIdMark,
                                          String wanPath,RxUtils.HttpListener listener) {
        ParamTools paramTools = new ParamTools();
        paramTools.addPath("gatewayDataService", "tracerouteDiagnose");
        paramTools.addParam("gatewayId", gatewayId);
        paramTools.addParam("mode", mode);
        paramTools.addParam("host", host);
        paramTools.addParam("numberOfTries", numberOfTries);
        paramTools.addParam("timeout", timeout);
        paramTools.addParam("dataBlockSize", dataBlockSize);
        paramTools.addParam("dscp", dscp);
        paramTools.addParam("maxHopCount", maxHopCount);
        paramTools.addParam("vlanIdMark", Integer.valueOf(vlanIdMark));
        paramTools.addParam("wanPath", wanPath);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), paramTools.getParam());
        RxUtils.INSTANCE.acquireString(Client.getApi(Api.class).handle(requestBody), listener);
    }


    /**
     * Ping诊断
     */
    public static void pingDiagnose(String gatewayId,
                                    String host,
                                    String dataBlockSize,
                                    String numberOfRepetitions,
                                    String timeout,
                                    String vlanIdMark,
                                    String wanPath, RxUtils.HttpListener listener) {
        ParamTools paramTools = new ParamTools();
        paramTools.addPath("gatewayDataService", "pingDiagnose");
        paramTools.addParam("gatewayId", gatewayId);
        paramTools.addParam("host", host);
        paramTools.addParam("timeout", Integer.valueOf(timeout));
        paramTools.addParam("dataBlockSize", Integer.valueOf(dataBlockSize));
        paramTools.addParam("numberOfRepetitions", Integer.valueOf(numberOfRepetitions));
        paramTools.addParam("vlanIdMark", Integer.valueOf(vlanIdMark));
        paramTools.addParam("wanPath", wanPath);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), paramTools.getParam());
        RxUtils.INSTANCE.acquireString(Client.getApi(Api.class).handle(requestBody), listener);
    }


    /**
     * 获取工单列表数据
     */
    public static void getWorkOrderListData(String userAccount,
                                    int pageIndex,
                                    int pageNum,RxUtils.HttpListener listener) {
        ParamTools paramTools = new ParamTools();
        paramTools.addPath("workOrderDataService", "getWorkOrderListData");
        paramTools.addParam("userAccount", userAccount);
        paramTools.addParam("pageIndex", pageIndex);
        paramTools.addParam("pageNum", pageNum);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), paramTools.getParam());
        RxUtils.INSTANCE.acquireString(Client.getApi(Api.class).handle(requestBody), listener);
    }

    /**
     * 获取工单详情数据
     */
    public static void getWorkOrderDetailData(String workOrderId,RxUtils.HttpListener listener) {
        ParamTools paramTools = new ParamTools();
        paramTools.addPath("workOrderDataService", "getWorkOrderDetailData");
        paramTools.addParam("workOrderId", workOrderId);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), paramTools.getParam());
        RxUtils.INSTANCE.acquireString(Client.getApi(Api.class).handle(requestBody), listener);
    }

    /**
     * 获取工单列表数据
     */
    public static void getWorkOrderOperationLogDetailListData(String operationLOID,
                                            String operationLogDayTime,
                                            int pageIndex,
                                            int pageNum,RxUtils.HttpListener listener) {
        ParamTools paramTools = new ParamTools();
        paramTools.addPath("workOrderDataService", "getWorkOrderOperationLogDetailListData");
        paramTools.addParam("operationLOID", operationLOID);
        paramTools.addParam("operationLogDayTime", operationLogDayTime);
        paramTools.addParam("pageIndex", pageIndex);
        paramTools.addParam("pageNum", pageNum);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), paramTools.getParam());
        RxUtils.INSTANCE.acquireString(Client.getApi(Api.class).handle(requestBody), listener);
    }

    /**
     * 获取工单操作记录详情数据
     */
    public static void getWorkOrderOperationLogDetailData(String operationLogDetailId,RxUtils.HttpListener listener) {
        ParamTools paramTools = new ParamTools();
        paramTools.addPath("workOrderDataService", "getWorkOrderOperationLogDetailData");
        paramTools.addParam("operationLogDetailId", operationLogDetailId);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), paramTools.getParam());
        RxUtils.INSTANCE.acquireString(Client.getApi(Api.class).handle(requestBody), listener);
    }

    /**
     * 拨测请求消息
     */
    public static void businessDialing(String dialingType,RxUtils.HttpListener listener) {
        ParamTools paramTools = new ParamTools();
        paramTools.addPath("platformMonitorService", "businessDialing");
        paramTools.addParam("dialingType", dialingType);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), paramTools.getParam());
        RxUtils.INSTANCE.acquireString(Client.getApi(Api.class).handle(requestBody), listener);
    }

    /**
     * 查询拨测结果接口
     */
    public static void queryByNum(String bizNum,RxUtils.HttpListener listener) {
        ParamTools paramTools = new ParamTools();
        paramTools.addPath("platformMonitorService", "queryByNum");
        paramTools.addParam("bizNum", bizNum);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), paramTools.getParam());
        RxUtils.INSTANCE.acquireString(Client.getApi(Api.class).handle(requestBody), listener);
    }


    /**
     * 获取新的openId
     * @param openId
     * @param listener
     */
    public static void getNewOpenId(String openId,RxUtils.HttpListener listener) {
        ParamTools paramTools = new ParamTools();
        paramTools.addPath("userManagerService", "getNewOpenId");
        paramTools.addParam("openId", openId);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), paramTools.getParam());
        RxUtils.INSTANCE.acquireString(Client.getApi(Api.class).handle(requestBody), listener);
    }

}
