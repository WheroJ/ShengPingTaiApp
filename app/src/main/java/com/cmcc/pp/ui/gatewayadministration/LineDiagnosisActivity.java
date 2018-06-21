package com.cmcc.pp.ui.gatewayadministration;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import com.zrspring.libv2.view.ProgressDlg;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by ABC on 2017/12/20.
 * 网关管理----线路诊断
 */

public class LineDiagnosisActivity extends BActivity {

    @Bind(R.id.tv_status)
    TextView tvStatus;
    @Bind(R.id.tv_tXPower)
    TextView tvTXPower;
    @Bind(R.id.tv_rXPower)
    TextView tvRXPower;
    @Bind(R.id.tv_transceiverTemperature)
    TextView tvTransceiverTemperature;
    @Bind(R.id.tv_supplyVottage)
    TextView tvSupplyVottage;
    @Bind(R.id.tv_biasCurrent)
    TextView tvBiasCurrent;
    @Bind(R.id.tv_bytesSent)
    TextView tvBytesSent;
    @Bind(R.id.tv_bytesReceived)
    TextView tvBytesReceived;
    @Bind(R.id.tv_packetsSent)
    TextView tvPacketsSent;
    @Bind(R.id.tv_packetsReceived)
    TextView tvPacketsReceived;
    @Bind(R.id.tv_sUnicastPackets)
    TextView tvSUnicastPackets;
    @Bind(R.id.textView)
    TextView textView;
    @Bind(R.id.tv_rUnicastPackets)
    TextView tvRUnicastPackets;
    @Bind(R.id.tv_sMulticastPackets)
    TextView tvSMulticastPackets;
    @Bind(R.id.tv_rMulticastPackets)
    TextView tvRMulticastPackets;
    @Bind(R.id.tv_sBroadcastPackets)
    TextView tvSBroadcastPackets;
    @Bind(R.id.tv_rBroadcastPackets)
    TextView tvRBroadcastPackets;
    @Bind(R.id.tv_fECError)
    TextView tvFECError;
    @Bind(R.id.tv_hECError)
    TextView tvHECError;
    @Bind(R.id.tv_dropPackets)
    TextView tvDropPackets;
    @Bind(R.id.tv_spausePackets)
    TextView tvSpausePackets;
    @Bind(R.id.tv_rpausePackets)
    TextView tvRpausePackets;

    @Bind(R.id.lin_linediagnosis)
    LinearLayout linLinediagnosis;
    @Bind(R.id.content)
    RefreshLinearLayout refreshLinearLayout;
    private String gatewayId = "";
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
                    break;
                case 2:
                    refreshLinearLayout.updateStatus(RefreshLinearLayout.SUCCESS,"线路诊断成功",true);
                    break;
                case 3:
                    refreshLinearLayout.updateStatus(RefreshLinearLayout.LOADING,"诊断中......",false);
                    break;
                case 4:
                    linLinediagnosis.setVisibility(View.VISIBLE);
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
        return R.layout.activity_linediagnosis;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        getHeader().setTitle(R.string.str_linediagnosis);
        gatewayId = getIntent().getStringExtra("gatewayId");
    }

    @Override
    public void loadData() {
        if(!TextUtils.isEmpty(gatewayId)){
            Message message = Message.obtain();
            message.what = 3;
            handler.sendMessage(message);
            lineDiagnose();
        }
    }

    private void lineDiagnose() {
        UserHttpUtils.lineDiagnose(gatewayId,
                new RxUtils.DefaultListener() {
                    @Override
                    public void onResult(@NotNull String result) {
                        try {
                            resultObject = new JSONObject(result);
                            Log.v("result",result);
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


    private void initData(JSONObject resultObject) {

        try {
            if(!TextUtils.isEmpty(resultObject.getString("status"))){
                tvStatus.setText(resultObject.getString("status"));
            }

            if(!TextUtils.isEmpty(resultObject.getString("tXPower"))){
                tvTXPower.setText(resultObject.getString("tXPower"));
            }

            if(!TextUtils.isEmpty(resultObject.getString("rXPower"))){
                tvRXPower.setText(resultObject.getString("rXPower"));
            }

            if(!TextUtils.isEmpty(resultObject.getString("transceiverTemperature"))){
                tvTransceiverTemperature.setText(resultObject.getString("transceiverTemperature"));
            }

            if(!TextUtils.isEmpty(resultObject.getString("supplyVottage"))){
                tvSupplyVottage.setText(resultObject.getString("supplyVottage"));
            }

            if(!TextUtils.isEmpty(resultObject.getString("biasCurrent"))){
                tvBiasCurrent.setText(resultObject.getString("biasCurrent"));
            }

            if(!TextUtils.isEmpty(resultObject.getString("bytesSent"))){
                tvBytesSent.setText(resultObject.getString("bytesSent"));
            }

            if(!TextUtils.isEmpty(resultObject.getString("bytesReceived"))){
                tvBytesReceived.setText(resultObject.getString("bytesReceived"));
            }

            if(!TextUtils.isEmpty(resultObject.getString("packetsSent"))){
                tvPacketsSent.setText(resultObject.getString("packetsSent"));
            }

            if(!TextUtils.isEmpty(resultObject.getString("packetsReceived"))){
                tvPacketsReceived.setText(resultObject.getString("packetsReceived"));
            }

            if(!TextUtils.isEmpty(resultObject.getString("sUnicastPackets"))){
                tvSUnicastPackets.setText(resultObject.getString("sUnicastPackets"));
            }

            if(!TextUtils.isEmpty(resultObject.getString("rUnicastPackets"))){
                tvRUnicastPackets.setText(resultObject.getString("rUnicastPackets"));
            }

            if(!TextUtils.isEmpty(resultObject.getString("sMulticastPackets"))){
                tvSMulticastPackets.setText(resultObject.getString("sMulticastPackets"));
            }

            if(!TextUtils.isEmpty(resultObject.getString("rMulticastPackets"))){
                tvRMulticastPackets.setText(resultObject.getString("rMulticastPackets"));
            }

            if(!TextUtils.isEmpty(resultObject.getString("sBroadcastPackets"))){
                tvSBroadcastPackets.setText(resultObject.getString("sBroadcastPackets"));
            }

            if(!TextUtils.isEmpty(resultObject.getString("rBroadcastPackets"))){
                tvRBroadcastPackets.setText(resultObject.getString("rBroadcastPackets"));
            }

            if(!TextUtils.isEmpty(resultObject.getString("fECError"))){
                tvFECError.setText(resultObject.getString("fECError"));
            }

            if(!TextUtils.isEmpty(resultObject.getString("hECError"))){
                tvHECError.setText(resultObject.getString("hECError"));
            }

            if(!TextUtils.isEmpty(resultObject.getString("dropPackets"))){
                tvDropPackets.setText(resultObject.getString("dropPackets"));
            }

            if(!TextUtils.isEmpty(resultObject.getString("spausePackets"))){
                tvSpausePackets.setText(resultObject.getString("spausePackets"));
            }

            if(!TextUtils.isEmpty(resultObject.getString("rpausePackets"))){
                tvRpausePackets.setText(resultObject.getString("rpausePackets"));
            }

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
