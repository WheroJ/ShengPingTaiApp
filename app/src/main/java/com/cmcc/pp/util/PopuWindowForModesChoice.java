package com.cmcc.pp.util;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.cmcc.pp.R;
import com.cmcc.pp.ui.LoginActivity;
import com.cmcc.pp.ui.gatewayadministration.TracerouteDiagnosisActivity;
import com.zrspring.libv2.base.ActivityCollector;

/**
 * Created by ABC on 2017/12/13.
 */

public class PopuWindowForModesChoice {
    private PopupWindow popupWindow;
    private LinearLayout layout;
    private float alpha=1f;
    private Activity thisactivity;
    public void choicewindow(Activity activity, View view) {
        thisactivity =activity;
        if (popupWindow != null && popupWindow.isShowing()) {
            return;
        }
        layout = (LinearLayout)activity.getLayoutInflater().inflate(R.layout.popuwindow_modechoice, null);
        popupWindow = new PopupWindow(layout,600,250,false);
        //点击空白处时，隐藏掉pop窗口
        popupWindow.setFocusable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.showAtLocation(view, Gravity.CENTER,0,0);
        //添加按键事件监听
        setButtonListeners(activity,layout);
        //添加pop窗口关闭事件，主要是实现关闭时改变背景的透明度
        popupWindow.setOnDismissListener(new poponDismissListener());

        new Thread(new Runnable(){
            @Override
            public void run() {
                while(alpha>0.4f){
                    try {
                        //4是根据弹出动画时间和减少的透明度计算
                        Thread.sleep(4);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Message msg =mHandler.obtainMessage();
                    msg.what = 1;
                    //每次减少0.01，精度越高，变暗的效果越流畅
                    alpha-=0.01f;
                    msg.obj =alpha ;
                    mHandler.sendMessage(msg);
                }
            }

        }).start();
    }

    //popuwindow 监听事件
    private void setButtonListeners(final Activity activity,LinearLayout layout) {
        TextView tvudp = (TextView) layout.findViewById(R.id.tvudp);
        TextView tvicmp = (TextView) layout.findViewById(R.id.tvicmp);
        final TracerouteDiagnosisActivity  activity1 = (TracerouteDiagnosisActivity) thisactivity;
        tvudp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (popupWindow != null && popupWindow.isShowing()) {
                    //在此处添加你的按键处理 xxx
                    popupWindow.dismiss();
                    activity1.setTvModechoice("UDP");
                }
            }
        });
        tvicmp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (popupWindow != null && popupWindow.isShowing()) {
                    popupWindow.dismiss();
                    activity1.setTvModechoice("ICMP");
                }
            }
        });
    }


    /**
     * 返回或者点击空白位置的时候将背景透明度改回来
     */
    class poponDismissListener implements PopupWindow.OnDismissListener{

        @Override
        public void onDismiss() {
            // TODO Auto-generated method stub
            new Thread(new Runnable(){
                @Override
                public void run() {
                    //此处while的条件alpha不能<= 否则会出现黑屏
                    while(alpha<1f){
                        try {
                            Thread.sleep(4);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        Log.d("HeadPortrait","alpha:"+alpha);
                        Message msg =mHandler.obtainMessage();
                        msg.what = 1;
                        alpha+=0.01f;
                        msg.obj =alpha ;
                        mHandler.sendMessage(msg);
                    }
                }

            }).start();
        }

    }



    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    setBackgroundAlpha(thisactivity,(float)msg.obj);
                    break;
            }
        }
    };


    /**
     * 设置页面的透明度
     * @param bgAlpha 1表示不透明
     */
    private  void setBackgroundAlpha(Activity activity, float bgAlpha) {
        WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
        lp.alpha = bgAlpha;
        if (bgAlpha == 1) {
            activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);//不移除该Flag的话,在有视频的页面上的视频会出现黑屏的bug
        } else {
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);//此行代码主要是解决在华为手机上半透明效果无效的bug
        }
        activity.getWindow().setAttributes(lp);
    }

}
