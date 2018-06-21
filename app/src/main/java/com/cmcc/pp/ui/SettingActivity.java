package com.cmcc.pp.ui;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cmcc.pp.R;
import com.cmcc.pp.base.BActivity;
import com.cmcc.pp.ui.widget.RefreshLinearLayout;
import com.cmcc.pp.util.ClearData;
import com.cmcc.pp.util.PopuWindowForSetting;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by ABC on 2017/12/12.
 * 设置
 */

public class SettingActivity extends BActivity {

    @Bind(R.id.tv_cache)
    TextView tvCache;
    @Bind(R.id.lin_cache)
    LinearLayout linCache;
    @Bind(R.id.bt_signout)
    Button btSignout;
    @Bind(R.id.content)
    RefreshLinearLayout refreshLinearLayout;

    private String strTips = "";
    private Handler handler;
    @Override
    protected boolean getHasTitle() {
        return true;
    }

    @Override
    public int getContentLayoutId() {
        return R.layout.activity_setting;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        getHeader().setTitle(R.string.str_setting);

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case 1:
                        strTips = "清除成功";
                        refreshLinearLayout.updateStatus(RefreshLinearLayout.SUCCESS,strTips,true);
                        onClickCleanCache();

                        break;
                    case 2:
                        refreshLinearLayout.updateStatus(RefreshLinearLayout.LOADING,strTips,true);
                        handler.sendEmptyMessageDelayed(1, 5000);
                        break;
                    case 3:
                        refreshLinearLayout.updateStatus(RefreshLinearLayout.FAIL,strTips,true);
                        break;

                }
            }
        };

        caculateCacheSize();
    }

    @Override
    public void loadData() {

    }

    private void caculateCacheSize() {

        try {
            ClearData clearData = new ClearData(getApplication());
            tvCache.setText(clearData.getTotalCacheSize());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnClick({
            R.id.lin_cache,
            R.id.bt_signout
    })
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.lin_cache:
                strTips = "清除中...";
                Message message = Message.obtain();
                message.what = 2;
                handler.sendMessage(message);

                break;
            case R.id.bt_signout:
                PopuWindowForSetting popuWindowForSetting = new PopuWindowForSetting();
                popuWindowForSetting.bottomwindow(this, btSignout);

                break;
        }
    }


    private void onClickCleanCache() {
        ClearData clearData = new ClearData(getApplication());
        clearData.clearAllDataOfApplication();
        try {
            tvCache.setText(clearData.getTotalCacheSize());
        } catch (Exception e) {
            e.printStackTrace();

            strTips = "清除失败";
            Message message = Message.obtain();
            message.what = 3;
            handler.sendMessage(message);
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
