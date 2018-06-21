package com.cmcc.pp.ui.workorder;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cmcc.pp.R;
import com.cmcc.pp.base.BActivity;
import com.cmcc.pp.request.UserHttpUtils;
import com.cmcc.pp.ui.widget.RefreshLinearLayout;
import com.cmcc.pp.util.JudgeErrorCodeUtils;
import com.zrspring.libv2.network.RxUtils;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by ABC on 2017/12/26.
 * 工单---设定参数值
 */

public class SettingParameterValuesActivity extends BActivity {
    @Bind(R.id.content)
    RefreshLinearLayout refreshLinearLayout;
    @Bind(R.id.lin_operationLogDetailStepValue)
    LinearLayout linOperationLogDetailStepValue;
    @Bind(R.id.tv_null)
    TextView tvNull;
    private String operationLogDetailId = "";
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
                    linOperationLogDetailStepValue.setVisibility(View.GONE);
                    tvNull.setVisibility(View.VISIBLE);
                    break;
                case 2:
                    refreshLinearLayout.updateStatus(RefreshLinearLayout.SUCCESS);
                    break;
                case 3:
                    refreshLinearLayout.updateStatus(RefreshLinearLayout.LOADING);
                    break;
                case 4:
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
        return R.layout.activity_settingparametervalues;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        getHeader().setTitle(R.string.settingparametervalues);
        operationLogDetailId = getIntent().getStringExtra("operationLogDetailId");
        if (!TextUtils.isEmpty(operationLogDetailId)) {
            getWorkOrderOperationLogDetailData();
        }
    }

    private void getWorkOrderOperationLogDetailData() {
        UserHttpUtils.getWorkOrderOperationLogDetailData(operationLogDetailId,
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
                        strTips = JudgeErrorCodeUtils.getJudgeErrorTips(e.getMessage());
                        handler.sendEmptyMessageDelayed(1, 3000);
                    }
                });
    }

    private void showResultData() {
    }

    @Override
    public void loadData() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
