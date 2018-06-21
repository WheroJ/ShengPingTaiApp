package com.cmcc.pp.ui.gatewayadministration;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cmcc.pp.R;
import com.cmcc.pp.base.BActivity;
import com.cmcc.pp.entity.gateway.WarningMessage;
import com.cmcc.pp.request.UserHttpUtils;
import com.cmcc.pp.ui.widget.RefreshLinearLayout;
import com.cmcc.pp.util.IntentUtils;
import com.cmcc.pp.util.JudgeErrorCodeUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import com.zrspring.libv2.network.RxUtils;
import com.zrspring.libv2.util.JsonUtils;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by ABC on 2017/12/20.
 * 网关详情
 */

public class GatewayDetailsActivity extends BActivity {

    @Bind(R.id.details_relat_morewaring)
    RelativeLayout detailsRelatMorewaring;
    @Bind(R.id.gateway_rela_moreinfo)
    RelativeLayout gatewayRelaMoreinfo;
    @Bind(R.id.gatewaydetails_but_linediagnosis)
    RelativeLayout gatewayDetailsButlinediagnosis;
    @Bind(R.id.gatewaydetails_relat_pingdiagnosis)
    RelativeLayout gatewaydetails_relat_pingdiagnosis;
    @Bind(R.id.gatewaydetails_relat_tracediagnosis)
    RelativeLayout gatewaydetails_relat_tracediagnosis;
    @Bind(R.id.details_tv_upsp)
    TextView detailsTvUpsp;
    @Bind(R.id.details_tv_kpbs)
    TextView detailsTvKpbs;
    @Bind(R.id.gatewaydetails_terminalSN)
    TextView gatewaydetailsTerminalSN;
    @Bind(R.id.gatewaydetails_manufacturerName)
    TextView gatewaydetailsManufacturerName;
    @Bind(R.id.details_tv_morewaring)
    TextView details_tv_morewaring;
    @Bind(R.id.content)
    RefreshLinearLayout refreshLinearLayout;

    private String gatewayId="";
    private JSONObject resultObject;
    private ArrayList<WarningMessage> alarmInfoData;
    private JSONObject gatewayInfoData;
    private String strTips = "";//提示文字
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    refreshLinearLayout.updateStatus(RefreshLinearLayout.FAIL, strTips, true);
                    break;
                case 2:
                    refreshLinearLayout.updateStatus(RefreshLinearLayout.SUCCESS);
                    break;
                case 3:
                    refreshLinearLayout.updateStatus(RefreshLinearLayout.LOADING);
                    break;
                case 4:
                    initData(resultObject);
                    break;
            }
        }
    };
    @Override
    protected boolean getHasTitle() {
        return true;
    }

    @Override
    public int getContentLayoutId() {
        return R.layout.activity_gatewaydetails;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        getHeader().setTitle(R.string.str_gatewaydetails);
        gatewayId = getIntent().getStringExtra("gatewayId");
    }

    @Override
    public void loadData() {
        if(!TextUtils.isEmpty(gatewayId)){
            Message message = Message.obtain();
            message.what = 3;
            handler.sendMessage(message);
            getGatewayInfoDetailData();
        }
    }


    public void getGatewayInfoDetailData() {
        UserHttpUtils.getGatewayInfoDetailData(gatewayId,
                new RxUtils.DialogListener() {
                    @Override
                    public void onResult(@NotNull String result) {
                        try{
                            resultObject = new JSONObject(result);
                            Log.v("result",result);
                            handler.sendEmptyMessageDelayed(2, 3000);
                            Message message = Message.obtain();
                            message.what = 4;
                            handler.sendMessage(message);

                        }catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onError(@NotNull Throwable e) {
                        super.onError(e);
                        showUpHeader(JudgeErrorCodeUtils.getJudgeErrorTips(e.getMessage()));
                    }
                });
    }

    //显示顶部提示
    private void showUpHeader(String strMessage) {
        strTips = strMessage;
        handler.sendEmptyMessageDelayed(1, 3000);
    }

    //显示数据
    private void initData(JSONObject resultObject) {
        try {
            detailsTvUpsp.setText(resultObject.getString("upSpeed"));
            detailsTvKpbs.setText(resultObject.getString("downloadSpeed"));

            String alarmInfoDataStr = resultObject.optString("alarmInfoData");
            if(!TextUtils.isEmpty(alarmInfoDataStr)){
                alarmInfoData = JsonUtils.json2Obj(alarmInfoDataStr, new TypeReference<ArrayList<WarningMessage>>() {
                });
                details_tv_morewaring.setText(alarmInfoData.size()+"个");
            }

            gatewayInfoData = new JSONObject(resultObject.optString("gatewayInfoData"));
            gatewaydetailsTerminalSN.setText(gatewayInfoData.getString("terminalSN"));
            gatewaydetailsManufacturerName.setText(gatewayInfoData.getString("manufacturerName"));

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @OnClick({
            R.id.details_relat_morewaring,
            R.id.gateway_rela_moreinfo,
            R.id.gatewaydetails_but_linediagnosis,
            R.id.gatewaydetails_relat_pingdiagnosis,
            R.id.gatewaydetails_relat_tracediagnosis

    })
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.details_relat_morewaring:
                openWaring();
                break;
            case R.id.gateway_rela_moreinfo:
                openMoreInfo();
                break;
            case R.id.gatewaydetails_but_linediagnosis:
                openLineDiagnosis();
                break;
            case R.id.gatewaydetails_relat_pingdiagnosis:
                openPingdiagnosis();
                break;
            case R.id.gatewaydetails_relat_tracediagnosis:
                openTraceDiagnosis();
                break;
        }
    }

    //Traceroute诊断
    private void openTraceDiagnosis() {
        IntentUtils.goTracerouteDiagnosis(this,gatewayId);
    }

    //Ping诊断
    private void openPingdiagnosis() {
        IntentUtils.goPingDiagnosis(this,gatewayId);

    }

    //线路诊断
    private void openLineDiagnosis() {
        IntentUtils.goLineDiagnosis(this,gatewayId);

    }

    //更多信息
    private void openMoreInfo() {
        IntentUtils.goMoreInformationForGateway(this,gatewayInfoData.toString());
    }

    //告警信息
    private void openWaring() {
        IntentUtils.goWarningMessage(this,alarmInfoData);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }


}
