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

import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @ClassName: FileUtils
 * @Description: FileUtils功能: 创建目录路径， 创建文件路径， 删除文件夹， 删除文件， 获取文件的URI, 换算文件大小
 * @author zrspring
 * @date 2014-5-24 下午1:41:56
 * @ConputerUserName zrspring
 */

public class FileUtils {

	/**
	 * @Title: createDirFile @Description: 创建根目录,包含子文件路径
	 * 例如创建：/mnt/sdcard/cqmq/testdir/... @param path 创建路径目录路径 @throws
	 */
	public static void createDir(String path) {
		File dir = new File(path);
		if (!dir.exists()) {
			dir.mkdirs();
		}

	}

	/**
	 * @Title: createNewFile @Description: 创建文件 例如
	 * 例如创建：/mnt/sdcard/cqmq/testdir/test.txt
	 * @param path 创建的文件 @return @throws
	 */
	public static File createNewFile(String path) {
		File file = new File(path);
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				return null;
			}
		}
		return file;
	}

	/**
	 * @Title: isFlieExites @Description: 判断是否文件存在
	 * 例如判断：例如创建：/mnt/sdcard/cqmq/testdir/test.txt 的test.txt的存在 @param path
	 * 文件的路径，含文件后缀和文件名 @return ture 表示文件或者文件夹存在 false 表示不存在文件夹或文件 @throws
	 */
	public static Boolean isFlieExites(String path) {
		return new File(path).exists();
	}

	/**
	 * @Title: delFolder @Description: 删除文件(含文件) 例如
	 * /mnt/sdcard/cqmq/testdir/test.txt @param folderPath文件夹的路径 @throws
	 */
	public static void delFolder(String folderPath) {
		delAllFile(folderPath);
		String filePath = folderPath;
		filePath = filePath.toString();
		File myFilePath = new File(filePath);
		myFilePath.delete();
	}

	/**
	 * @Title: delAllFile @Description: 删除当前文件夹下的所有文件 @param path 文件夹的路径 @throws
	 */
	public static void delAllFile(String path) {
		File file = new File(path);
		if (!file.exists()) {
			return;
		}
		if (!file.isDirectory()) {
			return;
		}
		String[] tempList = file.list();
		File temp = null;
		for (int i = 0; i < tempList.length; i++) {
			if (path.endsWith(File.separator)) {
				temp = new File(path + tempList[i]);
			} else {
				temp = new File(path + File.separator + tempList[i]);
			}
			if (temp.isFile()) {
				temp.delete();
			}
			if (temp.isDirectory()) {
				delAllFile(path + "/" + tempList[i]);
				delFolder(path + "/" + tempList[i]);
			}
		}
	}

	/**
	 * @Title: getUriFromFile @Description: 获取文件的Uri @param path 文件的路径 @return
	 * 文件的Uri @throws
	 */
	public static Uri getUriFromFile(String path) {
		File file = new File(path);
		return Uri.fromFile(file);
	}

	/**
	 * @MethodthName: getFormatSize
	 * @params: [size]
	 * @Return:java.lang.String
	 * @Throw:
	 * @Description: 获取当前缓存大小
	 *
	 */
	public static String getFormatSize(double size) {

		double kiloByte = size / 1024;
		if (kiloByte < 1) {
			return size + "Byte";
		}

		double megaByte = kiloByte / 1024;
		if (megaByte < 1) {
			BigDecimal result1 = new BigDecimal(Double.toString(kiloByte));
			return result1.setScale(2, BigDecimal.ROUND_HALF_UP)
					.toPlainString() + "KB";
		}

		double gigaByte = megaByte / 1024;
		if (gigaByte < 1) {
			BigDecimal result2 = new BigDecimal(Double.toString(megaByte));
			return result2.setScale(2, BigDecimal.ROUND_HALF_UP)
					.toPlainString() + "MB";
		}

		double teraBytes = gigaByte / 1024;
		if (teraBytes < 1) {
			BigDecimal result3 = new BigDecimal(Double.toString(gigaByte));
			return result3.setScale(2, BigDecimal.ROUND_HALF_UP)
					.toPlainString() + "GB";
		}
		BigDecimal result4 = new BigDecimal(teraBytes);
		return result4.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString()
				+ "TB";
	}

	/**
	 * @MethodthName: getDirSize
	 * @params: [dir]
	 * @Return:long
	 * @Throw:
	 * @Description: 获取当前目录下所有
	 *
	 */
	public static long getDirSize(File dir) {
		if (dir == null) {
			return 0;
		}
		if (!dir.isDirectory()) {
			return 0;
		}
		long dirSize = 0;
		File[] files = dir.listFiles();
		for (File file : files) {
			if (file.isFile()) {
				dirSize += file.length();
			} else if (file.isDirectory()) {
				dirSize += file.length();
				dirSize += getDirSize(file); // 递归调用继续统计
			}
		}
		return dirSize;
	}


	/**
	 * @Title: copyAssetsToSdCard 
	 * @Description: 将(Assets 下的文件)指定的资源文件名复制到指定的路径下 
	 * @param context 上下文 
	 * @param assetName (最好英文命名的文件，中文未测试)
	 * @param savePath 保存的路径 
	 * @throws
	 */
	public static void copyAssetsToSdCard(Context context, String assetName, String savePath) {
		if (SystemUtil.isSdcardExist()) {
			LogUtil.deMessage("copyAssetsToSdCard", "save path = " + savePath);
			InputStream input;
			OutputStream output;
			try {
				output = new FileOutputStream(savePath);
				input = context.getAssets().open(assetName);
				byte[] buffer = new byte[1024];
				int length = input.read(buffer);
				while (length > 0) {
					output.write(buffer, 0, length);
					length = input.read(buffer);
				}
				output.flush();
				input.close();
				output.close();
				LogUtil.deMessage("copyAssetsToSdCard", "save copyAssetsToSdCard success" + savePath);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			throw new RuntimeException(" there have no sdcard ,can`t copy " + assetName + " to " + savePath);
		}
	}


	/**
	 * @Title: copyAssetsToSdCard
	 * @Description: 将(Assets 下的文件)指定的资源文件名复制到指定的路径下
	 * @param cacheImgPath (最好英文命名的文件，中文未测试)
	 * @param savePath 保存的路径
	 * @throws
	 */
	public static void copyImgToSdCardFomChece( String cacheImgPath, String savePath) {
		if (SystemUtil.isSdcardExist()) {
			LogUtil.deMessage("copyImgToSdCardFomCache", "save path = " + savePath);
			InputStream input;
			OutputStream output;
			try {
				output = new FileOutputStream(savePath);
				input = new FileInputStream(cacheImgPath);
				byte[] buffer = new byte[1024];
				int length = input.read(buffer);
				while (length > 0) {
					output.write(buffer, 0, length);
					length = input.read(buffer);
				}
				output.flush();
				input.close();
				output.close();
				LogUtil.deMessage("copyAssetsToSdCard", "save img success" + savePath);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			throw new RuntimeException(" there have no sdcard ,can`t copy img to " + savePath);
		}
	}

	/**
	 * @Title: saveErrorLog2SD @Description: 将errorlog文本储存到SD卡上
	 * /mnt/sdcard/.cqmq/PackageName/errorLog @param context @param text
	 * 需要保存的文本数据 @throws
	 */
	public static void saveErrorLog2SD(Context context, String text) {
		String time = TimeTools.getCurrentTimeInString(TimeTools.getDATA_YYYY_MM_DD_HH_MM_SS());
		String fileName = "crash_time_" + time + ".log";
		if (SystemUtil.isSdcardExist()) {
//			File dir = new File(CFG.DIR_LOG);
//			if (!dir.exists()) {
//				dir.mkdirs();
//			}
//			try {
//				FileOutputStream fos = new FileOutputStream(dir + fileName);
//				fos.write(text.getBytes());
//				fos.flush();
//				fos.close();
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
		}
	}

	public static void saveErrorLog2SDNew(Context context, String str) {
		String filePath = null;
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String filteName = "MSGJ: crash @ " + dateFormat.format(new Date(System.currentTimeMillis())) + ".txt";
		boolean hasSDCard = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
		if (hasSDCard) {
			filePath = Environment.getExternalStorageDirectory().toString() + File.separator + filteName;
		} else {
			filePath = Environment.getDownloadCacheDirectory().toString() + File.separator + filteName;
		}
		try {
			File file = new File(filePath);
			if (!file.exists()) {
				File dir = new File(file.getParent());
				dir.mkdirs();
				file.createNewFile();
			}
			FileOutputStream outStream = new FileOutputStream(file);
			outStream.write(str.getBytes());
			outStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		Toast.makeText(context, "统计结果保存到:" + filePath, Toast.LENGTH_LONG).show();
	}

	//根据图片名称，拿到资源文件图片
	public static String getResPath(Context context,String imgName){
		String path ="";
		try {
			path = "res://com.chinamansha.aw/"+ getResourceIdByNameFromDrawable(context,imgName);
		}catch (Exception e){
			e.printStackTrace();
		}
		return path;
	}

	public static int getResourceIdByNameFromDrawable(Context context , String imgName){
		return context.getResources().getIdentifier(imgName,"drawable",context.getPackageName());
	}

	public static int getResourceIdByNameFromMipmap(Context context , String imgName){
		return context.getResources().getIdentifier(imgName,"mipmap",context.getPackageName());
	}

}
