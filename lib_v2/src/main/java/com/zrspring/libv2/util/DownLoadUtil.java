package com.zrspring.libv2.util;
import android.app.DownloadManager;
import android.app.DownloadManager.Request;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;

/**
* @ClassName: DownLoadUtil
* @Description: 下载工具类
* @author 请填写作者名
* @date 2014-5-25 下午1:40:30
* @ConputerUserName zrspring
*/
public class DownLoadUtil {

		/**
		* @Fields _context : 
		*/
		private  Context _context;
		
		/**
		* Title:
		* Description: 下载工具类
		* @param context
		*/
		public DownLoadUtil(Context context) {
			 this._context=context;
		}
		
		/**
		* @Title: downFileWithWIFI
		* @Description: 只在WIFI的情况下下载  
		* @param url 下载地址
		* @param saveFileName 下载的文件名，例如 down.png
		* @param downFileDesc 下载的文件描述
		* @throws
		*/
		public  void downFileWithWIFI(String url,String saveFileName,String downFileDesc){
			down(url,saveFileName,downFileDesc,Request.NETWORK_WIFI);
		}
		
		/**
		* @Title: downFileWithAllMoblie
		* @Description: 使用移动网络的情况的情况下载
		* @param url 下载地址
		* @param saveFileName 下载的文件名，例如 down.png
		* @param downFileDesc 下载的文件描述
		* @throws
		*/
		public void downFileWithAllMoblie(String url,String saveFileName,String downFileDesc){
			down(url,saveFileName,downFileDesc,Request.NETWORK_MOBILE);
		}
		
		/**
		* @Title: down
		* @Description: TODO(这里用一句话描述这个方法的作用)   
		* @param url  网络地址
		* @param saveFileName 保存的文件名
		* @param downFileDesc 下载的文件描述
		* @param useNetType  使用的下载网络类型 Request. NETWORK_MOBILE OR NETWORK_WIFI
		* @throws
		*/
		public void down(String url,String saveFileName,String downFileDesc,int useNetType){
			DownloadManager downloadManager =  (DownloadManager)_context.getSystemService(Context.DOWNLOAD_SERVICE);
			Request request = new Request(Uri.parse(url));
			request.setAllowedNetworkTypes(useNetType);
			request.setNotificationVisibility(Request.VISIBILITY_VISIBLE_NOTIFY_ONLY_COMPLETION);
			//当通知栏显示完成后，点击通知栏会根据文件类型进行打开
			 request.setMimeType("application/vnd.android.package-archive");
			request.setTitle(SystemUtil.getAppName(_context));
			request.setDescription(downFileDesc);
			request.setAllowedOverRoaming(false);
			request.setDestinationInExternalFilesDir(_context, Environment.DIRECTORY_DOWNLOADS, saveFileName);
			request.setDestinationInExternalPublicDir("Trinea", saveFileName);
			downloadManager.enqueue(request);
		};
}
