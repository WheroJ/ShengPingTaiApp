package com.cmcc.pp.app;


import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.cmcc.pp.BuildConfig;
import com.cmcc.pp.entity.UserEntity;
import com.cmcc.pp.util.CrashHandler;
import com.zrspring.libv2.LibInit;
import com.zrspring.libv2.util.SystemUtil;

import org.litepal.LitePal;
import org.litepal.crud.DataSupport;

import java.util.List;


/**
 * @Encoding:UTF-8
 * @ClassName: App
 * @Description: TODO
 * @Author 请填写作者名
 * @CreatDate 2017年12月11日 上午09:33:00
 * @ConputerUserName li
 */

public class App extends Application {

    private static App sInstance = null;
    private String appVersion;

    public static synchronized App getInstance() {
        return sInstance;
    }

    //=================================
    // 用户
    //=================================
    private UserEntity userEntity;

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;

        LibInit.getInstance().init(this);
        LibInit.getInstance().setDebug(BuildConfig.DEBUG);

        LitePal.initialize(this);
        CrashHandler.getInstance().init(this);
    }

    /**
     * @MethodthName: getAppVersion
     * @Description: 获取程序版本
     * @params:
     * @Return:java.lang.String
     * @Throw:
     */
    public String getAppVersion() {
        if (appVersion == null) {
            appVersion = String.valueOf(SystemUtil.getVersionName(getApplicationContext()));
        }
        return appVersion;
    }

    //拿到用户信息
    public UserEntity getUserEntity() {
        if (userEntity == null) {
            List<UserEntity> userEntities = DataSupport.findAll(UserEntity.class);
            if (userEntities != null && !userEntities.isEmpty())
                userEntity = userEntities.get(0);
        }
        return userEntity;
    }

    //设置用户信息
    public void setUserEntity(UserEntity userEntity) {
        if (userEntity != null) {
            userEntity.save();
        }
        this.userEntity = userEntity;

    }

    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
