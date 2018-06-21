package com.cmcc.pp.ui.transactiondata;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.cmcc.pp.R;
import com.cmcc.pp.base.BFragment;
import com.cmcc.pp.entity.transactiondata.AreaItem;
import com.cmcc.pp.entity.transactiondata.BusinessDev;
import com.cmcc.pp.entity.transactiondata.CommonItem;
import com.cmcc.pp.entity.transactiondata.RefreshViewObservable;
import com.cmcc.pp.entity.transactiondata.RefreshViewObserver;
import com.cmcc.pp.entity.transactiondata.RepairOrder;
import com.cmcc.pp.request.TransactionDataHttpUtils;
import com.cmcc.pp.ui.MainActivity;
import com.cmcc.pp.ui.transactiondata.adapter.BusinessDevAdapter;
import com.cmcc.pp.ui.transactiondata.adapter.CommonAdapter;
import com.cmcc.pp.ui.transactiondata.adapter.RepairOrderAdapter;
import com.cmcc.pp.ui.widget.FillLineChart;
import com.cmcc.pp.ui.widget.PieView;
import com.cmcc.pp.ui.widget.RefreshLinearLayout;
import com.cmcc.pp.ui.widget.TabLayout;
import com.cmcc.pp.util.IntentUtils;
import com.cmcc.pp.util.UIUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import com.zrspring.libv2.network.RxUtils;
import com.zrspring.libv2.util.JsonUtils;
import com.zrspring.libv2.util.LogPrinter;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.crud.DataSupport;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Created by ABC on 2017/12/12.
 * 业务数据
 */

public class FragmentTransactionFragment extends BFragment implements View.OnClickListener{

    @Bind(R.id.content)
    RefreshLinearLayout refreshLinearLayout;
    @Bind(R.id.fragmentTransactionData_pieData)
    PieView pieView;
    @Bind(R.id.fragmentTransactionData_rvBusinessData)
    RecyclerView rvBusinessData;
    @Bind(R.id.fragmentTransactionData_rvBindings)
    RecyclerView rvBindings;
    @Bind(R.id.fragmentTransactionData_rvGatewayData)
    RecyclerView rvGatewayData;
    @Bind(R.id.fragmentTransactionData_fillLineChart)
    FillLineChart fillLineChart;
    @Bind(R.id.fragmentTransactionData_elvRepairOrder)
    ExpandableListView elvRepairOrder;
    @Bind(R.id.fragmentTransactionData_tvLocation)
    TextView tvLocation;
    @Bind(R.id.fragmentTransactionData_trendTab)
    TabLayout trendTab;

    @Bind(R.id.scrollView)
    ScrollView scrollView;

    ArrayList<FillLineChart.DataPoint> dayDataPoints = new ArrayList<>();
    ArrayList<FillLineChart.DataPoint> monDataPoints = new ArrayList<>();

    private final int[] bindImgResIds = {R.mipmap.icon_bind_end, R.mipmap.icon_bind_ratio, R.mipmap.icon_bind_user};
    private final int[] gatewayImgResIds = {R.mipmap.icon_gateway_total, R.mipmap.icon_gateway_store
            , R.mipmap.icon_gateway_putpack, R.mipmap.icon_gateway_newadd};
    private final int[] repairOderImgResIds = {R.mipmap.icon_broad_band, R.mipmap.icon_audio
            , R.mipmap.icon_ott, R.mipmap.icon_other};

    private final String[] repairOrderNames = new String[]{"", "宽带", "语音", "OTT", "其他"};
    private final String[] repairOrderChildNames = new String[]{"", "新装", "缴费开机", "欠费停机", "客户申请停机", "客户申请复机"
            , "订户密码变更", "修改速率信息", "拆机", "修改", "其他"};

    private static final int GET_AREA = 100;
    private List<AreaItem> areaItems = new ArrayList<>();
    private LocationClient mLocationClient;
    private MyLocationListener locationListener;

    private int loadCount = 0;
    private RefreshViewObservable refreshViewObservable;
    private String nowCity;
    private AreaItem nowAreaItem;

    @Override
    protected boolean getHasTitle() {
        return true;
    }

    @Override
    public int getContentLayoutId() {
        return R.layout.fragment_transactiondate;
    }

