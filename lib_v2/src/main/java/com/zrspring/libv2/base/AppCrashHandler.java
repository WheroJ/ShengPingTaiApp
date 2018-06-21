/*
 * Copyright (c) 2014-2014 China.ChongQing.MaiQuan Ltd All Rights Reserved.
 *
 * This software is the confidential and proprietary information of
 * Founder. You shall not disclose such Confidential Information
 * and shall use it only in accordance with the terms of the agreements
 * you entered into with Founder.
 *
 */
package com.zrspring.libv2.base;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.zrspring.libv2.util.FileUtils;
import com.zrspring.libv2.util.LogUtil;
import com.zrspring.libv2.util.SystemUtil;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 请填写作者名
 * @ClassName: CrashHandler
 * @Description: 捕捉程序崩溃，并提交崩溃数据到服务器
 * @date 2014-5-19 上午10:00:02
 * @ConputerUserName zrspring
 */

public class AppCrashHandler implements UncaughtExceptionHandler {

    /**
     * @Fields RESTART_TIME : 崩溃后，重新启动应用的延迟时间 3s
     */
    private final static int RESTART_TIME = 1500;
    private static final String TAG = "CRASHHANDLER";
    private UncaughtExceptionHandler mDefaultHandler;
    private static AppCrashHandler INSTANCE = new AppCrashHandler();
    private Context mContext;
    private Map<String, String> info = null;

    /**
     * Title: Description: 空构造器
     */
    private AppCrashHandler() {

    }

    /**
     * @return
     * @throws
     * @Title: getInstance
     * @Description: 获取CrashHandler单例
     */
    public static AppCrashHandler getInstance() {
        return INSTANCE;
    }

    /**
     * @param context
     * @throws
     * @Title: init
     * @Description: 初始化 CrashHandler
     */
    public void init(Context context) {
        mContext = context;
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
    }


    /*
     * 当UncaughtException发生时会转入该重写的方法来处理 如果自定义错误处理成功，那么结束程序，准备重新启动app 抓取异常 重启软件
     * 有网络上传到网络 没有网络保存到本地
     */
    public void uncaughtException(Thread thread, Throwable ex) {
        LogUtil.deMessage("CrashHandler", "uncaughtException >> ");
        if (handleException(ex)) {
            try {
                thread.sleep(2000);
                prepareRestarApp();
                exitProgram();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } else {
            if (mDefaultHandler != null) {
                mDefaultHandler.uncaughtException(thread, ex);
            }
        }
    }

    /**
     * @throws
     * @Title: prepareRestarApp
     * @Description: 延时并重启应用程序
     */
    private void prepareRestarApp() {

        LogUtil.deMessage("CrashHandler", "prepareRestarApp >> ");
//

//        Intent intent = new Intent(application.getApplicationContext(), MainActivity.class);
//        PendingIntent restartIntent = PendingIntent.getActivity(
//                application.getApplicationContext(), 0, intent,
//                Intent.FLAG_ACTIVITY_NEW_TASK);
//        //退出程序
//        AlarmManager mgr = (AlarmManager)application.getSystemService(Context.ALARM_SERVICE);
//        mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 1000,
//                restartIntent); // 1秒钟后重启应用
//        application.finishActivity();

        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        PendingIntent restartIntent = PendingIntent.getActivity(
                mContext.getApplicationContext(), 0, intent,
                Intent.FLAG_ACTIVITY_NEW_TASK);
        // 退出程序
        AlarmManager mgr = (AlarmManager) mContext
                .getSystemService(Context.ALARM_SERVICE);
        mgr.set(AlarmManager.RTC, System.currentTimeMillis() + RESTART_TIME,
                restartIntent); // 1秒钟后重启应用

        LogUtil.deMessage("CrashHandler", "prepareRestarApp success >> ");
    }

    /**
     * 结束当前程序
     *
     * @throws
     * @Title: exitProgram
     * @Description: TODO(这里用一句话描述这个方法的作用)
     */
    private void exitProgram() {
        LogUtil.deMessage("CrashHandler", "exitProgram >> ");
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(1);
    }

    /**
     * @param ex 异常信息
     * @return true:如果处理了该异常信息;否则返回false.
     * @throws
     * @Title: handleException
     * @Description: 自定义错误处理, 收集错误信息 发送错误报告等操作均在此完成.
     */
    public boolean handleException(Throwable ex) {

        String crashMessage = getCrashMessageData(mContext, ex);
        SystemUtil.NetWorkState state = SystemUtil.getConnectState(mContext);
        if (state == SystemUtil.NetWorkState.NONE) {
            FileUtils.saveErrorLog2SDNew(mContext, crashMessage);
        } else {
            // 提交到服务器
            FileUtils.saveErrorLog2SDNew(mContext, crashMessage);
        }
        new Thread() {
            public void run() {
                Looper.prepare();
                Toast.makeText(mContext, "app handle an Exception",
                        Toast.LENGTH_LONG).show();
                Looper.loop();
            }
        }.start();
        LogUtil.deMessage("CrashHandler", "handleException success>> ");
        return true;
    }

    /**
     * @param context
     * @throws
     * @Title: getOsAndDeviceInfo
     * @Description: 收集设备参数信息
     */
    public void getOsAndDeviceInfo(Context context) {
        if (info == null) {
            info = new HashMap<String, String>();
        }
        info.put("versionName", SystemUtil.getVersionName(context));
        info.put("versionCode", SystemUtil.getVersionCode(context) + "");
        Field[] fields = Build.class.getDeclaredFields();
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                info.put(field.getName(), field.get("").toString());
                Log.d(TAG, field.getName() + ":" + field.get(""));
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * @param ex
     * @return
     * @throws
     * @Title: getCrashMessageData
     * @Description: 获取崩溃的的数据信息 获取设备信息 获取崩溃的数据
     */
    private String getCrashMessageData(Context context, Throwable ex) {
        getOsAndDeviceInfo(context);
        StringBuffer sb = new StringBuffer();
        sb.append("==================== CQMQ LTD ====================");
        sb.append("\r\n");
        sb.append("author:zrspring");
        sb.append("\r\n");
        sb.append("DRIVICE INFO:");
        sb.append("\r\n");
        for (Map.Entry<String, String> entry : info.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            sb.append("|* " + key + " = " + value + "\r\n");
        }
        sb.append("CRASH INFO:");
        sb.append("\r\n");
        Writer writer = new StringWriter();
        PrintWriter pw = new PrintWriter(writer);
        ex.printStackTrace(pw);
        Throwable cause = ex.getCause();
        while (cause != null) {
            cause.printStackTrace(pw);
            cause = cause.getCause();
        }
        pw.close();
        String result = writer.toString();
        sb.append(result);
        return sb.toString();
    }
}
