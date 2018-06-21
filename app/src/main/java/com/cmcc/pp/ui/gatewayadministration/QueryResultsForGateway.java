package com.cmcc.pp.ui.gatewayadministration;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cmcc.pp.R;
import com.cmcc.pp.base.BActivity;
import com.cmcc.pp.entity.gateway.GatewayInfo;
import com.cmcc.pp.util.IntentUtils;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by ABC on 2017/12/19.
 * 网关查询结果
 */

public class QueryResultsForGateway extends BActivity {

    @Bind(R.id.lin_results)
    LinearLayout linResults;
    @Bind(R.id.tv_null)
    TextView tvNull;
    @Bind(R.id.queryresultsforgateway_tv_whiceone)
    TextView queryresultsforgatewayTvWhiceone;
    @Bind(R.id.queryresultsforgateway_tv_gatewaySN)
    TextView queryresultsforgatewayTvGatewaySN;
    @Bind(R.id.queryresultsforgateway_tv_manufacturerName)
    TextView queryresultsforgatewayTvManufacturerName;
    @Bind(R.id.queryresultsforgateway_tv_macAddress)
    TextView queryresultsforgatewayTvMacAddress;
    @Bind(R.id.queryresultsforgateway_tv_wbandAccount)
    TextView queryresultsforgatewayTvWbandAccount;
    @Bind(R.id.queryresultsforgateway_tv_loid)
    TextView queryresultsforgatewayTvLoid;


    private GatewayInfo gatewayInfo;
    private String userAccount="";

    @Override
    protected boolean getHasTitle() {
        return true;
    }

    @Override
    public int getContentLayoutId() {
        return R.layout.activity_queryresultsforgateway;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        getHeader().setTitle(R.string.str_queryresults);
        gatewayInfo = (GatewayInfo) getIntent().getSerializableExtra("gatewayInfo");
        userAccount = getIntent().getStringExtra("userAccount");
        showInfo();
    }


    @Override
    public void loadData() {

    }

    private void showInfo() {
        queryresultsforgatewayTvWhiceone.setText(userAccount);
        if(gatewayInfo!=null && gatewayInfo.gatewayId!=null){
            queryresultsforgatewayTvGatewaySN.setText(gatewayInfo.gatewaySN);
            queryresultsforgatewayTvManufacturerName.setText(gatewayInfo.manufacturerName);
            queryresultsforgatewayTvMacAddress.setText(gatewayInfo.macAddress);
            queryresultsforgatewayTvWbandAccount.setText(gatewayInfo.wbandAccount);
            queryresultsforgatewayTvLoid.setText(gatewayInfo.loid);

            if(gatewayInfo.accountMatchingFlag == 1){
                queryresultsforgatewayTvGatewaySN.setTextColor(getResources().getColor(R.color.main_blue));
            }else if(gatewayInfo.accountMatchingFlag == 2){
                queryresultsforgatewayTvMacAddress.setTextColor(getResources().getColor(R.color.main_blue));
            }else if(gatewayInfo.accountMatchingFlag == 3){
                queryresultsforgatewayTvWbandAccount.setTextColor(getResources().getColor(R.color.main_blue));
            }else if(gatewayInfo.accountMatchingFlag == 4){
                queryresultsforgatewayTvLoid.setTextColor(getResources().getColor(R.color.main_blue));
            }

        }else{
            linResults.setVisibility(View.GONE);
            tvNull.setText("未找到网关");
            tvNull.setVisibility(View.VISIBLE);
        }
    }


    @OnClick({
            R.id.lin_results
    })
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.lin_results:
                openDetails();
                break;
        }
    }

    //网关详情
    private void openDetails() {
        IntentUtils.goGatewayDetails(this,gatewayInfo.gatewayId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
