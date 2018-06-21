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

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.cmcc.pp.ui.widget.DefaultPageView;
import com.cmcc.pp.ui.widget.ViewHeaderBar;
import com.zrspring.libv2.network.RxUtils;

import butterknife.ButterKnife;

/**
 * @author zrspring
 * @ClassName: BaseFragment
 * @Description: 基础的Fragment
 * @date 2015-11-24 下午3:52:30
 * @ConputerUserName zrspring
 */

public abstract class BFragment extends Fragment {

    private boolean hasTitle;
    private ViewHeaderBar viewHeadBar;
    private LinearLayout llContainer;
    private DefaultPageView defaultPageView;
    private View contentView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        contentView = inflater.inflate(getLayoutId(), container, false);
        hasTitle = getHasTitle();
        gainView(contentView);
        ButterKnife.bind(this, contentView);

        initView(contentView);
        loadData();
        return contentView;
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

    public View getContentView() {
        return contentView;
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

    private void gainView(View contentView) {
        llContainer = contentView.findViewById(com.cmcc.pp.R.id.activityBase_content);
        defaultPageView = contentView.findViewById(com.cmcc.pp.R.id.activityBase_error);
        defaultPageView.setVisibility(View.GONE);
        defaultPageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reLoadData();
                defaultPageView.setVisibility(View.GONE);
            }
        });
        LinearLayout.LayoutParams LayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        View view = View.inflate(getContext(), getContentLayoutId(), null);
        llContainer.addView(view, LayoutParams);

        viewHeadBar = contentView.findViewById(com.cmcc.pp.R.id.activityBase_header);
        if (hasTitle) {
            viewHeadBar.setOnViewClickListener(new ViewHeaderBar.OnViewClickListener() {
                @Override
                public void onRightClick(int viewType) {

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
     * 默认实现结束当前页
     */
    protected void onLeftClick() {
        if (isAdded()) getActivity().finish();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        cleanMemory();
        ButterKnife.unbind(this);
        RxUtils.INSTANCE.cancelRequest();
    }

    protected abstract boolean getHasTitle();

    private int getLayoutId() {
        return com.cmcc.pp.R.layout.activity_base;
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
     * @param fragmentView
     * @throws
     * @Title: initView
     * @Description: 初始化布局
     */
    public abstract void initView(View fragmentView);

    /**
     * @Title: loadData @Description: 加载数据，调用服务器数据，或者数据库等 @throws
     */
    public abstract void loadData();

    /**
     * @Title: cleanMemory @Description: 清空内存工作 @throws
     */
    public void cleanMemory() {
    }
}
