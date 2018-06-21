package com.cmcc.pp.ui.gatewayadministration;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;

import com.cmcc.pp.R;
import com.cmcc.pp.base.BActivity;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * Created by ABC on 2017/12/20.
 * 网关查询---更多信息
 */

public class MoreInformationForGateway extends BActivity {
    @Bind(R.id.tv_manufacturerName)
    TextView tvManufacturerName;
    @Bind(R.id.tv_deviceMode)
    TextView tvDeviceMode;
    @Bind(R.id.tv_oui)
    TextView tvOui;
    @Bind(R.id.tv_terminalSN)
    TextView tvTerminalSN;
    @Bind(R.id.tv_attributionArea)
    TextView tvAttributionArea;
    @Bind(R.id.tv_wbandAccount)
    TextView tvWbandAccount;
    @Bind(R.id.tv_terminalRegisterStatus)
    TextView tvTerminalRegisterStatus;
    @Bind(R.id.tv_terminalModel)
    TextView tvTerminalModel;
    @Bind(R.id.tv_firmwareVersion)
    TextView tvFirmwareVersion;
    @Bind(R.id.tv_hardwareVersion)
    TextView tvHardwareVersion;
    @Bind(R.id.tv_ipAddress)
    TextView tvIpAddress;
    @Bind(R.id.tv_macAddress)
    TextView tvMacAddress;
    @Bind(R.id.tv_joinNetDayTime)
    TextView tvJoinNetDayTime;
    @Bind(R.id.tv_recentConnectionDayTime)
    TextView tvRecentConnectionDayTime;
    @Bind(R.id.tv_gatewayToPlatformDigestAccount)
    TextView tvGatewayToPlatformDigestAccount;
    @Bind(R.id.tv_gatewayToPlatformDigestPassword)
    TextView tvGatewayToPlatformDigestPassword;
    @Bind(R.id.tv_platformToGatewayAccount)
    TextView tvPlatformToGatewayAccount;
    @Bind(R.id.tv_platformToGatewayPassword)
    TextView tvPlatformToGatewayPassword;
    @Bind(R.id.tv_deviceMaintenanceAccount)
    TextView tvDeviceMaintenanceAccount;
    @Bind(R.id.tv_deviceMaintenancePassword)
    TextView tvDeviceMaintenancePassword;
    @Bind(R.id.tv_passwordCertification)
    TextView tvPasswordCertification;
    @Bind(R.id.tv_inversionLinkUrl)
    TextView tvInversionLinkUrl;
    @Bind(R.id.tv_jvmVersion)
    TextView tvJvmVersion;
    @Bind(R.id.tv_osgiVersion)
    TextView tvOsgiVersion;
    @Bind(R.id.tv_flow)
    TextView tvFlow;
    @Bind(R.id.tv_lanQuantity)
    TextView tvLanQuantity;
    @Bind(R.id.tv_usbQuantity)
    TextView tvUsbQuantity;
    @Bind(R.id.tv_Ipv4v6Capacity)
    TextView tvIpv4v6Capacity;
    @Bind(R.id.tv_wifiCapacity)
    TextView tvWifiCapacity;
    @Bind(R.id.tv_wifiAntennaLocation)
    TextView tvWifiAntennaLocation;
    @Bind(R.id.tv_wifiAntennaQuantity)
    TextView tvWifiAntennaQuantity;
    @Bind(R.id.tv_wifiAntennaSize)
    TextView tvWifiAntennaSize;
    @Bind(R.id.tv_wifi24hzCapacity)
    TextView tvWifi24hzCapacity;
    @Bind(R.id.tv_wifi58ghzCapacity)
    TextView tvWifi58ghzCapacity;
    @Bind(R.id.tv_gatewayName)
    TextView tvGatewayName;

    private String gatewayInfoDataStr = "";
    private JSONObject gatewayInfoData;

    @Override
    protected boolean getHasTitle() {
        return true;
    }

    @Override
    public int getContentLayoutId() {
        return R.layout.activity_moreinformationforgateway;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        getHeader().setTitle(R.string.str_moreinformation);
        gatewayInfoDataStr = getIntent().getStringExtra("gatewayInfoDataStr");
        initData();
    }

