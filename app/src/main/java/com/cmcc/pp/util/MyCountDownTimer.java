package com.cmcc.pp.util;

import android.graphics.Color;
import android.os.CountDownTimer;
import android.widget.TextView;

import com.cmcc.pp.R;

/**
 * Created by ABC on 2017/12/12.
 * 倒计时
 */

public class MyCountDownTimer extends CountDownTimer {
    private  TextView tv;
    public MyCountDownTimer(TextView textView,long millisInFuture, long countDownInterval) {
        super(millisInFuture, countDownInterval);
        this.tv=textView;
    }

    //计时过程
    @Override
    public void onTick(long l) {
        //防止计时过程中重复点击
        tv.setClickable(false);
        tv.setText(l/1000+"秒后");
        tv.setBackgroundResource(R.drawable.bg_gray_all_4radius);
        tv.setTextColor(Color.rgb(154,154,154));

    }

    //计时完毕的方法
    @Override
    public void onFinish() {
        //重新给Button设置文字
        tv.setText("点击获取");
        //设置可点击
        tv.setClickable(true);
        tv.setBackgroundResource(R.drawable.bg_blue_all_4radius);
        tv.setTextColor(Color.rgb(255,255,255));
    }
}
