package com.zrspring.libv2.util;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;

import com.zrspring.libv2.R;

/**
 * Version: 1.0
 * Author: rui
 * CreatDate: 2016年09月22日下午1:13 13
 * Encoding:UTF-8
 * Description:
 */
public class PermissionHelper {

    public static final String PLEASE_OPEN_CAMERA_PERMISSION = "please open yesir camera and r/w storage permission";
    public static final String PLEASE_OPEN_CALL_PERMISSION = "please open yesir call permission";
    public static final String PLEASE_OPEN_READ_STORAGE_CARD_PERMISSION = "please open yesir r/w storage card permission ";
    public static final String PLEASE_OPEN_LOCATION_PERMISSION = "please open location permission ";
    private static final String PACKAGE_URL_SCHEME = "package:";

    public static final int MY_PERMISSIONS_REQUEST_MAKECALL = 101;
    public static final int MY_PERMISSIONS_REQUEST_CAMERA = 102;
    public static final int MY_PERMISSIONS_REQUEST_SDCARD = 103;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 104;
    public static final int MY_PERMISSIONS_READ_CONTACTS = 105;

    private static PermissionHelper sInstance = null;

    public static synchronized PermissionHelper getInstance() {
        if (sInstance == null) {
            sInstance = new PermissionHelper();
        }
        return sInstance;
    }

    private Dialog permissionNeedDialog;

    private PermissionResultListener _listener;

    public interface PermissionResultListener {
        void onGranted();

        void onDeclined();
    }

    //=================================
    // 操作
    //=================================

    public void makePhoneCall(Activity mActivity, PermissionResultListener listener) {
        this._listener = listener;
        if (ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            //权限不通过，重新请求
            requestSinglePermission(mActivity, Manifest.permission.CALL_PHONE, MY_PERMISSIONS_REQUEST_MAKECALL);
        } else {
            this._listener.onGranted();
        }
    }


    public void  openContacts(Activity mActivity, PermissionResultListener listener){
        this._listener = listener;
        if (ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            //权限不通过，重新请求
            requestSinglePermission(mActivity,
                    Manifest.permission.READ_CONTACTS, MY_PERMISSIONS_READ_CONTACTS);
        } else {
            this._listener.onGranted();//同意
        }

    }
    public void openCamera(Activity mActivity, PermissionResultListener listener) {
        this._listener = listener;
        if (ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            //只要其中一个权限不通过，重新请求
            requestCameraPermission(mActivity, MY_PERMISSIONS_REQUEST_CAMERA);
        } else {
            this._listener.onGranted();//同意
        }
    }

    public void loadSdCard(Activity mActivity, PermissionResultListener listener) {
        this._listener = listener;
        if (ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            //权限不通过，重新请求
            requestSinglePermission(mActivity,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE, MY_PERMISSIONS_REQUEST_SDCARD);
        } else {
            this._listener.onGranted();//同意
        }
    }


