package com.cmcc.pp.ui.gatewayadministration;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.cmcc.pp.R;
import com.cmcc.pp.base.BActivity;
import com.cmcc.pp.entity.gateway.WarningMessage;
import com.cmcc.pp.entity.transactiondata.AreaItem;
import com.cmcc.pp.ui.gatewayadministration.adapter.WarningMessageAdapter;
import com.fasterxml.jackson.core.type.TypeReference;
import com.zrspring.libv2.util.JsonUtils;
import com.zrspring.libv2.util.LogPrinter;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by ABC on 2017/12/20.
 * 网关查询---告警信息
 */

public class WarningMessageActivity extends BActivity {

    @Bind(R.id.RecyclerView)
    RecyclerView RecyclerView;
    @Bind(R.id.tv_null)
    TextView tv_null;

    private ArrayList<WarningMessage> alarmInfoData;

    @Override
    protected boolean getHasTitle() {
        return true;
    }

    @Override
    public int getContentLayoutId() {
        return R.layout.activity_warningmessage;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        getHeader().setTitle(R.string.str_warningmessage);
        RecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        alarmInfoData = (ArrayList<WarningMessage>) getIntent().getSerializableExtra("alarmInfoData");
        if(alarmInfoData!=null && alarmInfoData.size()>0){

            LogPrinter.i("alarmInfoData", "alarmInfoData = " + alarmInfoData);
            getHeader().setSubTitle("("+alarmInfoData.size()+")");
            RecyclerView.setVisibility(View.VISIBLE);
            tv_null.setVisibility(View.GONE);
            WarningMessageAdapter warningMessageAdapter = new WarningMessageAdapter(alarmInfoData);
            RecyclerView.setAdapter(warningMessageAdapter);
        }else{
            RecyclerView.setVisibility(View.GONE);
            tv_null.setVisibility(View.VISIBLE);
        }

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
