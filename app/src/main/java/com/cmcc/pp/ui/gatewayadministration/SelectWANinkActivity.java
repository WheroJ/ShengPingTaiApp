package com.cmcc.pp.ui.gatewayadministration;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.cmcc.pp.R;
import com.cmcc.pp.base.BActivity;
import com.cmcc.pp.entity.gateway.WANListData;
import com.cmcc.pp.entity.gateway.WarningMessage;
import com.cmcc.pp.request.UserHttpUtils;
import com.cmcc.pp.ui.gatewayadministration.adapter.WANListDataAdapter;
import com.cmcc.pp.ui.gatewayadministration.adapter.WarningMessageAdapter;
import com.cmcc.pp.ui.widget.RefreshLinearLayout;
import com.cmcc.pp.util.JudgeErrorCodeUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import com.zrspring.libv2.network.RxUtils;
import com.zrspring.libv2.util.JsonUtils;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by ABC on 2017/12/26.
 * 网关管理---WAN链接选择
 */

public class SelectWANinkActivity extends BActivity {

    @Bind(R.id.recyclerView_wanink)
    RecyclerView recyclerViewWanink;
    @Bind(R.id.tv_null)
    TextView tvNull;
    @Bind(R.id.content)
    RefreshLinearLayout refreshLinearLayout;

    private String gatewayId ="";
    private JSONObject resultObject;
    private ArrayList<WANListData> arrayWANListData;
    private WANListData wANListData;
    private String  arrayWANListDataStr="";
    private String strTips = "";//提示文字
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    refreshLinearLayout.updateStatus(RefreshLinearLayout.FAIL, strTips, true);
                    recyclerViewWanink.setVisibility(View.GONE);
                    tvNull.setVisibility(View.VISIBLE);
                    break;
                case 2:
                    refreshLinearLayout.updateStatus(RefreshLinearLayout.SUCCESS);
                    break;
                case 3:
                    refreshLinearLayout.updateStatus(RefreshLinearLayout.LOADING);
                    break;
                case 4:
                    initData(arrayWANListDataStr);
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
        return R.layout.activity_selectwanink;
    }

    @Override
    public void initView(Bundle savedInstanceState) {

        getHeader().setTitle(R.string.choiceWAN);
        getHeader().showLeftText(R.string.cancel);
        getHeader().showRightText(R.string.finish);
        gatewayId = getIntent().getStringExtra("gatewayId");

        SharedPreferences wANListDataStr= getSharedPreferences("arrayWANListDataStr", Context.MODE_PRIVATE);
        arrayWANListDataStr = wANListDataStr.getString("arrayWANListDataStr","");

        if(!TextUtils.isEmpty(arrayWANListDataStr)){
            initData(arrayWANListDataStr);
        }else{
            if(!TextUtils.isEmpty(gatewayId)){
                Message message = Message.obtain();
                message.what = 3;
                handler.sendMessage(message);
                getWANListData();
            }
        }
    }

    @Override
    public void loadData() {

    }

    private void getWANListData() {
        UserHttpUtils.getWANListData(gatewayId,
                new RxUtils.DialogListener() {
                    @Override
                    public void onResult(@NotNull String result) {
                        try{
                            resultObject = new JSONObject(result);
                            Log.v("result",result);

                            arrayWANListDataStr = resultObject.optString("wanInfoList");
                            if (!TextUtils.isEmpty(arrayWANListDataStr)) {
                                SharedPreferences wANListDataStr = getSharedPreferences("arrayWANListDataStr", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = wANListDataStr.edit();
                                editor.putString("arrayWANListDataStr",arrayWANListDataStr);
                                editor.commit();
                            }

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

    private void initData(String string) {

        arrayWANListData = JsonUtils.json2Obj(string, new TypeReference<ArrayList<WANListData>>() {});
        recyclerViewWanink.setVisibility(View.VISIBLE);
        tvNull.setVisibility(View.GONE);
        recyclerViewWanink.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        final WANListDataAdapter wANListDataAdapter = new WANListDataAdapter(arrayWANListData);
        wANListDataAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                wANListData = arrayWANListData.get(position);
                for(int i = 0;i < arrayWANListData.size();i++){
                    arrayWANListData.get(i).isChecked = false;
                }
                arrayWANListData.get(position).isChecked = true;
                wANListDataAdapter.notifyDataSetChanged();
            }

        });
        recyclerViewWanink.setAdapter(wANListDataAdapter);
    }



    //完成选择带回选择的值
    @Override
    protected void onFinishClick() {
        if(wANListData!=null){
            Intent intent = new Intent();
            intent.putExtra("wANListData",(Serializable) wANListData);
            setResult(RESULT_OK,intent);
            finish();
        }else{
            setResult(RESULT_CANCELED);
            finish();
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