    @Override
    public void initView(View fragmentView) {
        getHeader().setTitle(R.string.str_transactiondata);
        getHeader().showLeftImage(R.mipmap.iocn_menubar);
        getHeader().showRightImage(R.mipmap.icon_share);

        rvBindings.setLayoutManager(new GridLayoutManager(getActivity(), 3, GridLayoutManager.VERTICAL, false));
        rvBusinessData.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        rvGatewayData.setLayoutManager(new GridLayoutManager(getActivity(), 4, GridLayoutManager.VERTICAL, false));

        trendTab.setOnCheckChangeListener(new TabLayout.OnTabCheckedChangeListener() {
            @Override
            public void onCheckedChanged(@NotNull RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.layoutTab_left:
                        fillLineChart.setData(dayDataPoints);
                        break;
                    case R.id.layoutTab_right:
                        fillLineChart.setData(monDataPoints);
                        drawFace();
                        break;
                }
            }
        });

        refreshViewObservable = new RefreshViewObservable();
        refreshViewObservable.addOberver(new RefreshViewObserver(refreshLinearLayout));

        mLocationClient = new LocationClient(UIUtils.getContext());
        locationListener = new MyLocationListener();
        mLocationClient.registerLocationListener(locationListener);
        LocationClientOption option = new LocationClientOption();

        option.setIsNeedAddress(true);
        //如果开发者需要获得当前点的地址信息，此处必须为true