    public void openGPS(Activity mActivity, PermissionResultListener listener) {
        this._listener = listener;
        if (checkPermissionPass(mActivity, Manifest.permission.ACCESS_COARSE_LOCATION)
                && checkPermissionPass(mActivity, Manifest.permission.ACCESS_FINE_LOCATION)
                && checkPermissionPass(mActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                && checkPermissionPass(mActivity, Manifest.permission.READ_EXTERNAL_STORAGE)
                && checkPermissionPass(mActivity, Manifest.permission.READ_PHONE_STATE)) {
            thePermissionGranted();
        } else {
            requestGPSPermission(mActivity, MY_PERMISSIONS_REQUEST_LOCATION);
        }
    }


    private boolean checkPermissionPass(Activity mActivity, String permissionType) {
        boolean result = false;
        int resultStatus = ActivityCompat.checkSelfPermission(mActivity, permissionType);
        if (resultStatus == PackageManager.PERMISSION_GRANTED) {
            result = true;
        }
        return result;
    }

    //=================================
    // 请求权限
    //=================================

    private void requestSinglePermission(Activity mActivity, String permissiontype, int myReqType) {
        ActivityCompat.requestPermissions(mActivity, new String[]{permissiontype}, myReqType);
    }

    private void requestCameraPermission(Activity mActivity, int myReqType) {
        ActivityCompat.requestPermissions(
                mActivity,
                new String[]{
                        Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                },
                myReqType);
    }

    private void requestGPSPermission(Activity mActivity, int myReqType) {
        ActivityCompat.requestPermissions(
                mActivity,
                new String[]{
//                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
//                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.READ_PHONE_STATE
                },
                myReqType);
    }


    //=================================
    // 请求权限解析,新的
    //=================================

    public void onPermissionResult(Activity mActivity, int requestCode, String permissions[], int[] grantResults) {
        //是请求通话的
        if (requestCode == MY_PERMISSIONS_REQUEST_MAKECALL) {
            if (grantResults.length > 0 ) {
                boolean callPass = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                if(callPass){
                    thePermissionGranted();
                }else {
                    thePermissionDeclined();
                    if(!ActivityCompat.shouldShowRequestPermissionRationale(mActivity, Manifest.permission.CALL_PHONE)){
                        String message = mActivity.getResources().getString(R.string.per_please_open_call);
                        openPermissionNeedDialog(mActivity, message);
                    }
                }
            } else {
                thePermissionDeclined();
            }

        }else if(requestCode ==MY_PERMISSIONS_READ_CONTACTS){
            if (grantResults.length > 0 ) {
                boolean contactsPass = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                if(contactsPass){
                    thePermissionGranted();
                }else {
                    thePermissionDeclined();
                    if(!ActivityCompat.shouldShowRequestPermissionRationale(mActivity, Manifest.permission.READ_CONTACTS)){
                        String message = mActivity.getResources().getString(R.string.per_please_open_contacts);
                        openPermissionNeedDialog(mActivity, message);
                    }
                }
            } else {
                thePermissionDeclined();
            }

        } else if (requestCode == MY_PERMISSIONS_REQUEST_CAMERA) {   //请求照相

            // 0是摄像机权限，1是读取SD卡权限
            if (grantResults.length > 0 ) {
                boolean cameraPass = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                boolean writeSDCardPass = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                if(cameraPass && writeSDCardPass){
                    thePermissionGranted();
                }else{
                    thePermissionDeclined();
                    if(!ActivityCompat.shouldShowRequestPermissionRationale(mActivity, Manifest.permission.CAMERA)){
                        String message = mActivity.getResources().getString(R.string.per_please_open_camera);
                        openPermissionNeedDialog(mActivity, message);
                    }
                    if (!ActivityCompat.shouldShowRequestPermissionRationale(
                            mActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                        String message = mActivity.getResources().getString(R.string.per_please_open_sdcard);
                        openPermissionNeedDialog(mActivity, message);
                    }
                }
            } else {
                thePermissionDeclined();
            }

            //请求读取SD卡
        } else if (requestCode == MY_PERMISSIONS_REQUEST_SDCARD) {
            if (grantResults.length > 0) {
                boolean writeSDPass = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                if (writeSDPass) {
                    thePermissionGranted();
                } else {
                    thePermissionDeclined();
                    if (!ActivityCompat.shouldShowRequestPermissionRationale(mActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                        String message = mActivity.getResources().getString(R.string.per_please_open_sdcard);
                        openPermissionNeedDialog(mActivity, message);
                    }
                }
            }else{
                thePermissionDeclined();
            }
        } else if (requestCode == MY_PERMISSIONS_REQUEST_LOCATION) {
            if (grantResults.length > 0) {

              // boolean coarseLocationPass = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                boolean fineLocationPass = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                boolean writeSDPass = grantResults[1] == PackageManager.PERMISSION_GRANTED;
              //  boolean readSDPass = grantResults[3] == PackageManager.PERMISSION_GRANTED;
                boolean readPhonePass = grantResults[2] == PackageManager.PERMISSION_GRANTED;

                LogUtil.infoMessage("PermissionHelper",
                        "\n fineLocationPass = " + fineLocationPass
                                + "\n writeSDPass = " + writeSDPass
                                + "\n readPhonePass = " + readPhonePass);

                if (fineLocationPass && writeSDPass && readPhonePass) {
                    thePermissionGranted();
                } else {
                    thePermissionDeclined();
                    // 用户拒绝,并设置不再询问后,提示消息,并跳转设置界面
                    // 拒绝             shouldShowRequestPermissionRationale  返回 true
                    // 拒绝 + 不在询问   shouldShowRequestPermissionRationale   返回 false
//
//                    boolean result = ActivityCompat.shouldShowRequestPermissionRationale(
//                            mActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
//                    LogUtil.infoMessage("PermissionHelper", "Declined Result = " + result);
//                    LogUtil.infoMessage("PermissionHelper", "" +
//                            "fineLocationPass shouldShowRequestPermissionRationale = "
//                            + ActivityCompat.shouldShowRequestPermissionRationale(
//                            mActivity, Manifest.permission.ACCESS_FINE_LOCATION) +
//                            "\nwriteSDPass shouldShowRequestPermissionRationale = "
//                            + ActivityCompat.shouldShowRequestPermissionRationale(
//                            mActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE)+
//                            "\nreadPhonePass shouldShowRequestPermissionRationale = "
//                                    + ActivityCompat.shouldShowRequestPermissionRationale(
//                                    mActivity, Manifest.permission.READ_PHONE_STATE));

                    if (!ActivityCompat.shouldShowRequestPermissionRationale(
                            mActivity, Manifest.permission.ACCESS_FINE_LOCATION)) {
                        String message = mActivity.getResources().getString(R.string.per_please_open_location);
                        openPermissionNeedDialog(mActivity, message);
                    }

                    if (!ActivityCompat.shouldShowRequestPermissionRationale(
                            mActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                        String message = mActivity.getResources().getString(R.string.per_please_open_sdcard);
                        openPermissionNeedDialog(mActivity, message);
                    }

                    if (!ActivityCompat.shouldShowRequestPermissionRationale(
                            mActivity, Manifest.permission.READ_PHONE_STATE)) {
                        String message = mActivity.getResources().getString(R.string.per_please_open_phone_state);
                        openPermissionNeedDialog(mActivity, message);
                    }

                }
            }else {
                thePermissionDeclined();
            }
        }
    }

    private void thePermissionGranted() {
        if (this._listener != null) {
            this._listener.onGranted();
        }
    }

    private void thePermissionDeclined() {
        if (this._listener != null) {
            this._listener.onDeclined();
        }
    }

    private void openPermissionNeedDialog(final Activity activity, String message) {
        if (permissionNeedDialog == null) {
            permissionNeedDialog = new AlertDialog.Builder(activity)
                    .setMessage(message)
                    .setNegativeButton(" OK ", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            closePermissionNeedDialog();
                            openPermissionSetting(activity);
                        }
                    }).create();
        }
        permissionNeedDialog.setCancelable(true);
        permissionNeedDialog.show();
    }

    private void closePermissionNeedDialog() {
        if (permissionNeedDialog != null) {
            permissionNeedDialog.dismiss();
            permissionNeedDialog = null;
        }
    }

    //=================================
    // 打开设置界面
    //=================================
    private void openPermissionSetting(Activity mActivity) {
        try {
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            intent.setData(Uri.parse(PACKAGE_URL_SCHEME + mActivity.getPackageName()));
            mActivity.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //=================================
    // 清空
    //=================================
    public void clean() {
        sInstance = null;
        _listener = null;
    }
}
