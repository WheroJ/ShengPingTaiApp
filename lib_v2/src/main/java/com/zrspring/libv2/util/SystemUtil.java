package com.zrspring.libv2.util;

/*   
 * Copyright (c) 2014-2014 China.ChongQing.MaiQuan Ltd All Rights Reserved.   
 *   
 * This software is the confidential and proprietary information of   
 * Founder. You shall not disclose such Confidential Information   
 * and shall use it only in accordance with the terms of the agreements   
 * you entered into with Founder.   
 *   
 */

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo.State;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;

import java.io.File;
import java.util.List;

/**
 * @author QQ:346148849 loveanao@163.com
 * @ClassName: SystemUtil
 * @Description: 系统帮助工具 可以获取App的各种信息，包名，图标，当前软件版本，IMEI 判断当前系统网络 SD卡状态等
 * @date 2014-5-19 上午11:13:59
 * @ConputerUserName zrspring version=1.0
 */

public class SystemUtil {

    public static State wifiState = null;
    public static State mobileState = null;

    public static PackageInfo packageInfo = null;
    public static ApplicationInfo applicationInfo = null;
    public static TelephonyManager mTelephonyManager = null;

    /**
     * @author zrspring 网络状态枚举
     */
    public enum NetWorkState {
        WIFI, MOBILE, NONE
    }

    /**
     * 用来判断服务是否运行.
     *
     * @param mContext
     * @param className 判断的服务名字
     * @return true 在运行 false 不在运行
     */
    public static boolean isServiceRunning(Context mContext, String className) {
        boolean isRunning = false;
        ActivityManager activityManager = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> serviceList = activityManager.getRunningServices(30);
        if (!(serviceList.size() > 0)) {
            return false;
        }
        for (int i = 0; i < serviceList.size(); i++) {
            if (serviceList.get(i).service.getClassName().equals(className) == true) {
                isRunning = true;
                break;
            }
        }
        return isRunning;
    }

    /**
     * 功能:获取PackageManager对象
     *
     * @param context
     * @return
     */
    private static PackageManager getPackageManager(Context context) {
        if (context == null) {
        }
        PackageManager p = context.getApplicationContext().getPackageManager();
        if (p == null) {
        }
        return p;
    }

    /**
     * 功能:获取PackageInfo对象
     *
     * @param context
     * @return
     */
    public static PackageInfo getPackageInfo(Context context) {
        if (packageInfo == null) {
            try {
                packageInfo = getPackageManager(context).getPackageInfo(context.getPackageName(), 0);
            } catch (PackageManager.NameNotFoundException e) {
                packageInfo = null;
            }
        }
        return packageInfo;
    }

    /**
     * 功能:获取ApplicationInfo对象
     *
     * @param context
     * @return mTelephonyManager
     */
    private static ApplicationInfo getApplicationInfo(Context context) {
        if (applicationInfo == null) {
            try {
                applicationInfo = getPackageManager(context)
                        .getApplicationInfo(context.getPackageName(), 0);
            } catch (PackageManager.NameNotFoundException e) {
                applicationInfo = null;
            }
        }
        return applicationInfo;
    }

    private static TelephonyManager getTelephonyManager(Context context) {
        if (mTelephonyManager == null) {
            try {
                mTelephonyManager = (TelephonyManager) context
                        .getSystemService(Context.TELEPHONY_SERVICE);
            } catch (Exception e) {
                mTelephonyManager = null;
            }
        }
        return mTelephonyManager;
    }

    /**
     * 功能:获取App程序名
     *
     * @param context
     * @return
     */
    public static String getAppName(Context context) {
        return getPackageManager(context).getApplicationLabel(
                getApplicationInfo(context)).toString();
    }

    /**
     * 功能:获取App包名
     *
     * @param context
     * @return
     */
    public static String getPackageName(Context context) {
        return getPackageInfo(context).packageName;
    }

    /**
     * 功能:获取版本名字
     *
     * @param context
     * @return
     */
    public static String getVersionName(Context context) {
        return getPackageInfo(context).versionName;
    }

    /**
     * 功能:获取程序版本
     *
     * @param context
     * @return
     */
    public static int getVersionCode(Context context) {
        return getPackageInfo(context).versionCode;
    }

    /**
     * 获取设备IMEI号码
     *
     * @param context
     * @return IMEI号
     */
    public static String getIMEI(Context context) {
        return getTelephonyManager(context).getDeviceId();
    }

    /**
     * 获取设备ROM版本号
     *
     * @param context
     * @return ROM版本号
     */
    public static String getPhoneRomVersion(Context context) {
        return android.os.Build.VERSION.RELEASE;
    }

    /**
     * 获取本机的手机号码
     *
     * @param context
     * @return 手机号
     */
    public static String getPhoneNumber(Context context) {
        return getTelephonyManager(context).getLine1Number();
    }

    /**
     * 功能:获取App程序图标
     *
     * @param context
     * @return
     */
    public static Drawable getAppIcon(Context context) {
        return getApplicationInfo(context).loadIcon(getPackageManager(context));
    }

    /**
     * 功能:获取程序的签名
     *
     * @param context
     * @return
     */
    public static String getAppSignature(Context context) {
        return getPackageInfo(context).signatures[0].toCharsString();
    }

    /**
     * 功能:判断当前系统是否插入SD卡
     *
     * @return true：存在SD卡 false：不存在SD卡
     */
    public static boolean isSdcardExist() {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            return true;
        }
        return false;
    }

    /**
     * 功能：获取SD卡的路径
     *
     * @return SD卡的绝对路径
     */
    public static String getSDPath() {
        File sdDir = null;
        if (isSdcardExist()) {
            sdDir = Environment.getExternalStorageDirectory();// 获取跟目录
        }
        return sdDir.toString();
    }

    /**
     * 获取当前的网络状态
     *
     * @return NetWorkState
     */
    public static NetWorkState getConnectState(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        manager.getActiveNetworkInfo();
        wifiState = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
                .getState();
        mobileState = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
                .getState();
        if (wifiState != null && mobileState != null
                && State.CONNECTED != wifiState
                && State.CONNECTED == mobileState) {
            return NetWorkState.MOBILE;
        } else if (wifiState != null && mobileState != null
                && State.CONNECTED != wifiState
                && State.CONNECTED != mobileState) {
            return NetWorkState.NONE;
        } else if (wifiState != null && State.CONNECTED == wifiState) {
            return NetWorkState.WIFI;
        }
        return NetWorkState.NONE;
    }

    /**
     * @MethodthName: requestCallPhonePermission
     * @Description: 针对Android 6.0 系统,请求拨打电话权限
     * @params: [] reqPermissionCode 权限请求代码,
     * @Return:void
     * @Throw:
     */
    public static void makeCall(Activity activity, String callNumber) {
        if (ActivityCompat.checkSelfPermission(activity,
                Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
            Intent intent = new Intent(Intent.ACTION_CALL);
            Uri data = Uri.parse("tel:" + callNumber);
            intent.setData(data);
            activity.startActivity(intent);
        }
    }

    /**
     * 功能：获取SD卡的路径
     *
     * @return SD卡的绝对路径
     */
    public static String getSDAbsolutePath() {
        String path = "";
        if (isSdcardExist()) {
            path = Environment.getExternalStorageDirectory().getAbsolutePath();// 获取跟目录
        }
        return path;
    }

}
