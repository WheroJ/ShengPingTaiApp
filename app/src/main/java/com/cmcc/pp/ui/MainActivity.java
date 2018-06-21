package com.cmcc.pp.ui;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.Display;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cmcc.pp.R;
import com.cmcc.pp.base.BActivity;
import com.cmcc.pp.ui.gatewayadministration.FragmentGatewayAdministration;
import com.cmcc.pp.ui.platformmonitoring.PlatformMonitoringFragment;
import com.cmcc.pp.ui.transactiondata.FragmentTransactionFragment;
import com.cmcc.pp.ui.workorder.FragmentWorkOrder;
import com.cmcc.pp.util.IntentUtils;
import com.zrspring.libv2.util.LogUtil;
import com.zrspring.libv2.util.TimeTools;
import com.zrspring.libv2.util.ToastUtils;

import butterknife.Bind;
import butterknife.OnClick;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.RuntimePermissions;

/**
 * Created by ABC on 2017/12/22.
 * 主页菜单页面
 */

@RuntimePermissions
public class MainActivity extends BActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    public static final String TAG_EXIT = "exit";
    @Bind(R.id.drawer_layout)
    DrawerLayout drawer;
    @Bind(R.id.right)
    LinearLayout right;
    @Bind(R.id.nav_view)
    LinearLayout left;
    @Bind(R.id.menu_username)
    TextView menuUsername;
    @Bind(R.id.menu_userphone)
    TextView menuUserphone;
    @Bind(R.id.menu_transactiondata)
    LinearLayout menuTransactiondata;
    @Bind(R.id.menu_platformmonitoring)
    LinearLayout menuPlatformmonitoring;
    @Bind(R.id.menu_workorder)
    LinearLayout menuWorkorder;
    @Bind(R.id.menu_gatewayadministration)
    LinearLayout menuGatewayadministration;
    @Bind(R.id.menu_setting)
    ImageView menuSetting;
    @Bind(R.id.menu_lin_setting)
    LinearLayout menuLinSetting;

    private boolean isDrawer=false;
    private long backPressTimeRecord = 0l;
    private Fragment mTempFragment;// 临时界面变量
    private FragmentTransactionFragment fragmentTransactionData;//业务数据
    private PlatformMonitoringFragment platformMonitoringFragment;//平台监控
    private FragmentWorkOrder fragmentWorkOrder;//工单管理
    private FragmentGatewayAdministration fragmentGatewayAdministration;//网关管理
    public static final String HOME_TAG = "transactiondate";
    public static final String PLATFORMMONITORING_TAG = "platformmonitoring";
    public static final String WORKORDER_TAG = "workOrder";
    public static final String GATEWAYADMINISTRATION_TAG = "GatewayAdministration";
    @Override
    public boolean isSetStatusBarTransparent() {
        return false;
    }

    @Override
    public int getContentLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        inintViews();
    }
    @Override
    public void loadData() {

    }

    @Override
    public void cleanMemory() {

    }

    private void inintViews() {


        right.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(isDrawer){
                    return left.dispatchTouchEvent(motionEvent);
                }else{
                    return false;
                }
            }
        });


        drawer.setDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                isDrawer=true;
                //获取屏幕的宽高
                WindowManager manager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
                Display display = manager.getDefaultDisplay();
                //设置右面的布局位置  根据左面菜单的right作为右面布局的left   左面的right+屏幕的宽度（或者right的宽度这里是相等的）为右面布局的right
                right.layout(left.getRight(), 0, left.getRight() + display.getWidth(), display.getHeight());
            }
            @Override
            public void onDrawerOpened(View drawerView) {}
            @Override
            public void onDrawerClosed(View drawerView) {
                isDrawer=false;
            }
            @Override
            public void onDrawerStateChanged(int newState) {}
        });

        MainActivityPermissionsDispatcher.showHomeViewFragmentWithPermissionCheck(this);
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }



    @OnClick({R.id.menu_transactiondata,
            R.id.menu_platformmonitoring,
            R.id.menu_workorder,
            R.id.menu_gatewayadministration,
            R.id.menu_setting,
            R.id.menu_lin_setting
    })
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.menu_transactiondata://业务数据
                if (mTempFragment != fragmentTransactionData) {
//                    showHomeViewFragment();
                    MainActivityPermissionsDispatcher.showHomeViewFragmentWithPermissionCheck(this);
                }
                openmenu();
                break;
            case R.id.menu_platformmonitoring://平台监控
                if (mTempFragment != platformMonitoringFragment) {
                    showPlatformmonitoring();
                }
                openmenu();
                break;
            case R.id.menu_workorder://工单管理
                if (mTempFragment != fragmentWorkOrder) {
                    showFragmentWorkOrder();
                }
                openmenu();
                break;
            case R.id.menu_gatewayadministration://网关管理
                if (mTempFragment != fragmentGatewayAdministration) {
                    showGatewayAdministration();
                }
                openmenu();

                break;
            case R.id.menu_lin_setting:
            case R.id.menu_setting://设置
                openSet();
                break;

        }

    }

    //显示业务数据页面
    @NeedsPermission({Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION})
    void showHomeViewFragment() {
        if (fragmentTransactionData == null) {
            fragmentTransactionData = new FragmentTransactionFragment();
        }
        switchContentToContentBody(fragmentTransactionData, HOME_TAG);
    }

    @OnNeverAskAgain({Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION})
    void showHomeViewFragment2() {
        if (fragmentTransactionData == null) {
            fragmentTransactionData = new FragmentTransactionFragment();
        }
        switchContentToContentBody(fragmentTransactionData, HOME_TAG);
    }

    //显示平台监控页面
    private void showPlatformmonitoring() {
        if (platformMonitoringFragment == null) {
            platformMonitoringFragment = new PlatformMonitoringFragment();
        }
        switchContentToContentBody(platformMonitoringFragment, PLATFORMMONITORING_TAG);
    }

    //显示工单管理页面
    private void showFragmentWorkOrder() {
        if (fragmentWorkOrder == null) {
            fragmentWorkOrder = new FragmentWorkOrder();
        }
        switchContentToContentBody(fragmentWorkOrder, WORKORDER_TAG);
    }

    //显示网关管理页面
    private void showGatewayAdministration() {
        if (fragmentGatewayAdministration == null) {
            fragmentGatewayAdministration = new FragmentGatewayAdministration();
        }
        switchContentToContentBody(fragmentGatewayAdministration, GATEWAYADMINISTRATION_TAG);
    }


    private void switchContentToContentBody(Fragment to, String tag) {
        if (to != null && mTempFragment != to) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            if (!to.isAdded()) {
                if (mTempFragment == null) {
                    transaction.add(R.id.right, to, tag).commit();
                    LogUtil.deMessage(TAG, to.getClass().getSimpleName() + "  to  add");
                } else {
                    LogUtil.deMessage(TAG, mTempFragment.getClass().getSimpleName() + "  to  hide ### "
                            + to.getClass().getSimpleName() + "  to  show");
                    transaction.add(R.id.right, to, tag).hide(mTempFragment).commit();
                }
            } else {
                if (mTempFragment != to) {
                    transaction.hide(mTempFragment).show(to).commit();
                    LogUtil.deMessage(TAG, mTempFragment.getClass().getSimpleName() + "  to  hide ### "
                            + to.getClass().getSimpleName() + "  to  show");
                }
            }
            mTempFragment = to;
        }
    }

    private void openmenu() {

        drawer.closeDrawer(GravityCompat.START);

    }

    public void closemenu(){
        drawer.openDrawer(GravityCompat.START);
    }



    //设置页面
    private void openSet() {
        Intent setIntent = new Intent(this, SettingActivity.class);
        startActivity(setIntent);
    }



    @Override
    protected boolean getHasTitle() {
        return false;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            long currentTime = TimeTools.getCurrentTimeInLong();
            if ((currentTime - backPressTimeRecord) < 2000) {
                IntentUtils.goExit(this);
            } else {
                backPressTimeRecord = currentTime;
                ToastUtils.show(R.string.rt_exit);
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent != null) {
            boolean isExit = intent.getBooleanExtra(TAG_EXIT, false);
            if (isExit) {
                this.finish();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        MainActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }
}
