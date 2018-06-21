package com.zrspring.libv2.util;

import android.app.Activity;

import java.io.File;

/**
 * Version: 1.0
 * Author: rui
 * CreatDate: 2016年05月27日23:28 28
 * Encoding:UTF-8
 * Description: 应用缓存管理工具
 */
public class AppCacheManager {


    public AppCacheManager() {
    }

    public  void getAppCacheSize(final Activity context, final CacheListener cacheListener) {
        context.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                File file = context.getCacheDir();
                File file1 = context.getExternalFilesDir("");
                long allCacheSize = FileUtils.getDirSize(file)+FileUtils.getDirSize(file1);
                LogUtil.infoMessage("MainSlidingActivity", "file 缓存大小:" + FileUtils.getFormatSize(allCacheSize));
                cacheListener.getCacheSuccess(FileUtils.getFormatSize(allCacheSize));
            }
        });
    }

    public void cleanAppCache(final Activity context,final CacheListener cacheListener){
        context.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                File file = context.getCacheDir();
                File file1 = context.getExternalFilesDir("");
                FileUtils.delAllFile(file.getPath());
                FileUtils.delAllFile(file1.getPath());
                cacheListener.cacheCleanSuccess();
            }
        });
    }

    public interface CacheListener{
        public void getCacheSuccess(String sizeStr);
        public void cacheCleanSuccess();
    }
}
