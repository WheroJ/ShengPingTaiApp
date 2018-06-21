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
import android.util.Log;
import com.zrspring.libv2.util.logger.Logger;


/**
 * @ClassName: LogUtil
 * @Description: LogUtil工具类
 * @author zrspring
 * @date 2014-5-24 下午1:03:41
 * @ConputerUserName zrspring
 */
public class LogUtil {

	public static final String TAG = "LogUtil";

	private static boolean DEBUG = false;

	public static void init( boolean isDeBug){
		DEBUG  = isDeBug;
		if(DEBUG){
			Logger.init();
		}
	}

	public static void deMessage(String tag, String msg) {
		if (DEBUG) {
			//Log.i(tag, msg);
			Logger.d(msg,tag);
		}
	}

	public static void warningMessage(String tag, String msg) {
		if (DEBUG) {
//			Log.w(tag, msg);
			Logger.w(msg,tag);
		}
	}

	public static void infoMessage(String tag, String msg) {
		if (DEBUG) {
//			Log.w(tag, msg);
			Logger.i(msg,tag);
		}
	}

	public static void infoMessageOld(String tag, String msg) {
		if (DEBUG) {
			Log.i(tag, msg);
		}
	}

	public static void json(String jsonStr) {
		if (DEBUG) {
			Logger.json(jsonStr);
		}
	}

	public static void errorMessage(String tag, String msg) {
		if (DEBUG) {
//			Log.e(tag, msg);
			Logger.e(msg,tag);
		}
	}
}
