package com.cmcc.pp.ui.workorder;

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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.cmcc.pp.R;
import com.cmcc.pp.base.BActivity;
import com.cmcc.pp.entity.WorkOrder.WorkOrderDetailsOperationLogListDataEntity;
import com.cmcc.pp.entity.gateway.WANListData;
import com.cmcc.pp.request.UserHttpUtils;
import com.cmcc.pp.ui.widget.RefreshLinearLayout;
import com.cmcc.pp.ui.workorder.adapter.WorkOrderDetailsOperationLogListDataAdapter;
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

/**
 * Created by ABC on 2017/12/26.
 * 工单详情
 */

public class WorkOrderDetailsActivity extends BActivity {
    @Bind(R.id.workorderdetails_workOrderStatus)
    TextView workorderdetailsWorkOrderStatus;
    @Bind(R.id.workorderdetails_terminalSN)
    TextView workorderdetailsTerminalSN;
    @Bind(R.id.workorderdetails_terminalRegisterStatus)
    TextView workorderdetailsTerminalRegisterStatus;
    @Bind(R.id.workorderdetails_manufacturerName)
    TextView workorderdetailsManufacturerName;
    @Bind(R.id.workorderdetails_terminalModel)
    TextView workorderdetailsTerminalModel;
    @Bind(R.id.workorderdetails_softwareVersion)
    TextView workorderdetailsSoftwareVersion;
    @Bind(R.id.workorderdetails_hardwareVersion)
    TextView workorderdetailsHardwareVersion;
    @Bind(R.id.workorderdetails_ipAddress)
    TextView workorderdetailsIpAddress;
    @Bind(R.id.workorderdetails_macAddress)
    TextView workorderdetailsMacAddress;
    @Bind(R.id.workorderdetails_joinNetDayTime)
    TextView workorderdetailsJoinNetDayTime;
    @Bind(R.id.workorderdetails_recentConnectionDayTime)
    TextView workorderdetailsRecentConnectionDayTime;
    @Bind(R.id.workOrderData_workOrderNo)
    TextView workOrderDataWorkOrderNo;
    @Bind(R.id.workOrderData_businessType)
    TextView workOrderDataBusinessType;
    @Bind(R.id.workOrderData_provinceCode)
    TextView workOrderDataProvinceCode;
    @Bind(R.id.workOrderData_businessCode)
    TextView workOrderDataBusinessCode;
    @Bind(R.id.workOrderData_loid)
    TextView workOrderDataLoid;
    @Bind(R.id.workOrderData_openWorkOrderDayTime)
    TextView workOrderDataOpenWorkOrderDayTime;
    @Bind(R.id.workOrderData_attributionArea)
    TextView workOrderDataAttributionArea;
    @Bind(R.id.workOrderData_wbandAccount)
    TextView workOrderDataWbandAccount;
    @Bind(R.id.recyclerView_workorderoperationnote)
    RecyclerView recyclerViewWorkorderoperationnote;
    @Bind(R.id.lin_workorderoperationnote)
    LinearLayout lin_workorderoperationnote;
    @Bind(R.id.workorderdetails_scrollView)
    ScrollView workorderdetailsScrollView;
    @Bind(R.id.tv_null)
    TextView tvNull;
    @Bind(R.id.content)
    RefreshLinearLayout refreshLinearLayout;


