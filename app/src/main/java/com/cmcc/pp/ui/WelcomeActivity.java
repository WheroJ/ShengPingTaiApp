package com.cmcc.pp.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.cmcc.pp.R;
import com.cmcc.pp.base.BActivity;

/**
 * Created by ABC on 2017/12/13.
 * 启动页面
 */

public class WelcomeActivity extends BActivity implements Runnable{

    @Override
    protected boolean getHasTitle() {
        return false;
    }

    @Override
    public int getContentLayoutId() {
        return R.layout.activity_welcom;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        //启动一个延迟线程
        new  Thread(this).start();
    }

    @Override
    public void loadData() {

    }

    @Override
    public void run() {
        try{
            //延迟1秒时间
            Thread.sleep(1000);
            SharedPreferences preferences= getSharedPreferences("first", 0); // 存在则打开它，否则创建新的Preferences
            int count = preferences.getInt("first", 0); // 取出数据

            /**
             *如果用户不是第一次使用则直接调转到显示界面,否则调转到引导界面
             */
            if (count == 0) {
                Intent intent1 = new Intent();
                intent1.setClass(WelcomeActivity.this, GuideActivity.class);
                startActivity(intent1);
            } else {
                Intent intent2 = new Intent();
                intent2.setClass(WelcomeActivity.this, LoginActivity.class);
                startActivity(intent2);
            }
            finish();

            //实例化Editor对象
            SharedPreferences.Editor editor = preferences.edit();
            //存入数据
            editor.putInt("first", 1); // 存入数据
            //提交修改
            editor.commit();
        } catch (InterruptedException e) {

        }
    }
}
