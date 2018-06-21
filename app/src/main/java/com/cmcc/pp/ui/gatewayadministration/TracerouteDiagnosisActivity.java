package com.cmcc.pp.ui.gatewayadministration;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.cmcc.pp.R;
import com.cmcc.pp.base.BActivity;
import com.cmcc.pp.entity.gateway.RouteHopsListData;
import com.cmcc.pp.entity.gateway.WANListData;
import com.cmcc.pp.request.UserHttpUtils;
import com.cmcc.pp.ui.gatewayadministration.adapter.TracerouteDiagnosisAdapter;
import com.cmcc.pp.ui.widget.RefreshLinearLayout;
import com.cmcc.pp.util.JudgeErrorCodeUtils;
import com.cmcc.pp.util.PopuWindowForModesChoice;
import com.fasterxml.jackson.core.type.TypeReference;
import com.zrspring.libv2.network.RxUtils;
import com.zrspring.libv2.util.JsonUtils;
import com.zrspring.libv2.util.ToastUtils;
import com.zrspring.libv2.view.ProgressDlg;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by ABC on 2017/12/26.
 * <p>
 * Traceroute诊断
 */

public class TracerouteDiagnosisActivity extends BActivity implements View.OnClickListener {
    @Bind(R.id.trace_lin_choiceWAN)
    LinearLayout traceLinChoiceWAN;
    @Bind(R.id.trace_butt_start)
    Button traceButtStart;


    @Bind(R.id.tv_modechoice)
    TextView tvModechoice;
    @Bind(R.id.lin_modechoice)
    LinearLayout linModechoice;

    @Bind(R.id.tv_connectionType)
    TextView tv_connectionType;
    @Bind(R.id.edt_host)
    EditText edtHost;
    @Bind(R.id.edt_numberOfTries)
    EditText edtNumberOfTries;
    @Bind(R.id.edt_timeout)
    EditText edtTimeout;
    @Bind(R.id.edt_dataBlockSize)
    EditText edtDataBlockSize;
    @Bind(R.id.edt_dscp)
    EditText edtDscp;
    @Bind(R.id.edt_maxHopCount)
    EditText edtMaxHopCount;

    @Bind(R.id.tracediag_scroll)
    ScrollView tracediagScroll;
    @Bind(R.id.trace_scrollview_diagnosis)
    ScrollView traceScrollviewDiagnosis;
    @Bind(R.id.content)
    RefreshLinearLayout refreshLinearLayout;
    @Bind(R.id.tv_diagnosticsState)
    TextView tvDiagnosticsState;
    @Bind(R.id.tv_responseTime)
    TextView tvResponseTime;
    @Bind(R.id.tv_hopsNumberOfEntries)
    TextView tvHopsNumberOfEntries;
    @Bind(R.id.recyclerView_routeHopsListData)
    RecyclerView recyclerViewRouteHopsListData;

    private String gatewayId = "";
    private String mode = "UDP";
    private String host = "www.baidu.com";
    private String numberOfTries = "3";
    private String timeout = "5000";
    private String dataBlockSize = "38";
    private String dscp = "0";
    private String maxHopCount = "30";
    private String vlanIdMark = "";
    private String wanPath = "";