    private void initData() {
        try {
            gatewayInfoData = new JSONObject(gatewayInfoDataStr);

            if(!TextUtils.isEmpty(gatewayInfoData.getString("manufacturerName"))){
                tvManufacturerName.setText(gatewayInfoData.getString("manufacturerName"));
            }

            if(!TextUtils.isEmpty(gatewayInfoData.getString("deviceMode"))){
                tvDeviceMode.setText(gatewayInfoData.getString("deviceMode"));
            }

            if(!TextUtils.isEmpty(gatewayInfoData.getString("oui"))){
                tvOui.setText(gatewayInfoData.getString("oui"));
            }

            if(!TextUtils.isEmpty(gatewayInfoData.getString("terminalSN"))){
                tvTerminalSN.setText(gatewayInfoData.getString("terminalSN"));
            }

            if(!TextUtils.isEmpty(gatewayInfoData.getString("attributionArea"))){
                tvAttributionArea.setText(gatewayInfoData.getString("attributionArea"));
            }

            if(!TextUtils.isEmpty(gatewayInfoData.getString("wbandAccount"))){
                tvWbandAccount.setText(gatewayInfoData.getString("wbandAccount"));
            }

            if(!TextUtils.isEmpty(gatewayInfoData.getString("terminalRegisterStatus"))){
                tvTerminalRegisterStatus.setText(gatewayInfoData.getString("terminalRegisterStatus"));
            }

            if(!TextUtils.isEmpty(gatewayInfoData.getString("terminalModel"))){
                tvTerminalModel.setText(gatewayInfoData.getString("terminalModel"));
            }

            if(!TextUtils.isEmpty(gatewayInfoData.getString("firmwareVersion"))){
                tvFirmwareVersion.setText(gatewayInfoData.getString("firmwareVersion"));
            }

            if(!TextUtils.isEmpty(gatewayInfoData.getString("hardwareVersion"))){
                tvHardwareVersion.setText(gatewayInfoData.getString("hardwareVersion"));
            }

            if(!TextUtils.isEmpty(gatewayInfoData.getString("ipAddress"))){
                tvIpAddress.setText(gatewayInfoData.getString("ipAddress"));
            }

            if(!TextUtils.isEmpty(gatewayInfoData.getString("macAddress"))){
                tvMacAddress.setText(gatewayInfoData.getString("macAddress"));
            }

            if(!TextUtils.isEmpty(gatewayInfoData.getString("joinNetDayTime"))){
                tvJoinNetDayTime.setText(gatewayInfoData.getString("joinNetDayTime"));
            }

            if(!TextUtils.isEmpty(gatewayInfoData.getString("recentConnectionDayTime"))){
                tvRecentConnectionDayTime.setText(gatewayInfoData.getString("recentConnectionDayTime"));
            }

            if(!TextUtils.isEmpty(gatewayInfoData.getString("gatewayToPlatformDigestAccount"))){
                tvGatewayToPlatformDigestAccount.setText(gatewayInfoData.getString("gatewayToPlatformDigestAccount"));
            }

            if(!TextUtils.isEmpty(gatewayInfoData.getString("gatewayToPlatformDigestPassword"))){
                tvGatewayToPlatformDigestPassword.setText(gatewayInfoData.getString("gatewayToPlatformDigestPassword"));
            }

            if(!TextUtils.isEmpty(gatewayInfoData.getString("platformToGatewayAccount"))){
                tvPlatformToGatewayAccount.setText(gatewayInfoData.getString("platformToGatewayAccount"));
            }

            if(!TextUtils.isEmpty(gatewayInfoData.getString("platformToGatewayPassword"))){
                tvPlatformToGatewayPassword.setText(gatewayInfoData.getString("platformToGatewayPassword"));
            }

            if(!TextUtils.isEmpty(gatewayInfoData.getString("deviceMaintenanceAccount"))){
                tvDeviceMaintenanceAccount.setText(gatewayInfoData.getString("deviceMaintenanceAccount"));
            }

            if(!TextUtils.isEmpty(gatewayInfoData.getString("deviceMaintenancePassword"))){
                tvDeviceMaintenancePassword.setText(gatewayInfoData.getString("deviceMaintenancePassword"));
            }

            if(!TextUtils.isEmpty(gatewayInfoData.getString("passwordCertification"))){
                tvPasswordCertification.setText(gatewayInfoData.getString("passwordCertification"));
            }

            if(!TextUtils.isEmpty(gatewayInfoData.getString("inversionLinkUrl"))){
                tvInversionLinkUrl.setText(gatewayInfoData.getString("inversionLinkUrl"));
            }

            if(!TextUtils.isEmpty(gatewayInfoData.getString("jvmVersion"))){
                tvJvmVersion.setText(gatewayInfoData.getString("jvmVersion"));
            }

            if(!TextUtils.isEmpty(gatewayInfoData.getString("osgiVersion"))){
                tvOsgiVersion.setText(gatewayInfoData.getString("osgiVersion"));
            }

            if(!TextUtils.isEmpty(gatewayInfoData.getString("lanQuantity"))){
                tvLanQuantity.setText(gatewayInfoData.getString("lanQuantity"));
            }

            if(!TextUtils.isEmpty(gatewayInfoData.getString("usbQuantity"))){
                tvUsbQuantity.setText(gatewayInfoData.getString("usbQuantity"));
            }

            if(!TextUtils.isEmpty(gatewayInfoData.getString("ipv4v6Capacity"))){
                tvIpv4v6Capacity.setText(gatewayInfoData.getString("ipv4v6Capacity"));
            }

            if(!TextUtils.isEmpty(gatewayInfoData.getString("wifiCapacity"))){
                tvWifiCapacity.setText(gatewayInfoData.getString("wifiCapacity"));
            }

            if(!TextUtils.isEmpty(gatewayInfoData.getString("wifiAntennaLocation"))){
                tvWifiAntennaLocation.setText(gatewayInfoData.getString("wifiAntennaLocation"));
            }

            if(!TextUtils.isEmpty(gatewayInfoData.getString("wifiAntennaQuantity"))){
                tvWifiAntennaQuantity.setText(gatewayInfoData.getString("wifiAntennaQuantity"));
            }

            if(!TextUtils.isEmpty(gatewayInfoData.getString("wifiAntennaSize"))){
                tvWifiAntennaSize.setText(gatewayInfoData.getString("wifiAntennaSize"));
            }

            if(!TextUtils.isEmpty(gatewayInfoData.getString("wifi24hzCapacity"))){
                tvWifi24hzCapacity.setText(gatewayInfoData.getString("wifi24hzCapacity"));
            }

            if(!TextUtils.isEmpty(gatewayInfoData.getString("wifi58ghzCapacity"))){
                tvWifi58ghzCapacity.setText(gatewayInfoData.getString("wifi58ghzCapacity"));
            }

            if(!TextUtils.isEmpty(gatewayInfoData.getString("gatewayName"))){
                tvGatewayName.setText(gatewayInfoData.getString("gatewayName"));
            }



        } catch (JSONException e) {
            e.printStackTrace();
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