    private String workOrderId = "";
    private JSONObject resultObject;
    private WorkOrderDetailsOperationLogListDataAdapter OperationLogAdapter;
    private ArrayList<WorkOrderDetailsOperationLogListDataEntity> arrayOperationLog;
    private WorkOrderDetailsOperationLogListDataEntity OperationLogEntity;
    private String strTips = "";//提示文字
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    refreshLinearLayout.updateStatus(RefreshLinearLayout.FAIL, strTips, true);
                    tvNull.setVisibility(View.VISIBLE);
                    workorderdetailsScrollView.setVisibility(View.GONE);
                    break;
                case 2:
                    refreshLinearLayout.updateStatus(RefreshLinearLayout.SUCCESS);
                    break;
                case 3:
                    refreshLinearLayout.updateStatus(RefreshLinearLayout.LOADING);
                    break;
                case 4:
                    showDetails();
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
        return R.layout.activity_workorderdetatils;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        getHeader().setTitle(R.string.workorderdetails);
        workOrderId = getIntent().getStringExtra("workOrderId");
        if(!TextUtils.isEmpty(workOrderId)){
            Message message = Message.obtain();
            message.what = 3;
            handler.sendMessage(message);
            getWorkOrderDetailData();
        }
    }

    @Override
    public void loadData() {

    }


    private void getWorkOrderDetailData() {
        UserHttpUtils.getWorkOrderDetailData(workOrderId,
                new RxUtils.DialogListener() {
                    @Override
                    public void onResult(@NotNull String result) {
                        try{
                            if(!TextUtils.isEmpty(result)){
                                resultObject = new JSONObject(result);
                                Log.v("result",result);
                                handler.sendEmptyMessageDelayed(2, 3000);
                                Message message = Message.obtain();
                                message.what = 4;
                                handler.sendMessage(message);
                            }
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

    private void showDetails() {
        try {
            //工单状态 0.未执行;1.已完成;2.执行失败;3.执行中;4.己作废;5.已取消
            if(resultObject.getString("workOrderStatus").equals("0")){
                workorderdetailsWorkOrderStatus.setText("未执行");
            }else if(resultObject.getString("workOrderStatus").equals("1")){
                workorderdetailsWorkOrderStatus.setText("已完成");
            }else if(resultObject.getString("workOrderStatus").equals("2")){
                workorderdetailsWorkOrderStatus.setText("执行失败");
            }else if(resultObject.getString("workOrderStatus").equals("3")){
                workorderdetailsWorkOrderStatus.setText("执行中");
            }else if(resultObject.getString("workOrderStatus").equals("4")){
                workorderdetailsWorkOrderStatus.setText("己作废");
            }else if(resultObject.getString("workOrderStatus").equals("5")){
                workorderdetailsWorkOrderStatus.setText("已取消");
            }

            String terminalInfoData = resultObject.getString("terminalInfoData");
            JSONObject JsonterminalInfoData = new JSONObject(terminalInfoData);

            workorderdetailsTerminalSN.setText(JsonterminalInfoData.getString("terminalSN"));
            workorderdetailsTerminalRegisterStatus.setText(JsonterminalInfoData.getString("terminalRegisterStatus"));
            workorderdetailsManufacturerName.setText(JsonterminalInfoData.getString("manufacturerName"));
            workorderdetailsTerminalModel.setText(JsonterminalInfoData.getString("terminalModel"));
            workorderdetailsSoftwareVersion.setText(JsonterminalInfoData.getString("softwareVersion"));
            workorderdetailsHardwareVersion.setText(JsonterminalInfoData.getString("hardwareVersion"));
            workorderdetailsIpAddress.setText(JsonterminalInfoData.getString("ipAddress"));
            workorderdetailsMacAddress.setText(JsonterminalInfoData.getString("macAddress"));
            workorderdetailsJoinNetDayTime.setText(JsonterminalInfoData.getString("joinNetDayTime"));
            workorderdetailsRecentConnectionDayTime.setText(JsonterminalInfoData.getString("recentConnectionDayTime"));

            String workOrderData = resultObject.getString("workOrderData");
            JSONObject JsonWorkOrderData = new JSONObject(workOrderData);

            workOrderDataWorkOrderNo.setText(JsonWorkOrderData.getString("workOrderNo"));
           // 1：新装 2：缴费开机 3： 欠费停机 4：客户申请停机 5： 客户申请复机 6：订户密码变更 7：修改速率信息 8：拆机 9:修改
            if(JsonWorkOrderData.getString("businessType").equals("1")){
                workOrderDataBusinessType.setText("新装");
            }else if(JsonWorkOrderData.getString("businessType").equals("2")){
                workOrderDataBusinessType.setText("缴费开机");
            }else if(JsonWorkOrderData.getString("businessType").equals("3")){
                workOrderDataBusinessType.setText("欠费停机");
            }else if(JsonWorkOrderData.getString("businessType").equals("4")){
                workOrderDataBusinessType.setText("客户申请停机");
            }else if(JsonWorkOrderData.getString("businessType").equals("5")){
                workOrderDataBusinessType.setText("客户申请复机");
            }else if(JsonWorkOrderData.getString("businessType").equals("6")){
                workOrderDataBusinessType.setText("订户密码变更");
            }else if(JsonWorkOrderData.getString("businessType").equals("7")){
                workOrderDataBusinessType.setText("修改速率信息");
            }else if(JsonWorkOrderData.getString("businessType").equals("8")){
                workOrderDataBusinessType.setText("拆机");
            }else if(JsonWorkOrderData.getString("businessType").equals("9")){
                workOrderDataBusinessType.setText("修改");
            }

            workOrderDataProvinceCode.setText(JsonWorkOrderData.getString("provinceCode"));
            workOrderDataBusinessCode.setText(JsonWorkOrderData.getString("businessCode"));
            workOrderDataLoid.setText(JsonWorkOrderData.getString("loid"));
            workOrderDataOpenWorkOrderDayTime.setText(JsonWorkOrderData.getString("openWorkOrderDayTime"));
            workOrderDataAttributionArea.setText(JsonWorkOrderData.getString("attributionArea"));
            workOrderDataWbandAccount.setText(JsonWorkOrderData.getString("wbandAccount"));

            String OperationLogStr = resultObject.getString("workOrderOperationLogListData");
            arrayOperationLog = JsonUtils.json2Obj(OperationLogStr, new TypeReference<ArrayList<WorkOrderDetailsOperationLogListDataEntity>>() {});
            if(arrayOperationLog != null && arrayOperationLog.size()>0){
                recyclerViewWorkorderoperationnote.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));;
                OperationLogAdapter = new WorkOrderDetailsOperationLogListDataAdapter(arrayOperationLog);
                OperationLogAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                        OperationLogEntity = arrayOperationLog.get(position);
                        openOperationNote(OperationLogEntity);
                    }

                });
                recyclerViewWorkorderoperationnote.setAdapter(OperationLogAdapter);
            }else{
                lin_workorderoperationnote.setVisibility(View.GONE);
                recyclerViewWorkorderoperationnote.setVisibility(View.GONE);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    //操作记录
    private void openOperationNote(WorkOrderDetailsOperationLogListDataEntity OperationLogEntity) {
        if(OperationLogEntity !=null){
            IntentUtils.goOperationNoteForWorkOrder(this,OperationLogEntity.operationLOID,OperationLogEntity.operationLogDayTime);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
