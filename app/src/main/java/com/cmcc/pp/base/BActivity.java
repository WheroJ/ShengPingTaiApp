package com.cmcc.pp.base;

/*   
 * Copyright (c) 2014-2014 China.ChongQing.MaiQuan Ltd All Rights Reserved.   
 *   
 * This software is the confidential and proprietary information of   
 * Founder. You shall not disclose such Confidential Information   
 * and shall use it only in accordance with the terms of the agreements   
 * you entered into with Founder.   
 *   
 */

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.cmcc.pp.R;
import com.cmcc.pp.ui.widget.DefaultPageView;
import com.cmcc.pp.ui.widget.ViewHeaderBar;
import com.cmcc.pp.util.Constant;
import com.cmcc.pp.util.IntentUtils;
import com.cmcc.pp.util.UIUtils;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.zrspring.libv2.base.ActivityCollector;
import com.zrspring.libv2.network.RxUtils;
import com.zrspring.libv2.util.SPUtil;

import butterknife.ButterKnife;

/**
 * @author 请填写作者名
 * @ClassName: BActivity
 * @Description: 自定义activity类， 执行顺序 onCreate() > getContentLayoutId() > initView() >
 * findExtraData() > loadData(); onDestroy() > cleanMemory()
 * @date 2014-5-25 下午5:45:10
 * @ConputerUserName zrspring 2017年测试 2 3 4
 */

public abstract class BActivity extends AppCompatActivity {

    private boolean hasTitle;
    private ViewHeaderBar viewHeadBar;
    private LinearLayout llContainer;
    private DefaultPageView defaultPageView;
    private BFragment showFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        hasTitle = getHasTitle();
        beforeInit();
        super.onCreate(savedInstanceState);
        if (isSetStatusBarTransparent()) {
            setStatusBarState();
        }
        Fresco.initialize(this);
        setContentView(getLayoutId());
        gainView();

        ButterKnife.bind(this);
        initView(savedInstanceState);
        loadData();
        ActivityCollector.addActivity(this);
    }

    public void showError() {
        defaultPageView.setVisibility(View.VISIBLE);
    }

    public void showError(int resId, String errorText) {
        defaultPageView.setVisibility(View.VISIBLE);
        defaultPageView.setErrorImageAndText(resId, errorText);
    }

    public void showError(String errorText) {
        defaultPageView.setVisibility(View.VISIBLE);
        defaultPageView.setErrorText(errorText);
    }

    /**
     * 获取标题栏
     *
     * @return
     */
    protected final ViewHeaderBar getHeader() {
        if (hasTitle) {
            return viewHeadBar;
        }
        return null;
    }

    /**
     * 默认实现为点击重新加载数据
     */
    protected void reLoadData() {
        loadData();
    }

    private void gainView() {
        llContainer = findViewById(R.id.activityBase_content);
        defaultPageView = findViewById(R.id.activityBase_error);
        defaultPageView.setVisibility(View.GONE);
        defaultPageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reLoadData();
                defaultPageView.setVisibility(View.GONE);
            }
        });
        LinearLayout.LayoutParams LayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        View view = View.inflate(this, getContentLayoutId(), null);
        llContainer.addView(view, LayoutParams);

        viewHeadBar = findViewById(R.id.activityBase_header);
        if (hasTitle) {
            viewHeadBar.setOnViewClickListener(new ViewHeaderBar.OnViewClickListener() {
                @Override
                public void onRightClick(int viewType) {
                    onFinishClick();
                }

                @Override
                public void onCenterClick(int viewType) {

                }

                @Override
                public void onBackClick(int viewType) {
                    onLeftClick();
                }
            });
        } else {
            viewHeadBar.setVisibility(View.GONE);
        }
    }


    /**
     * 顶部右侧点击
     */
    protected void onFinishClick() {

    }

    /**
     * 默认实现结束当前页
     */
    protected void onLeftClick() {
        finish();
    }



    /**
     * 初始化之前调用，默认空实现
     */
    public void beforeInit() {
    }

    /**
     * 替换Fragment
     * @param fragment
     */
    protected void replaceShow(BFragment fragment){
        try {
            if (showFragment == null) {
                getSupportFragmentManager().beginTransaction().show(fragment).commitAllowingStateLoss();
                showFragment = fragment;
            } else {
                getSupportFragmentManager().beginTransaction().hide(showFragment).show(fragment).commitAllowingStateLoss();
                showFragment = fragment;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 替换Fragment
     * @param layoutId
     * @param fragment
     */
    protected void replaceShow(BFragment fragment, int layoutId){
        try {
            getSupportFragmentManager().beginTransaction().replace(layoutId, fragment).commitAllowingStateLoss();
            showFragment = fragment;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cleanMemory();
        ActivityCollector.removeActivity(this);
        //所有界面都退出了，结束服务
        if (ActivityCollector.activities.size() == 0) {
            IntentUtils.stopServcie(UIUtils.getContext());
        }
        ButterKnife.unbind(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        //从后台运行 转到前台运行
        if (SPUtil.getBoolean(Constant.IS_RUN_BACK, false)) {
            //APP 前台运行重新开启服务
            SPUtil.saveBoolean(Constant.IS_RUN_BACK, false);
            IntentUtils.startReLoginService();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        RxUtils.INSTANCE.cancelRequest();
    }

    protected abstract boolean getHasTitle();

    private int getLayoutId() {
        return R.layout.activity_base;
    }

    public void setStatusBarState() {
//      //如果   4.3< 平台版本 < 5.0 打开4.4的透明
//		if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN_MR2 &&
//			Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP ) {
//			getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//		}
        //如果  平台版本 > 4.3 打开透明
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN_MR2) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }

    /**
     * @return
     * @Title: isSetStatusBarTransparent
     * @Description: true 是否开启状态栏透明，适用于4.4-6.0，全部同一
     * false是使用Android 5.0,6.0的Desgin风格，对应style主题下的参数colorPrimaryDark
     */
    protected boolean isSetStatusBarTransparent() {
        return false;
    }

    /**
     * @Title: getContentLayoutId @Description: 设置布局文件 @return @throws
     */
    public abstract int getContentLayoutId();

    /**
     * @Title: initView @Description: 初始化界面，和配置熟悉，并添加监听等 @throws
     */
    public abstract void initView(Bundle savedInstanceState);

    /**
     * @Title: loadData @Description: 加载数据，调用服务器数据，或者数据库等 @throws
     */
    public abstract void loadData();

    /**
     * @Title: cleanMemory @Description: 清空内存工作 @throws
     */
    public void cleanMemory() {
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //super.onSaveInstanceState(outState);
        //activity 销毁的时候不保存 Fragment状态
        super.onSaveInstanceState(outState);
    }
}
