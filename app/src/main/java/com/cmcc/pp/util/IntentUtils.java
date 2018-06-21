package com.cmcc.pp.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.cmcc.pp.base.BActivity;
import com.cmcc.pp.base.BFragment;
import com.cmcc.pp.entity.NetworkElement;
import com.cmcc.pp.entity.gateway.GatewayInfo;
import com.cmcc.pp.entity.gateway.WarningMessage;
import com.cmcc.pp.entity.platformmonitoring.DialingDetailEntity;
import com.cmcc.pp.entity.transactiondata.AreaItem;
import com.cmcc.pp.ui.BackService;
import com.cmcc.pp.ui.LoginActivity;
import com.cmcc.pp.ui.MainActivity;
import com.cmcc.pp.ui.gatewayadministration.GatewayDetailsActivity;
import com.cmcc.pp.ui.gatewayadministration.LineDiagnosisActivity;
import com.cmcc.pp.ui.gatewayadministration.MoreInformationForGateway;
import com.cmcc.pp.ui.gatewayadministration.PingDiagnosisActivity;
import com.cmcc.pp.ui.gatewayadministration.QueryResultsForGateway;
import com.cmcc.pp.ui.gatewayadministration.TracerouteDiagnosisActivity;
import com.cmcc.pp.ui.gatewayadministration.WarningMessageActivity;
import com.cmcc.pp.ui.platformmonitoring.ChoiceNetElementActivity;
import com.cmcc.pp.ui.platformmonitoring.ResultForServiceDialingActivity;
import com.cmcc.pp.ui.platformmonitoring.StepDetailsActivity;
import com.cmcc.pp.ui.workorder.OperationNoteForWorkOrder;
import com.cmcc.pp.ui.platformmonitoring.ServerDetailsActivity;
import com.cmcc.pp.ui.platformmonitoring.ServerlistActivity;
import com.cmcc.pp.ui.transactiondata.ChooseAreaActivity;
import com.cmcc.pp.ui.workorder.QueryResultsForWorkOrder;
import com.cmcc.pp.ui.workorder.SettingParameterValuesActivity;
import com.cmcc.pp.ui.workorder.WorkOrderDetailsActivity;
import com.zrspring.libv2.util.LogPrinter;

import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by ABC on 2018/1/17.
 */

public class IntentUtils {
    public static void goMain(Activity context){
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
        context.finish();
    }

    public static void goLogout(Activity context){
        Intent intent = new Intent(context,LoginActivity.class);
        context.startActivity(intent);
        TokenUtils.INSTANCE.setAllUserLogout();
        stopServcie(context);
    }


    public static void goQueryResults(Activity context, GatewayInfo gatewayInfo,String userAccount){
        Intent intent = new Intent(context,QueryResultsForGateway.class);
        intent.putExtra("gatewayInfo",gatewayInfo);
        intent.putExtra("userAccount",userAccount);
        context.startActivity(intent);
    }

    public static void goGatewayDetails(Activity context,String gatewayId){
        Intent intent = new Intent(context,GatewayDetailsActivity.class);
        intent.putExtra("gatewayId",gatewayId);
        context.startActivity(intent);
    }

    public static void goResultForServiceDialing(Activity context){
        Intent intent = new Intent(context,ResultForServiceDialingActivity.class);
        context.startActivity(intent);
    }

    public static void goMoreInformationForGateway(Activity context, String gatewayInfoDataStr){
        Intent intent = new Intent(context,MoreInformationForGateway.class);
        intent.putExtra("gatewayInfoDataStr", gatewayInfoDataStr);
        context.startActivity(intent);
    }

    public static void goWarningMessage(Activity context, ArrayList<WarningMessage> alarmInfoData){
        Intent intent = new Intent(context,WarningMessageActivity.class);
        intent.putExtra("alarmInfoData",  (Serializable) alarmInfoData);
        context.startActivity(intent);
    }

    public static void goLineDiagnosis(Activity context, String gatewayId){
        Intent intent = new Intent(context,LineDiagnosisActivity.class);
        intent.putExtra("gatewayId",gatewayId);
        context.startActivity(intent);
    }

