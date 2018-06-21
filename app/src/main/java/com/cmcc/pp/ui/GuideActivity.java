package com.cmcc.pp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.cmcc.pp.R;
import com.cmcc.pp.adapter.ViewPagerAdapter;
import com.cmcc.pp.base.BActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ABC on 2017/12/13.
 * 欢迎 引导页面
 */

public class GuideActivity extends BActivity implements ViewPager.OnPageChangeListener {

    private ViewPager vp;
    private ViewPagerAdapter vpAdapter;
    private List<View> views;

    // 底部小点图片
    private ImageView[] dots;

    // 记录当前选中位置
    private int currentIndex;


    @Override
    protected boolean getHasTitle() {
        return false;
    }

    @Override
    public int getContentLayoutId() {
        return R.layout.activity_guideactivity;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        // 初始化页面
        initViews();

        // 初始化底部小点
        initDots();
    }

    @Override
    public void loadData() {

    }

    private void initViews() {

        LayoutInflater inflater = LayoutInflater.from(this);
        RelativeLayout guideFour = (RelativeLayout) inflater.inflate(R.layout.guide_four, null);
        guideFour.findViewById(R.id.toMain).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GuideActivity.this,LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
        views = new ArrayList<View>();
        // 初始化引导图片列表
        views.add(inflater.inflate(R.layout.guide_one, null));
        views.add(inflater.inflate(R.layout.guide_two, null));
        views.add(inflater.inflate(R.layout.guide_three, null));
        views.add(guideFour);
        // 初始化Adapter
        vpAdapter = new ViewPagerAdapter(views, this);

        vp = (ViewPager) findViewById(R.id.viewpager);
        vp.setAdapter(vpAdapter);
        // 绑定回调
        vp.setOnPageChangeListener(this);


    }

    private void initDots() {
        LinearLayout ll = (LinearLayout) findViewById(R.id.ll);

        dots = new ImageView[views.size()];

        // 循环取得小点图片
        for (int i = 0; i < views.size(); i++) {
            dots[i] = (ImageView) ll.getChildAt(i);
           // dots[i].setEnabled(true);// 都设为灰色
            dots[i].setBackgroundResource(R.drawable.bullet_white);
        }

        currentIndex = 0;
        dots[currentIndex].setBackgroundResource(R.drawable.bullet_yellow);;// 即选中状态
    }

    private void setCurrentDot(int position) {
        if (position < 0 || position > views.size() - 1
                || currentIndex == position) {
            return;
        }

        dots[position].setBackgroundResource(R.drawable.bullet_yellow);
        dots[currentIndex].setBackgroundResource(R.drawable.bullet_white);

        currentIndex = position;
    }

    // 当滑动状态改变时调用
    @Override
    public void onPageScrollStateChanged(int arg0) {
    }

    // 当当前页面被滑动时调用
    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {
    }

    // 当新的页面被选中时调用
    @Override
    public void onPageSelected(int arg0) {
        // 设置底部小点选中状态
        setCurrentDot(arg0);
    }
}