    private WANListData wANListData;
    private JSONObject resultObject;
    private String strTips = "";//提示文字
    private ArrayList<RouteHopsListData> arrayRouteHopsListData;
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    refreshLinearLayout.updateStatus(RefreshLinearLayout.FAIL, strTips, true);
                    traceScrollviewDiagnosis.setVisibility(View.VISIBLE);
                    tracediagScroll.setVisibility(View.GONE);
                    break;
                case 2:
                    refreshLinearLayout.updateStatus(RefreshLinearLayout.SUCCESS, "诊断成功", true);
                    break;
                case 3:
                    refreshLinearLayout.updateStatus(RefreshLinearLayout.LOADING, "诊断中......", false);
                    break;
                case 4:
                    tracediagScroll.setVisibility(View.VISIBLE);
                    showResultData();
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
        return R.layout.activity_traceroutediagnosis;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        getHeader().setTitle(R.string.str_traceroutediagnosis);
        gatewayId = getIntent().getStringExtra("gatewayId");
    }


    @Override
    public void loadData() {

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 1:
                    if (data != null) {
                        wANListData = (WANListData) data.getSerializableExtra("wANListData");
                        vlanIdMark = wANListData.vlanIdMark;
                        wanPath = wANListData.wanPath;
                        tv_connectionType.setText(wANListData.connectionType);
                    }

                    break;

            }
        }
    }


    @OnClick({
            R.id.trace_lin_choiceWAN,
            R.id.trace_butt_start,
            R.id.lin_modechoice,

    })
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.trace_lin_choiceWAN:
                openChoiceWAN();
                break;
            case R.id.trace_butt_start:
                if (IsValid()) {
                    startTraceDiagnosis();
                }
                break;
            case R.id.lin_modechoice:
                PopuWindowForModesChoice popuWindowForSetting = new PopuWindowForModesChoice();
                popuWindowForSetting.choicewindow(TracerouteDiagnosisActivity.this, linModechoice);
                break;
        }
    }

    private boolean IsValid() {
        boolean result = true;
        mode = tvModechoice.getText().toString().trim();
        host = edtHost.getText().toString().trim();
        numberOfTries = edtNumberOfTries.getText().toString().trim();
        timeout = edtTimeout.getText().toString().trim();
        dataBlockSize = edtDataBlockSize.getText().toString().trim();
        dscp = edtDscp.getText().toString().trim();
        maxHopCount = edtMaxHopCount.getText().toString().trim();


        if (TextUtils.isEmpty(mode)) {
            ToastUtils.show("请选择诊断采用的协议类型");
            result = false;
        } else if (TextUtils.isEmpty(host)) {
            ToastUtils.show("请输入链接地址");
            result = false;
        } else if (TextUtils.isEmpty(numberOfTries)) {
            ToastUtils.show("请输入每跳重复次数");
            result = false;
        } else if (TextUtils.isEmpty(timeout)) {
            ToastUtils.show("请输入诊断超时时间");
            result = false;
        } else if (TextUtils.isEmpty(dataBlockSize)) {
            ToastUtils.show("请输入每个Traceroute包发送的数据块大小");
            result = false;
        } else if (TextUtils.isEmpty(dscp)) {
            ToastUtils.show("请输入DSCP值");
            result = false;
        } else if (TextUtils.isEmpty(maxHopCount)) {
            ToastUtils.show("请输入最大跳数");
            result = false;
        } else if (TextUtils.isEmpty(vlanIdMark)) {
            ToastUtils.show("请选择WAN链接");
            result = false;
        } else if (TextUtils.isEmpty(wanPath)) {
            ToastUtils.show("请选择WAN链接");
            result = false;
        }

        return result;
    }


    //开始诊断，显示诊断结果
    private void startTraceDiagnosis() {
        Message message = Message.obtain();
        message.what = 3;
        handler.sendMessage(message);
        traceScrollviewDiagnosis.setVisibility(View.GONE);
        tracerouteDiagnose();
    }

    private void tracerouteDiagnose() {
        UserHttpUtils.tracerouteDiagnose(gatewayId,
                mode,
                host,
                numberOfTries,
                timeout,
                dataBlockSize,
                dscp,
                maxHopCount,
                vlanIdMark,
                wanPath,
                new RxUtils.DialogListener() {
                    @Override
                    public void onResult(@NotNull String result) {
                        try {
                            resultObject = new JSONObject(result);
                            Log.v("result", result);
                            handler.sendEmptyMessageDelayed(2, 3000);
                            Message message = Message.obtain();
                            message.what = 4;
                            handler.sendMessage(message);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(@NotNull Throwable e) {
                        super.onError(e);
                        strTips = JudgeErrorCodeUtils.getJudgeErrorTips(e.getMessage());
                        handler.sendEmptyMessageDelayed(1, 3000);
                    }
                });
    }

    //选择WAN
    private void openChoiceWAN() {
        Intent wanIntent = new Intent(this, SelectWANinkActivity.class);
        wanIntent.putExtra("gatewayId", gatewayId);
        startActivityForResult(wanIntent, 1);
    }


    public void setTvModechoice(String string) {
        tvModechoice.setText(string);
        mode = string;
    }


    //显示诊断结果
    private void showResultData() {
        try {
            tvDiagnosticsState.setText(resultObject.getString("diagnosticsState"));
            tvResponseTime.setText(resultObject.getString("responseTime"));
            tvHopsNumberOfEntries.setText(resultObject.getString("hopsNumberOfEntries"));

            String arrayRouteHopsListDataStr = resultObject.optString("routeHopsListData");
            if (!TextUtils.isEmpty(arrayRouteHopsListDataStr)) {
                arrayRouteHopsListData = JsonUtils.json2Obj(arrayRouteHopsListDataStr, new TypeReference<ArrayList<RouteHopsListData>>() {
                });
            }
            recyclerViewRouteHopsListData.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
            TracerouteDiagnosisAdapter tracerouteDiagnosisAdapter = new TracerouteDiagnosisAdapter(arrayRouteHopsListData);
            recyclerViewRouteHopsListData.setAdapter(tracerouteDiagnosisAdapter);

        } catch (JSONException e) {
            e.printStackTrace();
        }



    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }


}
