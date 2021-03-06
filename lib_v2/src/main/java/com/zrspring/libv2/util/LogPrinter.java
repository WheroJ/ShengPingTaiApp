package com.zrspring.libv2.util;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import com.zrspring.libv2.LibInit;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LogPrinter {

	private static final boolean DEBUG = LibInit.getInstance().getDebug();
	
	public static void i(String tag, String msg){
		if (DEBUG && !TextUtils.isEmpty(msg)) {
			Log.i(tag, msg);
		}
	}

	public static void d(String tag, String msg) {
		if (DEBUG && !TextUtils.isEmpty(msg)) {
			Log.d(tag, msg);
		}
	}
	
	public static void e(String tag, String msg) {
		if (DEBUG && !TextUtils.isEmpty(msg)) {
			Log.e(tag, msg);
		}
	}
	public static void e(String tag, String msg, Throwable tr) {
		if (DEBUG && !TextUtils.isEmpty(msg)) {
			Log.e(tag, msg, tr);
		}
	}
	public static void w(String tag, String msg) {
		if (DEBUG && !TextUtils.isEmpty(msg)) {
			Log.w(tag, msg);
		}
	}
	public static void w(String tag, String msg, Throwable tr) {
		if (DEBUG && !TextUtils.isEmpty(msg)) {
			Log.w(tag, msg, tr);
		}
	}

	/**
	 * 将日志输出到本地文件
	 * @param tag
	 * @param msg
     */
	public static void appendLog(String tag, String msg){
		Context mContext = LibInit.getInstance().getContext();
		String path ;
		if (!(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))) {
			path = mContext.getFilesDir().getAbsolutePath();
		} else {
			path = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + mContext.getPackageName();
		}
		path += File.separator + "log";
		File dir = new File(path);
		if(!dir.exists())
			dir.mkdirs();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String fileName = simpleDateFormat.format(new Date()) + ".log";
		File logFile = new File(dir, fileName);
		try {
			if (!logFile.exists()){
				logFile.createNewFile();
			}
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(logFile, true)));
			bufferedWriter.append(format.format(new Date()) + "---------------------------TAG=" + tag + "---------------------------");
			bufferedWriter.newLine();
			bufferedWriter.append(msg);
			bufferedWriter.newLine();
			bufferedWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
