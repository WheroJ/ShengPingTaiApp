package com.cmcc.pp.ui.gatewayadministration;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.cmcc.pp.R;
import com.cmcc.pp.base.BActivity;
import com.cmcc.pp.entity.gateway.WANListData;
import com.cmcc.pp.request.UserHttpUtils;
import com.cmcc.pp.ui.widget.RefreshLinearLayout;
import com.cmcc.pp.util.JudgeErrorCodeUtils;
import com.zrspring.libv2.network.RxUtils;
import com.zrspring.libv2.util.ToastUtils;
import com.zrspring.libv2.view.ProgressDlg;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by ABC on 2017/12/25.
 * Ping诊断
 */

public class PingDiagnosisActivity extends BActivity {

    @Bind(R.id.pingdiag_lin_choiceWAN)
    LinearLayout pingdiagLinChoiceWAN;
    @Bind(R.id.pingdiag_butt_start)
    Button pingdiagButtStart;
    @Bind(R.id.pingdiag_lin_diagnosis)
    LinearLayout pingdiagLinDiagnosis;
    @Bind(R.id.pingdiag_scroll)
    ScrollView pingdiagScroll;
    @Bind(R.id.tv_connectionType)
    TextView tvConnectionType;
    @Bind(R.id.edt_host)
    EditText edtHost;
    @Bind(R.id.edt_dataBlockSize)
    EditText edtDataBlockSize;
    @Bind(R.id.edt_numberOfRepetitions)
    EditText edtNumberOfRepetitions;
    @Bind(R.id.edt_timeout)
    EditText edtTimeout;
    @Bind(R.id.content)
    RefreshLinearLayout refreshLinearLayout;
    @Bind(R.id.tv_host)
    TextView tvHost;
    @Bind(R.id.tv_numberOfRepetitions)
    TextView tvNumberOfRepetitions;
    @Bind(R.id.tv_timeout)
    TextView tvTimeout;
    @Bind(R.id.tv_dataBlockSize)
    TextView tvDataBlockSize;
    @Bind(R.id.tv_successCount)
    TextView tvSuccessCount;
    @Bind(R.id.tv_failureCount)
    TextView tvFailureCount;
    @Bind(R.id.tv_averageResponseTime)
    TextView tvAverageResponseTime;
    @Bind(R.id.tv_minimumResponseTime)
    TextView tvMinimumResponseTime;
    @Bind(R.id.tv_maximumResponseTime)
    TextView tvMaximumResponseTime;

    private String gatewayId = "";
    private String host = "";
    private String dataBlockSize = "";
    private String numberOfRepetitions = "";
    private String timeout = "";
    private String vlanIdMark = "";
    private String wanPath = "";

    private WANListData wANListData;
    private JSONObject resultObject;
    private String strTips = "";//提示文字
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    refreshLinearLayout.updateStatus(RefreshLinearLayout.FAIL, strTips, true);
                    pingdiagLinDiagnosis.setVisibility(View.VISIBLE);
                    pingdiagScroll.setVisibility(View.GONE);
                    break;
                case 2:
                    refreshLinearLayout.updateStatus(RefreshLinearLayout.SUCCESS, "诊断成功", true);
                    break;
                case 3:
                    refreshLinearLayout.updateStatus(RefreshLinearLayout.LOADING, "诊断中......", false);
                    break;
                case 4:
                    pingdiagScroll.setVisibility(View.VISIBLE);
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
        return R.layout.activity_pingdiagnosis;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        getHeader().setTitle(R.string.str_pingdiagnosis);
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
                        tvConnectionType.setText(wANListData.connectionType);
                    }

                    break;

            }
        }
    }


    @OnClick({
            R.id.pingdiag_lin_choiceWAN,
            R.id.pingdiag_butt_start
    })
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.pingdiag_lin_choiceWAN:
                openChoiceWAN();
                break;
            case R.id.pingdiag_butt_start:
                if (IsValid()) {
                    startPingDiagnosis();
                }

                break;
        }
    }

    private boolean IsValid() {
        boolean result = true;
        host = edtHost.getText().toString().trim();
        timeout = edtTimeout.getText().toString().trim();
        dataBlockSize = edtDataBlockSize.getText().toString().trim();
        numberOfRepetitions = edtNumberOfRepetitions.getText().toString().trim();


        if (TextUtils.isEmpty(host)) {
            ToastUtils.show("请输入链接地址");
            result = false;
        } else if (TextUtils.isEmpty(dataBlockSize)) {
            ToastUtils.show("请输入包大小");
            result = false;
        } else if (TextUtils.isEmpty(numberOfRepetitions)) {
            ToastUtils.show("请输入PING的次数");
            result = false;
        } else if (TextUtils.isEmpty(timeout)) {
            ToastUtils.show("请输超时时间");
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

    //开始Ping诊断，显示诊断结果
    private void startPingDiagnosis() {
        Message message = Message.obtain();
        message.what = 3;
        handler.sendMessage(message);
        pingdiagLinDiagnosis.setVisibility(View.GONE);
        pingDiagnose();
    }

    //诊断
    private void pingDiagnose() {
        UserHttpUtils.pingDiagnose(gatewayId,
                host,
                dataBlockSize,
                numberOfRepetitions,
                timeout,
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


    private void showResultData() {
        try {
            if(!TextUtils.isEmpty(resultObject.getString("host"))){
                tvHost.setText(resultObject.getString("host"));
            }

            if(!TextUtils.isEmpty(resultObject.getString("numberOfRepetitions"))){
                tvNumberOfRepetitions.setText(resultObject.getString("numberOfRepetitions"));
            }

            if(!TextUtils.isEmpty(resultObject.getString("timeout"))){
                tvTimeout.setText(resultObject.getString("timeout"));
            }

            if(!TextUtils.isEmpty(resultObject.getString("dataBlockSize"))){
                tvDataBlockSize.setText(resultObject.getString("dataBlockSize"));
            }

            if(!TextUtils.isEmpty(resultObject.getString("successCount"))){
                tvSuccessCount.setText(resultObject.getString("successCount"));
            }

            if(!TextUtils.isEmpty(resultObject.getString("failureCount"))){
                tvFailureCount.setText(resultObject.getString("failureCount"));
            }

            if(!TextUtils.isEmpty(resultObject.getString("averageResponseTime"))){
                tvAverageResponseTime.setText(resultObject.getString("averageResponseTime"));
            }

            if(!TextUtils.isEmpty(resultObject.getString("minimumResponseTime"))){
                tvMinimumResponseTime.setText(resultObject.getString("minimumResponseTime"));
            }

            if(!TextUtils.isEmpty(resultObject.getString("maximumResponseTime"))){
                tvMaximumResponseTime.setText(resultObject.getString("maximumResponseTime"));
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    //选择WAN
    private void openChoiceWAN() {
        Intent wanIntent = new Intent(this, SelectWANinkActivity.class);
        wanIntent.putExtra("gatewayId", gatewayId);
        startActivityForResult(wanIntent, 1);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