    public static void goTracerouteDiagnosis(Activity context, String gatewayId){
        Intent intent = new Intent(context,TracerouteDiagnosisActivity.class);
        intent.putExtra("gatewayId",gatewayId);
        context.startActivity(intent);
    }

    public static void goPingDiagnosis(Activity context, String gatewayId){
        Intent intent = new Intent(context,PingDiagnosisActivity.class);
        intent.putExtra("gatewayId",gatewayId);
        context.startActivity(intent);
    }


    public static void goQueryResultsForWorkOrder(Activity context, String userAccount){
        Intent intent = new Intent(context,QueryResultsForWorkOrder.class);
        intent.putExtra("userAccount",userAccount);
        context.startActivity(intent);
    }

    public static void goWorkOrderDetails(Activity context, String workOrderId){
        Intent intent = new Intent(context,WorkOrderDetailsActivity.class);
        intent.putExtra("workOrderId",workOrderId);
        context.startActivity(intent);
    }

    public static void goSettingParameterValuesActivity(Activity context, String operationLogDetailId){
        Intent intent = new Intent(context,SettingParameterValuesActivity.class);
        intent.putExtra("operationLogDetailId",operationLogDetailId);
        context.startActivity(intent);
    }

    public static void goOperationNoteForWorkOrder(Activity context, String operationLOID,String operationLogDayTime){
        Intent intent = new Intent(context,OperationNoteForWorkOrder.class);
        intent.putExtra("operationLOID",operationLOID);
        intent.putExtra("operationLogDayTime",operationLogDayTime);
        context.startActivity(intent);
    }

    public static void goStepDetailsActivity(Activity context, DialingDetailEntity dialingDetailEntity){
        Intent intent = new Intent(context,StepDetailsActivity.class);
        intent.putExtra("dialingDetailEntity",dialingDetailEntity);
        context.startActivity(intent);
    }


    /**
     * 选择区域
     * @param nowAreaItem
     * @param getArea
     */
    public static void goChooseArea(BFragment fragment, AreaItem nowAreaItem, int getArea) {
        Intent intent = new Intent(fragment.getContext(), ChooseAreaActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("nowAreaItem", nowAreaItem);
        intent.putExtras(bundle);
        fragment.startActivityForResult(intent, getArea);
    }

    public static void goReLogin(@Nullable Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
        TokenUtils.INSTANCE.setAllUserLogout();
        stopServcie(context);
    }

    /**
     * 退出整个应用
     * @param activity
     */
    public static void goExit(BActivity activity) {
        stopServcie(activity);
        activity.finish();
        System.exit(0);
    }

    public static void stopServcie(Context context) {
        Intent intent = new Intent(context, BackService.class);
        context.stopService(intent);
        LogPrinter.i("StopService", "服务被停止了=======");
    }

    /**
     * 开启token判断过期机制的service
     */
    public static void startReLoginService() {
        Context context = UIUtils.getContext();
        Intent intent = new Intent(context, BackService.class);
        context.startService(intent);
        LogPrinter.i("StartService", "服务被开启了=======");
    }

    public static void openServerlist(Context context, NetworkElement checkNE) {
        Intent serverIntent = new Intent(context, ServerlistActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("checkNE", checkNE);
        serverIntent.putExtras(bundle);
        context.startActivity(serverIntent);
    }

    /**
     * 打开服务器详情
     * @param context
     * @param ip
     */
    public static void openServerDetails(Context context, String ip) {
        Intent intent = new Intent(context, ServerDetailsActivity.class);
        intent.putExtra("ip", ip);
        context.startActivity(intent);
    }

    /**
     * 选择网元
     * @param context
     * @param checkNE
     * @param requestCode
     */
    public static void openChoiceNet(Activity context, NetworkElement checkNE, int requestCode) {
        Intent intent = new Intent(context, ChoiceNetElementActivity.class);
        intent.putExtra("nowNEItem", checkNE);
        context.startActivityForResult(intent, requestCode);
    }
}
