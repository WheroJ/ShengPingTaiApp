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

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.util.TypedValue;

/**
* @ClassName: ScreenTools
* @Description: 屏幕处理工具
* @author 请填写作者名
* @date 2014-5-24 下午1:54:27
* @ConputerUserName zrspring
*/

public class ScreenTools {

	/**
	* @Title: getStatusBarHeight
	* @Description: 获取android状态栏高度   
	* @param mActivity
	* @return int 状态栏高度
	* @throws
	*/
	public static int getStatusBarHeight(Activity mActivity){
		Rect frame = new Rect();
		mActivity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
		int statusBarHeight = frame.top;
		return statusBarHeight;
	}
	
	/**
	* @Title: getScreenWithHeight
	* @Description: 获取屏幕宽高 
	* @param context 上下文
	* @return int[] int[0]为宽 int[1]为高 
	* @throws
	*/
	public static int[] getScreenWithHeight(Context context){
		int[] screecn = new int[2]; 
		DisplayMetrics dm = new DisplayMetrics();  
		((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(dm);  
		int widthPixels= dm.widthPixels;  
		int heightPixels= dm.heightPixels;
//		LogUtil.infoMessage("ScreenTools", " widthPixels = "+widthPixels);
//		LogUtil.infoMessage("ScreenTools", " heightPixels = "+heightPixels);
//		float density = dm.density;
//		int screenWidth = (int) (widthPixels * density) ;
//		int screenHeight = (int) (heightPixels * density) ;
		screecn[0]= widthPixels;
		screecn[1]= heightPixels;
		return screecn;
	}
	
	/**
	* @Title: Dp2Px
	* @Description: dip转px   
	* @param context
	* @param dp 
	* @return 
	* @throws
	*/
	public static int Dp2Px(Context context, float dp) { 
	    final float scale = context.getResources().getDisplayMetrics().density; 
	    return (int) (dp * scale + 0.5f); 
	} 
	 
	/**
	* @Title: Px2Dp
	* @Description: px转dip   
	* @param context
	* @param px
	* @return 
	* @throws
	*/
	public static int Px2Dp(Context context, float px) { 
	    final float scale = context.getResources().getDisplayMetrics().density; 
	    return (int) (px / scale + 0.5f); 
	}

	public static int value2Dip(Context context,float mValue) {
		return (int) (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, mValue,
				context.getResources().getDisplayMetrics()));
	}

	public static int value2Px(Context context, float mValue) {
		return (int) (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, mValue,
				context.getResources().getDisplayMetrics()));
	}

	public static int value2Sp(Context context,float mValue) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, mValue,
				context.getResources().getDisplayMetrics());
	}

	
}