        mLocationClient.setLocOption(option);
    }

    @OnClick(R.id.fragmentTransactionData_tvLocation)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fragmentTransactionData_tvLocation:
                IntentUtils.goChooseArea(this, nowAreaItem, GET_AREA);
                break;
        }
    }

    public class MyLocationListener extends BDAbstractLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            //更多结果信息获取说明，请参照类参考中BDLocation类中的说明

            String addr = location.getAddrStr();    //获取详细地址信息
            String district = location.getAddress().district;//获取区域
            //获取城市
            nowCity = location.getCity();
            if (!TextUtils.isEmpty(nowCity)) {
//                tvLocation.setText(nowCity); TODO
                if (nowCity.endsWith("市")){
                    nowCity = nowCity.substring(0, nowCity.length() - 1);
                }

                refreshViewObservable.changeStatus(RefreshLinearLayout.SUCCESS);
                getAreaInfo(nowCity);
                LogPrinter.i("LOCATION", "当前位置：" + addr + ", 城市：" + nowCity + ", 区域：" + district);
            }
        }
    }

    private void getAreaInfo(final String nowCity) {
        areaItems = DataSupport.findAll(AreaItem.class);
        if (areaItems.isEmpty()) {
            TransactionDataHttpUtils.getAreaInfoData(new RxUtils.HttpListener() {
                @Override
                public void onStart(@NotNull Disposable d) {
                    refreshViewObservable.addLoadCount();
                    refreshViewObservable.changeStatus(RefreshLinearLayout.LOADING);
                }

                @Override
                public void onComplete() {
                    refreshViewObservable.subLoadCount();
                    refreshViewObservable.changeStatus(RefreshLinearLayout.SUCCESS);
                }

                @Override
                public void onError(@NotNull Throwable e) {
                    refreshViewObservable.subLoadCount();
                    refreshViewObservable.changeStatus(RefreshLinearLayout.FAIL);
                    showError();
                }

                @Override
                public void onResult(@NotNull String result) {
                    try {
                        String userAreaInfoListData = new JSONObject(result).optString("userAreainfoListData");
                        areaItems = JsonUtils.json2Obj(userAreaInfoListData, new TypeReference<ArrayList<AreaItem>>() {
                        });
                        DataSupport.saveAll(areaItems);

                        //TODO  测试只能写死区域，重庆没有数据
                        getDataByArea(areaItems.get(0));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        } else {
            long recentTime = Long.MIN_VALUE;
            for (int i = 0; i < areaItems.size(); i++) {
                if (areaItems.get(i).isRecent == 2) {
                    //以最后选取的区域作为当前区域
                    if (recentTime < areaItems.get(i).recentTime) {
                        recentTime = areaItems.get(i).recentTime;
                        nowAreaItem = areaItems.get(i);
                    }
                }
            }
            getDataByArea(nowAreaItem);
        }
    }

    private void getDataByArea(AreaItem areaItem) {
        nowAreaItem = areaItem;
        if (nowAreaItem == null) nowAreaItem = areaItems.get(0);
        tvLocation.setText(nowAreaItem.areaName);
        loadTopViewData(nowAreaItem.areaId);
        loadBottomViewData(nowAreaItem.areaId);
    }

    private void drawLineChart(JSONObject data) {
        JSONArray dayStatisticsTrendData = data.optJSONArray("dayStatisticsTrendData");
        JSONArray monthStatisticsStatisticsData = data.optJSONArray("monthStatisticsStatisticsData");
        dayDataPoints.clear();
        monDataPoints.clear();

        if (dayStatisticsTrendData != null) {
            for (int i = 0; i < dayStatisticsTrendData.length(); i++) {
                JSONObject object = dayStatisticsTrendData.optJSONObject(i);
                FillLineChart.DataPoint dataPoint = new FillLineChart.DataPoint();
                dataPoint.setDayTime(object.optString("monthDayTime"));
                dataPoint.setTrendValue(object.optString("statisticsValue"));
                dayDataPoints.add(dataPoint);
            }
        }
        if (monthStatisticsStatisticsData != null) {
            for (int i = 0; i < monthStatisticsStatisticsData.length(); i++) {
                JSONObject object = monthStatisticsStatisticsData.optJSONObject(i);
                FillLineChart.DataPoint dataPoint = new FillLineChart.DataPoint();
                dataPoint.setDayTime(object.optString("dayTime"));
                dataPoint.setTrendValue(object.optString("trendValue"));
                monDataPoints.add(dataPoint);
            }
        }

        switch (trendTab.getCheckedId()) {
            case R.id.layoutTab_left:
                fillLineChart.setData(dayDataPoints);
                break;
            case R.id.layoutTab_right:
                fillLineChart.setData(monDataPoints);
                break;
        }
    }

    private void drawPieChart(String json, String workOrderTotalQuantity) {
        ArrayList<RepairOrder> repairOrders = JsonUtils.json2Obj(json, new TypeReference<ArrayList<RepairOrder>>() {
        });
        initRepairOrder(repairOrders);

        float total = Float.parseFloat(workOrderTotalQuantity);
        if (repairOrders != null) {
            for (int i = 0; i < repairOrders.size(); i++) {
                try {
                    RepairOrder repairOrder = repairOrders.get(i);
                    float part = Float.parseFloat(repairOrder.statisticsValue);
                    repairOrder.ratio = part / total;
                    repairOrder.type = repairOrder.statisticsName;
                    repairOrder.itemName = repairOrderNames[repairOrder.type];
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                } catch (IndexOutOfBoundsException e) {
                    e.printStackTrace();
                }
            }
        }
        pieView.setData(repairOrders);
    }

    private String getAreaId(String cityName) {
        for (AreaItem areaItem : areaItems) {
            if (!TextUtils.isEmpty(cityName) && cityName.equals(areaItem.areaName)) {
                return areaItem.areaId;
            }
        }
        return null;
    }


    @Override
    public void loadData() {
        if (!TextUtils.isEmpty(nowCity)) {
            getAreaInfo(nowCity);
        } else {
            mLocationClient.start();
        }
    }

    private void loadBottomViewData(String areaId) {
        TransactionDataHttpUtils.getWorkOrderStatisticsDataAndAppBindingData(areaId, new RxUtils.DialogListener(getActivity()) {

            @Override
            public void onStart(@NotNull Disposable d) {
                refreshViewObservable.addLoadCount();
                refreshViewObservable.changeStatus(RefreshLinearLayout.LOADING);
            }

            @Override
            public void onComplete() {
                refreshViewObservable.subLoadCount();
                refreshViewObservable.changeStatus(RefreshLinearLayout.SUCCESS);
            }

            @Override
            public void onResult(@NotNull String result) {
                drawPieChart(JsonUtils.getJsonStringObjInKeyValue(result, "graphStatisticsData")
                        , JsonUtils.getJsonStringObjInKeyValue(result, "workOrderTotalQuantity"));
                initBinds(JsonUtils.getJsonStringObjInKeyValue(result, "appBindingData"));
            }

            @Override
            public void onError(@NotNull Throwable e) {
                super.onError(e);
                showError();
            }
        });
    }

    private void loadTopViewData(String areaId) {
        TransactionDataHttpUtils.getGatewayInstallationBusinessManufacturerData(areaId
                , new RxUtils.DialogListener(getActivity()) {
                    @Override
                    public void onResult(@NotNull String result) {
                        try {
                            JSONObject jsonObject = new JSONObject(result);
                            JSONObject gatewayData = jsonObject.optJSONObject("gatewayData");
                            JSONObject installQuantityTrendData = jsonObject.optJSONObject("installQuantityTrendData");
                            JSONArray businessDevelopmentOfTheManufacturer = jsonObject.optJSONArray("businessDevelopmentOfTheManufacturerData");
                            initGateway(gatewayData);
                            initBusinessDev(businessDevelopmentOfTheManufacturer);
                            drawLineChart(installQuantityTrendData);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onStart(@NotNull Disposable d) {
                        refreshViewObservable.addLoadCount();
                        refreshViewObservable.changeStatus(RefreshLinearLayout.LOADING);
                    }

                    @Override
                    public void onComplete() {
                        refreshViewObservable.subLoadCount();
                        refreshViewObservable.changeStatus(RefreshLinearLayout.SUCCESS);
                    }

                    @Override
                    public void onError(@NotNull Throwable e) {
                        refreshViewObservable.subLoadCount();
                        refreshViewObservable.changeStatus(RefreshLinearLayout.FAIL);
                        showError();
                    }
                });
    }

    private void drawFace() {
        int h = 0;
        RefreshLinearLayout view = (RefreshLinearLayout) scrollView.getChildAt(0);
        for (int i = 0; i < view.getChildCount(); i++) {
            h += view.getChildAt(i).getHeight();
        }

        final Bitmap bitmap = Bitmap.createBitmap(getActivity().getResources().getDisplayMetrics().widthPixels
                , h, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        scrollView.draw(canvas);

        Observable.just(true).subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(Boolean aBoolean) throws Exception {
                FileOutputStream fileOutputStream = new FileOutputStream(UIUtils.getCachePath() + File.separator + "bitmap.jpg");
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
                bitmap.recycle();
            }
        });
    }

    private void initRepairOrder(ArrayList<RepairOrder> repairOrders) {
        elvRepairOrder.setGroupIndicator(null);
        if (repairOrders != null) {
            for (int i = 0; i < repairOrders.size(); i++) {
                RepairOrder repairOrder = repairOrders.get(i);
                repairOrder.itemName = repairOrderNames[repairOrder.statisticsName];
                ArrayList<RepairOrder.RepairOderChild> dimensionDetailData = repairOrder.dimensionDetailData;
                if (dimensionDetailData != null) {
                    for (int j = 0; j < dimensionDetailData.size(); j++) {
                        try {
                            dimensionDetailData.get(j).dimensionDetailNameStr
                                    = repairOrderChildNames[dimensionDetailData.get(j).dimensionDetailName];
                        } catch (IndexOutOfBoundsException e) {
                            e.printStackTrace();
                            LogPrinter.e("获取工单", "下标越界");
                        }
                    }
                }
            }
        }

        final RepairOrderAdapter repairOrderAdapter = new RepairOrderAdapter(getContext(), repairOrders);
        elvRepairOrder.setAdapter(repairOrderAdapter);
        elvRepairOrder.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                int count = elvRepairOrder.getExpandableListAdapter().getGroupCount();
                for (int j = 0; j < count; j++) {
                    if (j != groupPosition) {
                        elvRepairOrder.collapseGroup(j);
                        Calendar.getInstance().add(Calendar.MINUTE, 15);
                    }
                }
            }
        });

        if (repairOrders.size() > 0) {
            elvRepairOrder.expandGroup(0);
        }
    }

    private void addAnimToView(ExpandableListView parent, View v, int groupPosition) {
        ImageView ivArrow = v.findViewById(R.id.itemRepairParent_imageRight);
        if (parent.isGroupExpanded(groupPosition)) {

            RotateAnimation anim = new RotateAnimation(90, 0, Animation.RELATIVE_TO_SELF, 0.5f,
                    Animation.RELATIVE_TO_SELF, 0.5f);
            anim.setDuration(200);
            anim.setFillAfter(true);
            anim.setAnimationListener(getAnimListener(v));
            ivArrow.startAnimation(anim);
        } else {
            RotateAnimation anim = new RotateAnimation(0, 90, Animation.RELATIVE_TO_SELF, 0.5f,
                    Animation.RELATIVE_TO_SELF, 0.5f);
            anim.setDuration(200);
            anim.setFillAfter(true);
            anim.setAnimationListener(getAnimListener(v));
            ivArrow.startAnimation(anim);
        }
    }

    private void initBusinessDev(JSONArray dataList) {

        ArrayList<BusinessDev> businessDevs = JsonUtils.json2Obj(dataList.toString(), new TypeReference<ArrayList<BusinessDev>>() {
        });

        if (businessDevs != null) {
            float maxInstall = 0;
            float maxStorage = 0;
            for (int i = 0; i < businessDevs.size(); i++) {
                try {
                    float aFloat = Float.parseFloat(businessDevs.get(i).installQuantity);
                    if (aFloat > maxInstall) maxInstall = aFloat;

                    aFloat = Float.parseFloat(businessDevs.get(i).bePutInStorageTotalQuantity);
                    if (aFloat > maxStorage) maxStorage = aFloat;
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }

            maxInstall = calculateMax(maxInstall);
            maxStorage = calculateMax(maxStorage);
            for (int i = 0; i < businessDevs.size(); i++) {
                try {
                    businessDevs.get(i).putPackRatio = Float.parseFloat(businessDevs.get(i).installQuantity) / maxInstall;
                    businessDevs.get(i).inventoryRatio = Float.parseFloat(businessDevs.get(i).bePutInStorageTotalQuantity) / maxStorage;
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }

        }

        BusinessDevAdapter businessDevAdapter = new BusinessDevAdapter(businessDevs);
        rvBusinessData.setAdapter(businessDevAdapter);
    }

    /**
     * 业务发展：采用右数第二位数向上取整计算最大值
     *
     * @param nowMax
     * @return
     */
    private float calculateMax(float nowMax) {
        float lastMax = 1;

        int rem = 0;
        while (nowMax >= 10) {
            rem = (int) (nowMax % 10);
            nowMax = nowMax / 10;
            lastMax *= 10;
        }
        lastMax = (rem + 1) * (lastMax / 10) + ((int) nowMax) * lastMax;
        return lastMax;
    }

    private void initGateway(JSONObject data) {
        ArrayList<CommonItem> commonItems = new ArrayList<>();
        CommonItem commonItem = new CommonItem();
        commonItem.bottomText = "入库总量";
        commonItem.centerText = data.optString("bePutInStorageTotalQuantity");
        commonItem.imgResId = gatewayImgResIds[0];
        commonItems.add(commonItem);

        commonItem = new CommonItem();
        commonItem.bottomText = "库存量";
        commonItem.centerText = data.optString("storageTotalQuantity");
        commonItem.imgResId = gatewayImgResIds[1];
        commonItems.add(commonItem);

        commonItem = new CommonItem();
        commonItem.bottomText = "放装量";
        commonItem.centerText = data.optString("installQuantity");
        commonItem.imgResId = gatewayImgResIds[2];
        commonItems.add(commonItem);

        commonItem = new CommonItem();
        commonItem.bottomText = "昨日新增";
        commonItem.centerText = data.optString("yesterdayNewAddedQuantity");
        commonItem.imgResId = gatewayImgResIds[3];
        commonItems.add(commonItem);

        CommonAdapter commonAdapter = new CommonAdapter(commonItems);
        rvGatewayData.setAdapter(commonAdapter);
    }

    private void initBinds(String appBindingData) {
        ArrayList<CommonItem> commonItems = new ArrayList<>();
        CommonItem commonItem = new CommonItem();
        commonItem.bottomText = "绑定用户量";
        commonItem.centerText = JsonUtils.getJsonStringObjInKeyValue(appBindingData, "userBindingQuantity");
        commonItem.imgResId = bindImgResIds[0];
        commonItems.add(commonItem);

        commonItem = new CommonItem();
        commonItem.bottomText = "绑定终端";
        commonItem.centerText = JsonUtils.getJsonStringObjInKeyValue(appBindingData, "appBindingQuantity");
        commonItem.imgResId = bindImgResIds[1];
        commonItems.add(commonItem);
        commonItem = new CommonItem();

        commonItem.bottomText = "绑定率";
        commonItem.centerText = JsonUtils.getJsonStringObjInKeyValue(appBindingData, "bindingRatio");
        commonItem.imgResId = bindImgResIds[2];
        commonItems.add(commonItem);

        CommonAdapter commonAdapter = new CommonAdapter(commonItems);
        commonAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {

            }
        });
        rvBindings.setAdapter(commonAdapter);
    }

    private Animation.AnimationListener getAnimListener(final View view) {
        Animation.AnimationListener listener = new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
                view.setEnabled(false);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view.setEnabled(true);
                view.findViewById(R.id.itemRepairParent_imageRight).clearAnimation();
            }
        };
        return listener;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
        mLocationClient.unRegisterLocationListener(locationListener);
    }

    @Override
    protected void onLeftClick() {
        MainActivity mainActivity = (MainActivity) getActivity();
        mainActivity.closemenu();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null && requestCode == GET_AREA) {
            nowAreaItem = (AreaItem) data.getExtras().getSerializable("nowAreaItem");
            nowCity = nowAreaItem.areaName;
            loadData();
        }
    }
}
