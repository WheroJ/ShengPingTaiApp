package com.cmcc.pp.request;

import com.cmcc.pp.api.Api;
import com.cmcc.pp.api.ParamTools;
import com.cmcc.pp.entity.transactiondata.AreaItem;
import com.fasterxml.jackson.core.type.TypeReference;
import com.zrspring.libv2.network.Client;
import com.zrspring.libv2.network.RxUtils;
import com.zrspring.libv2.util.JsonUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

/**
 * Created by shopping on 2018/1/18 11:32.
 * https://github.com/wheroj
 */

public class TransactionDataHttpUtils {
    /**
     * 获取区域详情
     */
    public static void getAreaInfoData(RxUtils.HttpListener listener) {
        ParamTools paramTools = new ParamTools();
        paramTools.addPath("businessDataService", "getAreaInfoData");
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), paramTools.getParam());
        RxUtils.INSTANCE.acquireString(Client.getApi(Api.class).handle(requestBody), listener);
    }

    /**
     * 业务数据-获取网关数据和放装量趋势数据和厂商业务发展情况数据
     * @param areaId
     * @param listener
     */
    public static void getGatewayInstallationBusinessManufacturerData(String areaId, RxUtils.HttpListener listener) {
        ParamTools paramTools = new ParamTools();
        paramTools.addPath("businessDataService", "getGatewayInstallationBusinessManufacturerData");
        paramTools.addParam("areaId", areaId);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), paramTools.getParam());
        RxUtils.INSTANCE.acquireString(Client.getApi(Api.class).handle(requestBody), listener);
    }

    /**
     * 业务数据-获取工单统计数据与APP绑定量数据
     * @param areaId
     * @param listener
     */
    public static void getWorkOrderStatisticsDataAndAppBindingData(String areaId, RxUtils.HttpListener listener) {
        ParamTools paramTools = new ParamTools();
        paramTools.addPath("businessDataService", "getWorkOrderStatisticsDataAndAppBindingData");
        paramTools.addParam("areaId", areaId);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), paramTools.getParam());
        RxUtils.INSTANCE.acquireString(Client.getApi(Api.class).handle(requestBody), listener);
    }

    public static void test () {
        ParamTools paramTools = new ParamTools();
        paramTools.addPath("businessDataService", "getAreaInfoData");
        final RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), paramTools.getParam());
        Client.getApi(Api.class)
                .handle(requestBody)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .flatMap(new Function<ResponseBody, ObservableSource<ResponseBody>>() {
                    @Override
                    public ObservableSource<ResponseBody> apply(ResponseBody responseBody) throws Exception {
                        JSONObject jsonObject = new JSONObject(responseBody.string());
                        String resultData = jsonObject.optString("resultData");
                        int  result = jsonObject.optInt("result", -1);
                        ArrayList<AreaItem> areaItems = new ArrayList<>();
                        if (result == 0) {
                            String userAreaInfoListData = new JSONObject(resultData).optString("userAreainfoListData");
                            areaItems = JsonUtils.json2Obj(userAreaInfoListData, new TypeReference<ArrayList<AreaItem>>() {
                            });
                        }
                        ParamTools paramTools = new ParamTools();
                        paramTools.addPath("businessDataService", "getGatewayInstallationBusinessManufacturerData");
                        paramTools.addParam("areaId", areaItems.get(0).areaId);
                        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), paramTools.getParam());
                        return Client.getApi(Api.class).handle(requestBody);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<ResponseBody>() {
                    @Override
                    public void accept(ResponseBody responseBody) throws Exception {
                        try {
                            JSONObject resultJson = new JSONObject(responseBody.string());
                            String resultData = resultJson.optString("resultData");
                            int  result = resultJson.optInt("result", -1);
                            if (result == 0) {
                                JSONObject jsonObject = new JSONObject(resultData);
                                JSONObject gatewayData = jsonObject.optJSONObject("gatewayData");
                                JSONObject installQuantityTrendData = jsonObject.optJSONObject("installQuantityTrendData");
                                JSONArray businessDevelopmentOfTheManufacturer = jsonObject.optJSONArray("businessDevelopmentOfTheManufacturerData");
//                                initGateway(gatewayData);
//                                initBusinessDev(businessDevelopmentOfTheManufacturer);
//                                drawLineChart(installQuantityTrendData);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }
}
